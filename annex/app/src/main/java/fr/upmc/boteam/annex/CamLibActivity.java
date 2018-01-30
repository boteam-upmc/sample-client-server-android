package fr.upmc.boteam.annex;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.afollestad.materialcamera.MaterialCamera;

import java.io.File;

public class CamLibActivity extends AppCompatActivity {

    private final String LOG_TAG = "CamLibActivity";
    private final static int CAMERA_RQ = 6969;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cam_lib);

        new MaterialCamera(this)
                .saveDir(getFolder())
                .allowRetry(false) // Whether or not 'Retry' is visible during playback
                .showPortraitWarning(false) // Whether or not a warning is displayed if the user presses record in portrait orientation
                //.retryExits(true)
                .qualityProfile(MaterialCamera.QUALITY_480P)
                .start(CAMERA_RQ);
    }

    private File getFolder() {
        File folder = new File(Environment.getExternalStorageDirectory() + "/OBOApp");

        if (!folder.exists()) {
            boolean isCreated = folder.mkdir();
            Log.i(LOG_TAG, "file created? " + isCreated);
        }

        return folder;
    }
}
