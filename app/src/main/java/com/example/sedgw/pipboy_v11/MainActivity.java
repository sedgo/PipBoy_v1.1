package com.example.sedgw.pipboy_v11;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //FullScreen options
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        //path for video
        //String videoSource ="http://www.sample-videos.com/video/mp4/240/big_buck_bunny_240p_1mb.mp4";

        //VideoView videoView = (VideoView) findViewById(R.id.videoView);
        //videoView.setVideoURI(Uri.parse(videoSource));

        //videoView.setMediaController(new MediaController(this));
        //videoView.requestFocus(0);
        //videoView.start();
        //Intent intent = new Intent(MainActivity.this, OSMMapsActivity.class);
        //startActivity(intent);
    }

    public void onClickFileDialog(View view) {
        Intent intent = new Intent()
                .setType("*/*")
                .setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Select a file"), 123);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == RESULT_OK) {
            Uri selectedFile = data.getData();
            WebView image = (WebView) findViewById(R.id.imageView);
            image.getSettings().setLoadWithOverviewMode(true);
            image.getSettings().setUseWideViewPort(true);
            image.loadUrl(Uri.decode(selectedFile.toString()));
            image.getSettings().setSupportZoom(true);
            image.getSettings().setBuiltInZoomControls(true);

            image.setScrollbarFadingEnabled(true);
            image.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);


            TextView textView = (TextView) findViewById(R.id.filePath);
            textView.setText(selectedFile.toString());

        }

    }
}
