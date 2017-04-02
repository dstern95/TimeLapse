package com.example.timelapse;

import android.Manifest;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //String folder_main = "NewFolder";
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.WAKE_LOCK}, 001);

        File f = new File(Environment.getExternalStorageDirectory(), "timelapsefiles");
        if (!f.exists()) {
            f.mkdirs();
        }
    }
    public void Photo(View v)
    {
        Log.d(TAG, "here2");

        Intent intent1 = new Intent(this, Photocapture.class);
        startActivity(intent1);

    }

    public void Photoview(View v)
    {
        Log.d(TAG, "here2");


        Intent intent1 = new Intent(this, PhotoFiles.class);
        startActivity(intent1);

        //Intent intent1 = new Intent(this, PhotoView.class);
        //startActivity(intent1);

    }
}
