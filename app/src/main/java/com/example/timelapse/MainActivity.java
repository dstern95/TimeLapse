package com.example.timelapse;

import android.Manifest;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    private final static String TAG = MainActivity.class.getName();

    public static final int RequestPermissionCode = 1;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(!checkPerm()){
            requestPerm();
        }

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);


        Log.d(TAG, sharedpreferences.getString("sval","10"));
        Log.d(TAG, "here");
        Log.d(TAG,"sec "+ sharedpreferences.getString("bval2","10"));





    }



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



    public void Photo(View v)
    {
        if(checkCameraHardware(this)){
            Intent intent1 = new Intent(this, Photocapture.class);

            startActivity(intent1);


        }
    }



    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    public void Photoview(View v)
    {
        Log.d(TAG, "here2");


        Intent intent1 = new Intent(this, PhotoFiles.class);
        startActivity(intent1);

        //Intent intent1 = new Intent(this, PhotoView.class);
        //startActivity(intent1);

    }

    public void Settings(View v)
    {
        Log.d(TAG, "here2");



        Intent intent1 = new Intent(this, CaptureSettings.class);
        startActivity(intent1);

        //Intent intent1 = new Intent(this, PhotoView.class);
        //startActivity(intent1);

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
