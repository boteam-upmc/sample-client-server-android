package fr.upmc.boteam.annex;

import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.content.res.Configuration;
import android.util.Log;
import android.widget.FrameLayout.LayoutParams;
import android.widget.RelativeLayout;


public class MainActivity extends Activity {

    public static final String LOG_TAG = "MainActivity";

    private Camera camera;
    private int cameraID;
    private CameraPreview camPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (setCameraInstance()) {
            this.camPreview = new CameraPreview(this);

        } else {
            this.finish();
        }

        RelativeLayout preview = findViewById(R.id.preview_layout);
        preview.addView(this.camPreview);

        RelativeLayout.LayoutParams previewLayout = (RelativeLayout.LayoutParams) camPreview.getLayoutParams();
        previewLayout.width = LayoutParams.MATCH_PARENT;
        previewLayout.height = LayoutParams.MATCH_PARENT;
        this.camPreview.setLayoutParams(previewLayout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (setCameraInstance()) {
            camPreview.refreshDrawableState();
        }
        else {
            Log.e(MainActivity.LOG_TAG, "onResume(): can't reconnect the camera");
            this.finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCameraInstance();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseCameraInstance();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private boolean setCameraInstance() {
        if (this.camera != null) {
            Log.i(MainActivity.LOG_TAG, "setCameraInstance(): camera is already set, nothing to do");
            return true;
        }

        if (Build.VERSION.SDK_INT >= 15) {

            if (this.cameraID < 0) {
                Camera.CameraInfo camInfo = new Camera.CameraInfo();
                for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
                    Camera.getCameraInfo(i, camInfo);

                    if (camInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                        try {
                            this.camera = Camera.open(i);
                            this.cameraID = i;
                            return true;

                        } catch (RuntimeException e) {
                            Log.e(MainActivity.LOG_TAG, "setCameraInstance(): trying to open camera #" + i + " but it's locked", e);
                        }
                    }
                }
            } else {
                try {
                    this.camera = Camera.open(this.cameraID);

                } catch (RuntimeException e){
                    Log.e(MainActivity.LOG_TAG, "setCameraInstance(): trying to re-open camera #" + this.cameraID + " but it's locked", e);
                }
            }
        }

        if (this.camera == null) {
            try {
                this.camera = Camera.open();
                this.cameraID = 0;

            } catch (RuntimeException e) {
                Log.e(MainActivity.LOG_TAG,
                        "setCameraInstance(): trying to open default camera but it's locked. "
                                + "The camera is not available for this app at the moment.", e
                );
                return false;
            }
        }

        Log.i(MainActivity.LOG_TAG, "setCameraInstance(): successfully set camera #" + this.cameraID);
        return true;
    }

    private void releaseCameraInstance() {
        if (this.camera != null) {
            try {
                this.camera.stopPreview();
            }
            catch (Exception e) {
                Log.i(MainActivity.LOG_TAG, "releaseCameraInstance(): tried to stop a non-existent preview, this is not an error");
            }

            this.camera.setPreviewCallback(null);
            this.camera.release();
            this.camera = null;
            this.cameraID = -1;
            Log.i(MainActivity.LOG_TAG, "releaseCameraInstance(): camera has been released.");
        }
    }

    public Camera getCamera() {
        return this.camera;
    }

    public int getCameraID() {
        return this.cameraID;
    }
}