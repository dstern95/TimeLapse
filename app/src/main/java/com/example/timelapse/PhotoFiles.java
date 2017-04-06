package com.example.timelapse;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;

public class PhotoFiles extends AppCompatActivity {

    String[] fileArray;
    private final static String TAG = MainActivity.class.getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_files);
        File loc[] = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "/" + "Timelapse").listFiles();
        fileArray = new String[loc.length];

        for (int i = 0; i < loc.length; i++) {
            fileArray[i] = loc[i].getName();
        }
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fileArray);

        final ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(itemsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String filename = listView.getItemAtPosition(position).toString();

                Intent i = new Intent(PhotoFiles.this, Play.class);
                i.putExtra("foldername", filename);

                startActivity(i);


            }
        });


    }
}