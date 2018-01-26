package fr.upmc.boteam.annex;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.afollestad.materialcamera.MaterialCamera;

public class CamLibActivity extends AppCompatActivity {

    private final static int CAMERA_RQ = 6969;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cam_lib);

        new MaterialCamera(this)
                .start(CAMERA_RQ);
    }
}
