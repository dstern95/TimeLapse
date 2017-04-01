package com.example.timelapse;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Picture;
import android.media.Image;
import android.os.Environment;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ViewFlipper;


import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;

public class PhotoView extends AppCompatActivity {
    String[] fileArray;
    private final static String TAG = MainActivity.class.getName();

    private ViewFlipper mViewFlipper;


    public boolean running;
    public int ct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);

        File loc = new File(Environment.getExternalStorageDirectory() + "/" + "timelapsefiles/", "sunrise");
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


        // Get the ViewFlipper
        mViewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);

        // Add all the images to the ViewFlipper
        for (int i = 0; i < 2; i++) {
            ImageView imageView = new ImageView(this);
            Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/" + "timelapsefiles/" +"sunrise/"+
                    File.separator+fileArray[i]);
            imageView.setImageBitmap(bitmap);
            mViewFlipper.addView(imageView);
        }
        mViewFlipper.setAutoStart(true);
        mViewFlipper.setFlipInterval(2000);


    }

    public class SequenceEncoder {
        private SeekableByteChannel ch;
        private Picture toEncode;
        private RgbToYuv420 transform;
        private H264Encoder encoder;
        private ArrayList<ByteBuffer> spsList;
        private ArrayList<ByteBuffer> ppsList;
        private CompressedTrack outTrack;
        private ByteBuffer _out;
        private int frameNo;
        private MP4Muxer muxer;

        public SequenceEncoder(File out) throws IOException {
            this.ch = NIOUtils.writableFileChannel(out);

            // Transform to convert between RGB and YUV
            transform = new RgbToYuv420(0, 0);

            // Muxer that will store the encoded frames
            muxer = new MP4Muxer(ch, Brand.MP4);

            // Add video track to muxer
            outTrack = muxer.addTrackForCompressed(TrackType.VIDEO, 25);

            // Allocate a buffer big enough to hold output frames
            _out = ByteBuffer.allocate(1920 * 1080 * 6);

            // Create an instance of encoder
            encoder = new H264Encoder();

            // Encoder extra data ( SPS, PPS ) to be stored in a special place of
            // MP4
            spsList = new ArrayList<ByteBuffer>();
            ppsList = new ArrayList<ByteBuffer>();

        }

        public void encodeImage(BufferedImage bi) throws IOException {
            if (toEncode == null) {
                toEncode = Picture.create(bi.getWidth(), bi.getHeight(), ColorSpace.YUV420);
            }

            // Perform conversion
            for (int i = 0; i < 3; i++)
                Arrays.fill(toEncode.getData()[i], 0);
            transform.transform(AWTUtil.fromBufferedImage(bi), toEncode);

            // Encode image into H.264 frame, the result is stored in '_out' buffer
            _out.clear();
            ByteBuffer result = encoder.encodeFrame(_out, toEncode);

            // Based on the frame above form correct MP4 packet
            spsList.clear();
            ppsList.clear();
            H264Utils.encodeMOVPacket(result, spsList, ppsList);

            // Add packet to video track
            outTrack.addFrame(new MP4Packet(result, frameNo, 25, 1, frameNo, true, null, frameNo, 0));

            frameNo++;
        }

        public void finish() throws IOException {
            // Push saved SPS/PPS to a special storage in MP4
            outTrack.addSampleEntry(H264Utils.createMOVSampleEntry(spsList, ppsList));

            // Write MP4 header and finalize recording
            muxer.writeHeader();
            NIOUtils.closeQuietly(ch);
        }
    }


}




