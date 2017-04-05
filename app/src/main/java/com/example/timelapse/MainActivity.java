package com.example.timelapse;

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
    private final static String TAG = MainActivity.class.getName();

    public static final int RequestPermissionCode = 1;
    public NumberPicker frameRatePick  = null;
    private float capRate;
    public ArrayList<String> tracker = new ArrayList<>();
    public ArrayList<String> namestracker = new ArrayList<>();

    public static final int SECOND_CODE = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(!checkPerm()){
            requestPerm();
        }

        //Makes a dedicataed folder to store files related to this app
        File f = new File(Environment.getExternalStorageDirectory(), "PowerLapseFiles");
        if (!f.exists()) {
            f.mkdirs();
        }

        String error = "Um with great power comes great responsibility and we promise to use it wisely but we need these permissions";

        if(!checkPerm()){
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }

        capRate = 1;
        frameRatePick = (NumberPicker) findViewById(R.id.numberPicker);
        frameRatePick.setMaxValue(10);
        frameRatePick.setMinValue(1);
        frameRatePick.setValue(1);
        frameRatePick.setWrapSelectorWheel(false);
        //frameRatePick.setOnValueChangedListener(this);
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
            intent1.putExtra("fps", capRate);
            intent1.putStringArrayListExtra("tracker", tracker);
            startActivityForResult(intent1, SECOND_CODE);
        }
    }

    @Override
    protected void onActivityResult(int request, int result, Intent i){
        super.onActivityResult(request, result, i);
        if(request == SECOND_CODE){
            if(result == RESULT_OK){
                String name = i.getStringArrayExtra("fname")[0]; //strs
                String fname = i.getStringArrayExtra("fname")[1];
                namestracker.add(name);
                tracker.add(fname);
            }
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
