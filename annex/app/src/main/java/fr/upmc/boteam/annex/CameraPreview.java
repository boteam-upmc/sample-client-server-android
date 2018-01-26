package fr.upmc.boteam.annex;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;

import static android.media.MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED;
import static android.media.MediaRecorder.MEDIA_RECORDER_INFO_MAX_FILESIZE_REACHED;
import static android.media.MediaRecorder.MEDIA_RECORDER_INFO_UNKNOWN;


public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback, PictureCallback {

    private final float ASPECT_RATIO_W = 4.0f;
    private final float ASPECT_RATIO_H = 3.0f;

    private byte[] previewBuffer;
    private SurfaceHolder surfaceHolder;

    private MediaRecorder recorder;

    private Socket mSocket;
    {
        try {
            // Change this to your localhost ip address example : 192.168.42.91
            mSocket = IO.socket("http://192.168.42.187:3000/");


        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public CameraPreview(Context context) {
        super(context);

        surfaceHolder = this.getHolder();
        surfaceHolder.addCallback(this);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setupCamera();
        //new SetupCameraAsyncTask().execute();
        startCameraPreview(holder);

        recorder = new MediaRecorder();
        initRecorder("rec");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        if (this.surfaceHolder.getSurface() == null) {
            Log.e(MainActivity.LOG_TAG, "surfaceChanged(): surfaceHolder is null, nothing to do.");
            return;
        }

        updateCameraDisplayOrientation();

        try {
            recorder.setPreviewDisplay(surfaceHolder.getSurface());
            recorder.prepare();
            //recorder.start();

        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }
    }

    private void initRecorder(String recordName) {
        final String EXTENSION = ".mp4";
        final String SEPARATOR = File.separator;
        final int QUALITY = CamcorderProfile.QUALITY_HIGH;
        final int MAX_DURATION = 5000; // 50000 = 50 seconds
        //final int MAX_FILE_SIZE = 5000000; // Approximately 5 megabytes

        recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        recorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);

        CamcorderProfile cpHigh = CamcorderProfile.get(QUALITY);
        recorder.setProfile(cpHigh);
        recorder.setOutputFile(getDirectory() + SEPARATOR + recordName + EXTENSION);
        recorder.setMaxDuration(MAX_DURATION);
        //recorder.setMaxFileSize(MAX_FILE_SIZE);
    }

    @NonNull
    private String getDirectory() {
        File folder = new File(Environment.getExternalStorageDirectory() + "/OBOApp");

        if (!folder.exists()) {
            boolean success = folder.mkdir();
            Log.i(MainActivity.LOG_TAG, "file created? " + success);
        }

        return folder.getPath();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {}

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        float ratio = ASPECT_RATIO_H / ASPECT_RATIO_W;

        if (width > height * ratio) {
            width = (int) (height / ratio + .5);
        } else {
            height = (int) (width / ratio + .5);
        }

        setMeasuredDimension(width, height);
        Log.i(MainActivity.LOG_TAG, "onMeasure(): set surface dimension to " + width + "x" + height);
    }

    private void setupCamera() {
        final int PREVIEW_MAX_WIDTH = 640;
        final int PICTURE_MAX_WIDTH = 1280;

        MainActivity parent = (MainActivity)this.getContext();
        Camera camera = parent.getCamera();

        if (camera == null) {
            Log.e(MainActivity.LOG_TAG, "setupCamera(): warning, camera is null");
            return;
        }

        Camera.Parameters parameters = camera.getParameters();

        Size bestPreviewSize = getBestSize(parameters.getSupportedPreviewSizes(), PREVIEW_MAX_WIDTH);
        Size bestPictureSize = getBestSize(parameters.getSupportedPictureSizes(), PICTURE_MAX_WIDTH);

        parameters.setPreviewSize(bestPreviewSize.width, bestPreviewSize.height);
        parameters.setPictureSize(bestPictureSize.width, bestPictureSize.height);

        parameters.setPreviewFormat(ImageFormat.NV21); // NV21 is the most supported format for preview frames
        parameters.setPictureFormat(ImageFormat.JPEG); // JPEG for full resolution images

        // example of settings
        try {
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);

        } catch (NoSuchMethodError e) {
            Log.e(MainActivity.LOG_TAG, "setupCamera(): this camera ignored some unsupported settings.", e);
        }

        camera.setParameters(parameters); // save everything

        int prevWidth = camera.getParameters().getPreviewSize().width;
        int prevHeight = camera.getParameters().getPreviewSize().height;
        int picWidth = camera.getParameters().getPictureSize().width;
        int picHeight = camera.getParameters().getPictureSize().height;

        Log.d(MainActivity.LOG_TAG, "setupCamera(): settings applied:\n\t"
                + "preview size: " + prevWidth + "x" + prevHeight + "\n\t"
                + "picture size: " + picWidth + "x" + picHeight
        );

        try {
            this.previewBuffer = new byte[prevWidth * prevHeight * ImageFormat.getBitsPerPixel(camera.getParameters().getPreviewFormat()) / 8];
            setCameraCallback();

        } catch (IOException e) {
            Log.e(MainActivity.LOG_TAG, "setupCamera(): error setting camera callback.", e);
        }
    }

    private void setCameraCallback() throws IOException {
        MainActivity parent = (MainActivity)this.getContext();
        Camera camera = parent.getCamera();

        camera.addCallbackBuffer(this.previewBuffer);
        camera.setPreviewCallbackWithBuffer(new PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] data, Camera cam) {
                processFrame(previewBuffer);
                cam.addCallbackBuffer(previewBuffer);
            }
        });
    }

    private Size getBestSize(List<Size> sizes, int widthThreshold) {
        Size bestSize = null;

        for (Size currentSize : sizes) {
            boolean isDesiredRatio = ((currentSize.width / ASPECT_RATIO_W) == (currentSize.height / ASPECT_RATIO_H));
            boolean isBetterSize = (bestSize == null || currentSize.width > bestSize.width);
            boolean isInBounds = currentSize.width <= widthThreshold;

            if (isDesiredRatio && isInBounds && isBetterSize) {
                bestSize = currentSize;
            }
        }

        if (bestSize == null) {
            bestSize = sizes.get(0);
            Log.e(MainActivity.LOG_TAG, "determineBestSize(): can't find a good size. Setting to the very first...");
        }

        Log.i(MainActivity.LOG_TAG, "determineBestSize(): bestSize is " + bestSize.width + "x" + bestSize.height);
        return bestSize;
    }

    private synchronized void startCameraPreview(SurfaceHolder holder) {
        MainActivity parent = (MainActivity)this.getContext();
        Camera camera = parent.getCamera();

        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (Exception e){
            Log.e(MainActivity.LOG_TAG, "startCameraPreview(): error starting camera preview", e);
        }
    }

    private void updateCameraDisplayOrientation() {
        MainActivity parent = (MainActivity)this.getContext();
        Camera camera = parent.getCamera();
        int cameraID = parent.getCameraID();

        if (camera == null) {
            Log.e(MainActivity.LOG_TAG, "updateCameraDisplayOrientation(): warning, camera is null");
            return;
        }

        int result;
        Activity parentActivity = (Activity)this.getContext();

        int rotation = parentActivity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;

        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }

        if (Build.VERSION.SDK_INT >= 15) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(cameraID, info);

            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                result = (info.orientation + degrees) % 360;
                result = (360 - result) % 360;  // compensate the mirror

            } else {
                result = (info.orientation - degrees + 360) % 360;
            }
        } else {
            result = Math.abs(degrees - 90);
        }

        camera.setDisplayOrientation(result); // save settings
    }

    @Override
    public void onPictureTaken(byte[] raw, Camera cam) {}

    /** TODO change here for data frame processing
      * Example : convert byte[] to another type
      */
    private void processFrame(byte[] raw) {
        /*Log.d(MainActivity.LOG_TAG, "processFrame() : raw[9] is " + raw[9]);

        try {
            JSONObject rawObj = new JSONObject();

            rawObj.put("raw", raw);
            mSocket.emit("getStream", rawObj);

        } catch (JSONException e) {
            e.printStackTrace();
        }*/
    }

    private class SetupCameraAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            //mSocket.connect();
            setupCamera();

            return null;
        }
    }
}
