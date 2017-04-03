package com.example.timelapse;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Picture;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ViewFlipper;


import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Timer;
import java.util.TimerTask;


public class PhotoView extends AppCompatActivity {
    String[] fileArray;
    Thread t;
    Runnable r = new PhotoView.MyRunnable();

    private ImageView _imagView;
    private Timer _timer;
    private int _index;
    private MyHandler handler;
    private final static String TAG = MainActivity.class.getName();
    int findex = 0;

    private ViewFlipper mViewFlipper;
    File[] files;


    public boolean running;
    public int ct;

    Bitmap[] bitt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);
        Bundle extras = getIntent().getExtras();
        String fname = extras.getString("foldername");

        File path = new File(Environment.getExternalStorageDirectory() + "/" + "timelapsefiles/" + fname+"/");
        files = path.listFiles();

        //t = new Thread(r, "record");
        //t.start();




    }

    @Override
    protected void onResume()
    {
        super.onResume();

        Log.d(TAG, String.valueOf(files.length) + "****");
        //ImageView jpgview = (ImageView) findViewById(R.id.photo_view);
        //Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/" + "timelapsefiles/" + "sunrise/");
        //Drawable myDrawable = new BitmapDrawable(getResources(), Environment.getExternalStorageDirectory() + "/" + "timelapsefiles/" + "sunrise");
        handler= new MyHandler();
        _imagView=(ImageView) findViewById(R.id.photo_view);
        _index=0;
        _timer= new Timer();
        _timer.schedule(new TickClass(), 0, 400);

    }
    @Override
    protected void onPause()
    {
        super.onPause();
        _timer.cancel();
    }

    class MyRunnable implements Runnable {


        public void run() {
            bitt = new Bitmap[files.length];
            for(int i =0;i<files.length;i++)
            {
                bitt[i] =BitmapFactory.decodeFile(files[i].getAbsolutePath());
            }

            Log.d(TAG,"done");



        }
    }

    public void startdisplay(View v)
    {

        handler= new MyHandler();
        _imagView=(ImageView) findViewById(R.id.photo_view);
        _index=0;
        _timer= new Timer();
        _timer.schedule(new TickClass(), 0, 600);


    }


    private class TickClass extends TimerTask
    {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            handler.sendEmptyMessage(_index);
            Log.d(TAG, "index = " + String.valueOf(_index));
            _index++;
        }
    }

    private class MyHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);

            try {

                Bitmap bmp= BitmapFactory.decodeFile(files[findex].getAbsolutePath());
                //Bitmap bmp = bitt[findex];
                Log.d(TAG, String.valueOf(findex) + "SHOWING THIS");
                findex+=1;
                _imagView.setImageBitmap(bmp);
                Log.v("Loading Image: ",findex+"");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                Log.v("Exception in Handler ",e.getMessage());
            }
        }
    }
}
        /*File loc = new File(Environment.getExternalStorageDirectory() + "/" + "timelapsefiles/", "sunrise");
        File[] files = loc.listFiles();
        ct = 0;
        //If there are files in the directory
        if (files != null) {
            fileArray = new String[files.length];

            for (int i = 0; i < fileArray.length; i++) {
                fileArray[i] = files[i].getName();
                ct += 1;
            }
        } else {
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


        // Get the ViewFlipper
        /*mViewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);

        // Add all the images to the ViewFlipper
        for (int i = 0; i < 2; i++) {
            ImageView imageView = new ImageView(this);
            Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/" + "timelapsefiles/" + "sunrise/" +
                    File.separator + fileArray[i]);
            imageView.setImageBitmap(bitmap);
            mViewFlipper.addView(imageView);
        }
        mViewFlipper.setAutoStart(true);
        mViewFlipper.setFlipInterval(2000);

    }*/





