package com.example.sedgw.pipboy_v11;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
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

import com.google.android.gms.ads.formats.NativeAd;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.activity_main);

        //off flag of radio
        SharedPreferences radioSettings = getSharedPreferences("radio_settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = radioSettings.edit();
        editor.putString("status", "stopped");
        editor.apply();
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
            ImageView image = (ImageView) findViewById(R.id.imageView);
            //image.getSettings().setLoadWithOverviewMode(true);
            //image.getSettings().setUseWideViewPort(true);
            image.setImageURI(selectedFile);

            //image.getSettings().setBuiltInZoomControls(true); //Button of zoom
            /* suka ne vkuril!
            int picHeight = image.getContentHeight();
            int height = image.getHeight();
            float val = 1;
            if (picHeight > height)
                val = (float) height / (float) picHeight / 100f;

            //val = val * 100f;
            int val_int = (int) val;
            image.setInitialScale(val_int); */
            //image.setInitialScale(10);

            image.setScrollbarFadingEnabled(true);
            image.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);


            TextView textView = (TextView) findViewById(R.id.filePath);
            textView.setText(selectedFile.toString());

        }

    }
}
