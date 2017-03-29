package com.example.timelapse;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        Intent intent1 = new Intent(this, PhotoView.class);
        startActivity(intent1);

    }
}
