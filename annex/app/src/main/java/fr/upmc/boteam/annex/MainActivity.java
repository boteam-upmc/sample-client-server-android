package fr.upmc.boteam.annex;

import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        SurfaceHolder.Callback , MediaRecorder.OnInfoListener {

    final String LOG_TAG = "MainActivity";

    private int recordsCounter;
    private SurfaceView cameraView;
    private MediaRecorder recorder;
    private SurfaceHolder holder;
    private boolean recording;
    {
        recordsCounter = 0;
        recording = false;
    }

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://192.168.42.91:3000/");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        recorder = new MediaRecorder();
        recorder.setOnInfoListener(this);
        initRecorder("rec" + recordsCounter);

        cameraView = findViewById(R.id.sv_camera);
        holder = cameraView.getHolder();
        holder.addCallback(this);

        cameraView.setClickable(true);
        cameraView.setOnClickListener(this);

        Log.i(LOG_TAG, "Try to connect..");

        mSocket.connect();
        mSocket.emit("setStream", true);
    }

    public static final String path = "/storage/emulated/0/OBOApp/";
    FileInputStream fis = null;
    BufferedInputStream bis = null;

    private void handleActionSendVideo(String param) {
        if(param != null) {
            String videoPath = path + param + ".mp4";
            int n;
            try {
                File file = new File(videoPath);
                byte[] buffer = new byte[(int)file.length()];
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                System.out.println("Sending " + videoPath + "(" + buffer.length + " bytes)");
                while((n = bis.read(buffer,0,buffer.length)) != -1){
                    mSocket.emit("setStream", buffer);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initRecorder(String recordName) {
        final String EXTENSION = ".mp4";
        final String SEPARATOR = File.separator;
        final int QUALITY = CamcorderProfile.QUALITY_480P;
        final int MAX_DURATION = 5000; // 10000 = 10 seconds
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
            Log.i(LOG_TAG, "file created? " + success);
        }

        return folder.getPath();
    }

    private void prepareRecorder() {
        recorder.setPreviewDisplay(holder.getSurface());

        try {
            recorder.prepare();

        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSocket.disconnect();
    }

    @Override
    public void onInfo(MediaRecorder mediaRecorder, int i, int i1) {}

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        prepareRecorder();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {}

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {}

    @Override
    public void onClick(View view) {
        if (recording) {
            Log.i(LOG_TAG, "STOP");
            recorder.stop();
            recording = false;

            // Let's initRecorder so we can record again
            initRecorder("rec" + recordsCounter / 2);
            prepareRecorder();

        } else {
            Log.i(LOG_TAG, "START");
            recording = true;
            recorder.start();
        }
    }
}
