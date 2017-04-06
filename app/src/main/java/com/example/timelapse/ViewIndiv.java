package com.example.timelapse;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

//This file implements viewing the a timelapse file


public class ViewIndiv extends AppCompatActivity implements SurfaceHolder.Callback {
    SurfaceView mSurface;
    SurfaceHolder mHolder;
    MediaPlayer mp;
    boolean pausing = false;
    String stringPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_indiv);
        Intent i = new Intent();
        Bundle extra = getIntent().getExtras();
        String fname = extra.getString("filename"); //Gte name of file we want to view
        stringPath = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MOVIES) + "/Timelapse/" + fname;
        getWindow().setFormat(PixelFormat.UNKNOWN);
        mSurface = (SurfaceView) findViewById(R.id.surfaceview);
        mHolder = mSurface.getHolder();
        mHolder.addCallback(this);
        mHolder.setFixedSize(176, 144);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mp = new MediaPlayer();
    }

        //Connects to SurfaceView so when you press anywhere on the screen it plays the video
        public void onClick(View v) {
                // TODO Auto-generated method stub
            pausing = false;

            if (mp.isPlaying()) { //Check if it is already playing
                mp.reset();
            }

            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mp.setDisplay(mHolder);

            try {
                mp.setDataSource(stringPath);
                mp.prepare();
            } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalStateException e) {
                    // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                    // TODO Auto-generated catch block
                e.printStackTrace();
            }

            mp.start();
            }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

        //Nothing to do here

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //Nothing to do here

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //Nothing to do here
    }
}
