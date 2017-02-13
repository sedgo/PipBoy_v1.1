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


    }

    public void init() {
        //off flag of radio
        SharedPreferences radioSettings = getSharedPreferences("radio_settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editorRadio = radioSettings.edit();
        editorRadio.putString("status", "stopped");
        editorRadio.apply();

        //Phone of admin default setting
        SharedPreferences allSettings = getSharedPreferences("all_settings", Context.MODE_PRIVATE);
        if (!allSettings.contains("admin_number")) {
            SharedPreferences.Editor editorAll = allSettings.edit();
            editorAll.putString("admin_number", "89143644279");
            editorAll.apply();
        }
        if (!allSettings.contains("length_of_code")) {
            SharedPreferences.Editor editorAll = allSettings.edit();
            editorAll.putInt("length_of_code", 8);
            editorAll.apply();
        }
    }
}
