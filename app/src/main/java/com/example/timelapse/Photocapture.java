package com.example.timelapse;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.PowerManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Surface;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Photocapture extends AppCompatActivity {
    public static final String MyPREFERENCES = "MyPrefs";

    SharedPreferences sharedpreferences;
    private boolean isRec; //To check if the camera is recording right now

    Bundle camsavedInstanceState;

    boolean doneRec; //Check if we are done recording once

    private float CframeRate = 1.0f;

    private final static String TAG = Photocapture.class.getName();

    private Camera mCamera; //Camera object
    private CameraPreview mPreview; //Preview Object
    MediaRecorder medrec;

    private SurfaceHolder mHolder;
    String tlapsename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            getSupportActionBar().hide();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_photocapture);
        camsavedInstanceState = savedInstanceState;
        medrec = new MediaRecorder();
        mCamera = getCameraInstance();
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        Camera.Parameters p = mCamera.getParameters();

        if (p.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
            p.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO); //This is essentially specifying what the camera does essentially
        }
        mCamera.setParameters(p);

        //Now to set the display of the preview and manage its look

        Display d = this.getWindowManager().getDefaultDisplay();
        int o = d.getRotation();

        if (o == Surface.ROTATION_0) //Portrait View
        {
            mCamera.setDisplayOrientation(270); //Realigning
            medrec.setOrientationHint(270); //Aligning media recorder with camera
        } else if (o == Surface.ROTATION_90) {
            mCamera.setDisplayOrientation(180);
            medrec.setOrientationHint(180);
        } else if (o == Surface.ROTATION_180) {
            mCamera.setDisplayOrientation(90);
            medrec.setOrientationHint(90);
        } else {
            mCamera.setDisplayOrientation(0);
            medrec.setOrientationHint(0);
        }

        preview.addView(mPreview); //Finally show the preview
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCam();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMedRec();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_photocapture);
        medrec = new MediaRecorder(); //Recreating one

        mCamera = getCameraInstance();

        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);

        Camera.Parameters p = mCamera.getParameters();

        Display d = this.getWindowManager().getDefaultDisplay();
        int o = d.getRotation();

        if (o == Surface.ROTATION_0) //Portrait View
        {
            mCamera.setDisplayOrientation(270); //Realigning
            medrec.setOrientationHint(270); //Aligning media recorder with camera
        } else if (o == Surface.ROTATION_90) {
            mCamera.setDisplayOrientation(180);
            medrec.setOrientationHint(180);
        } else if (o == Surface.ROTATION_180) {
            mCamera.setDisplayOrientation(90);
            medrec.setOrientationHint(90);
        } else {
            mCamera.setDisplayOrientation(0);
            medrec.setOrientationHint(0);
        }

        preview.addView(mPreview); //Finally show the preview
    }

    public void capture(View v) {
        //starts or stops the video record at the desired frame rate
        Button b1 = (Button) findViewById(R.id.captureButton);
        if (isRec) {
            b1.setText("CAPTURE");

            medrec.stop(); //Stop Recorder
            releaseMedRec();
            mCamera.lock(); //Regain camera access

            isRec = false;
            doneRec = true;
            onResume();
        } else {
            b1.setText("STOP");
            Log.d(TAG, Float.toString(CframeRate));
            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            //gets the frame rate
            CframeRate = 1/(Float.valueOf(sharedpreferences.getString("bval2","10"))*Float.valueOf(sharedpreferences.getString("sval","10")));


            mCamera.unlock();
            medrec.setCamera(mCamera);
            medrec.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            CamcorderProfile cprof = CamcorderProfile.get(CamcorderProfile.QUALITY_TIME_LAPSE_1080P);
            Log.d(TAG, Float.toString(cprof.videoFrameRate));
            medrec.setCaptureRate(CframeRate);
            medrec.setProfile(cprof);
            medrec.setOutputFile(getOutputMediaFile().toString());
            medrec.setPreviewDisplay(mPreview.getHolder().getSurface());
            if (prepareVideoRecorder()) {
                try {
                    medrec.prepare();
                }
                catch (Exception e)
                {

                }
                medrec.start();
                isRec = true;
            }
        }
    }



    /**
     * A basic Camera preview class
     */
    public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

        public CameraPreview(Context context, Camera camera) {
            super(context);
            mCamera = camera;

            // Install a SurfaceHolder.Callback so we get notified when the
            // underlying surface is created and destroyed.
            mHolder = getHolder();
            mHolder.addCallback(this);
            // deprecated setting, but required on Android versions prior to 3.0
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        public void surfaceCreated(SurfaceHolder holder) {
            // The Surface has been created, now tell the camera where to draw the preview.
            try {
                mCamera.setPreviewDisplay(mHolder);
                mCamera.startPreview();
            } catch (IOException e) {
                Log.d(TAG, "Error setting camera preview: " + e.getMessage());
            }
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            // empty. Take care of releasing the Camera preview in your activity.
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
            // If your preview can change or rotate, take care of those events here.
            // Make sure to stop the preview before resizing or reformatting it.

            if (mHolder.getSurface() == null) {
                // preview surface does not exist
                return;
            }

            // stop preview before making changes
            try {
                mCamera.stopPreview();
            } catch (Exception e) {
                // ignore: tried to stop a non-existent preview
            }

            // set preview size and make any resize, rotate or
            // reformatting changes here

            // start preview with new settings
            try {
                mCamera.setPreviewDisplay(mHolder);
                mCamera.startPreview();

            } catch (Exception e) {
                Log.d(TAG, "Error starting camera preview: " + e.getMessage());
            }
        }
    }

    /**
     * A safe way to get an instance of the Camera object.
     */
    public static Camera getCameraInstance()
    {
        Camera c = null;
        try
        {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e)
        {
            // Camera is not available (in use or does not exist)
            e.printStackTrace();
        }
        return c; // returns null if camera is unavailable
    }

    //Make Video Recorder ready to start recording
    private boolean prepareVideoRecorder() {
        try {
            medrec.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            medrec.release();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            medrec.release();
            return false;
        }
        return true;
    }

    //Release the media recorder
    private void releaseMedRec() {
        if (medrec != null) {
            medrec.reset();
            medrec.release();
            medrec = null;
            if(mCamera!=null){
                mCamera.lock();
            }

        }
    }

    //Release the Camera
    private void releaseCam() {
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    private String getOutputMediaFile() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.


        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MOVIES), "Timelapse");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                System.out.println("System failed to make directory!");
                return null;
            }
        }
        // Create a media file name
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        tlapsename = sharedpreferences.getString("filename","vid");
        if (tlapsename.equals(""))
        {
            tlapsename = "vid";
        }
        if (tlapsename==null)
        {
            tlapsename = "vid";

        }
        int i = 0;
        String tmpname = tlapsename;
        while (ename(tlapsename) == false)
        {
            i+=1;
            tlapsename = tmpname+"-"+Integer.toString(i);

        }



        String ms =mediaStorageDir.getPath() + File.separator + tlapsename + ".MP4";
        Toast.makeText(this, "saved as "+tlapsename, Toast.LENGTH_SHORT).show();

        return ms;
    }
    public boolean ename(String fname)
    {
        //checks if file already exists
        File loc[] = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "/" + "Timelapse").listFiles();
        fname = fname+".MP4";
        for (int i = 0; i < loc.length; i++) {
            Log.d(TAG, loc[i].getName() +"="+fname);

            if (loc[i].getName().equals(fname))
            {
                    return false;
            }

        }

        return true;
    }
}

























