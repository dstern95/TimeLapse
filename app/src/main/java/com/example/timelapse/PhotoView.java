package com.example.timelapse;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class PhotoView extends AppCompatActivity {
    String[] fileArray;
    private final static String TAG = MainActivity.class.getName();
    public boolean running;
    public int ct;
    Thread t;
    Runnable r = new PhotoView.MyRunnable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);

        File loc = new File(Environment.getExternalStorageDirectory() + "/" + "timelapsefiles/", "go");
        File[] files = loc.listFiles();
        ct = 0;
        //If there are files in the directory
        if (files != null) {
            fileArray = new String[files.length];

            for (int i = 0; i < fileArray.length; i++) {
                fileArray[i] = files[i].getName();
                ct+= 1;
            }
        }
        else {
            fileArray = new String[0];
        }
        Log.d(TAG, Integer.toString(ct));
        Log.d(TAG, fileArray[1]);

/*
        Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/" + "timelapsefiles/" +"go/"+
                File.separator+"IMG_20170331_004200.jpg");
        ImageView iv = (ImageView)findViewById(R.id.iv_image1);
        iv.setImageBitmap(bitmap);
        */

    }



    @Override
    protected void onResume()
    {
        super.onResume();
        int i =0;
        while (i < ct) {

            Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/" + "timelapsefiles/" + "go/" +
                    File.separator + fileArray[i]);
            ImageView iv = (ImageView) findViewById(R.id.iv_image1);
            iv.setImageBitmap(bitmap);
            i+=1;
            try {
                TimeUnit.SECONDS.sleep(2);
            }catch (InterruptedException e1){
                running = false;
                return;
            }


        }


    }
    protected void onPause()
    {
        super.onPause();
        running = false;
    }


    class MyRunnable implements Runnable {


        public void run() {
            //thread for microphone records in byte[]
            // mCamera.takePicture(null, null, mPicture);while ()
            Log.d(TAG, "pre loop");
            int i = 0;
            while(running)
            {

                if (i < ct) {

                    Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/" + "timelapsefiles/" + "go/" +
                            File.separator + fileArray[i]);
                    ImageView iv = (ImageView) findViewById(R.id.iv_image1);
                    iv.setImageBitmap(bitmap);
                    i+=1;

                }
                try {
                    Thread.currentThread().sleep(2000);
                }catch (InterruptedException e1){
                    running = false;
                    return;
                }




            }
            Log.d(TAG, "post loop");



        }
    }




}
