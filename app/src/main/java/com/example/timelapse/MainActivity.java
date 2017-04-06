package com.example.timelapse;


//mainActivity + Home Screen for PowerLapse

import android.Manifest;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {

    //using free gift
    
    private final static String TAG = MainActivity.class.getName();

    public static final int RequestPermissionCode = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //We check if we obtain permissions

        if(!checkPerm()){
            requestPerm();
        }

        //Makes a dedicated folder to store files related to this app
        File f = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MOVIES), "Timelapse");

        //Create file if app-specific folder does not exist
        if (!f.exists()) {
            f.mkdirs();
        }

        String error = "With great power comes great responsibility and we promise to use it wisely.";

        if(!checkPerm()){
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }

    }


    //Requesting permissions
    private void requestPerm(){
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, RequestPermissionCode);
    }



    public boolean checkPerm(){
        int r1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int r2 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        boolean c;
        if(r1 == PackageManager.PERMISSION_GRANTED && r2 == PackageManager.PERMISSION_GRANTED){
            c = true;
        }
        else{
            c = false;
        }
        return c;
    }


    //Links to the stat button and we check if device has a camera, and if it does it starts the recording activity
    public void Photo(View v)
    {
        if(checkCameraHardware(this)){
            Intent intent1 = new Intent(this, Photocapture.class);
            startActivity(intent1);
        }
    }


    //Implements the check to see if device has camera
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }


    //Links to the gallery button. takes user to list of previous timelapses
    public void Photoview(View v)
    {
        Intent intent1 = new Intent(this, PhotoFiles.class);
        startActivity(intent1);

    }

    //Links to the settings button - takes user to settings menu
    public void Settings(View v)
    {
        Intent intent1 = new Intent(this, CaptureSettings.class);
        startActivity(intent1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu m) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.captmenu, m);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                Intent intent1 = new Intent(this, CaptureSettings.class);
                startActivity(intent1);
                break;

        }
        return true;
    }
}
