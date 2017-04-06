package com.example.timelapse;

import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class ViewIndiv extends AppCompatActivity implements SurfaceHolder.Callback {

    SurfaceView mSurface;
    SurfaceHolder mHolder;
    MediaPlayer mp;
    boolean pausing = false;
    String stringPath = "/sdcard/Movies/Timelapse/VID_2017_04_05_01_05_43.MP4";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_indiv);
        Button buttonPlayVideo = (Button) findViewById(R.id.playvideoplayer);
        Button buttonPauseVideo = (Button) findViewById(R.id.pausevideoplayer);

        getWindow().setFormat(PixelFormat.UNKNOWN);
        mSurface = (SurfaceView) findViewById(R.id.surfaceview);
        mHolder = mSurface.getHolder();
        mHolder.addCallback(this);
        mHolder.setFixedSize(176, 144);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mp = new MediaPlayer();

        buttonPlayVideo.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                pausing = false;

                if (mp.isPlaying()) {
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
        });

        buttonPauseVideo.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (pausing) {
                    pausing = false;
                    mp.start();
                } else {
                    pausing = true;
                    mp.pause();
                }
            }
        });

    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
// TODO Auto-generated method stub

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
// TODO Auto-generated method stub

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
// TODO Auto-generated method stub
    }
}
