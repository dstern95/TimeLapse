package com.example.timelapse;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;

public class PhotoFiles extends AppCompatActivity {

    String[] fileArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_files);
        File loc[] = new File(Environment.getExternalStorageDirectory() + "/" + "timelapsefiles").listFiles();
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

                Intent i = new Intent(PhotoFiles.this, PhotoView.class);
                i.putExtra("foldername", filename);

                startActivity(i);


            }
        });

    }
}