package com.example.sedgw.pipboy_v11;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.activity_main);

        init();

        //time
        Timer curTimer = new Timer();
        final Handler handler = new Handler();
        final TextView textviewTime = (TextView) findViewById(R.id.time_text);
        curTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                final String curTime = android.text.format.DateFormat.format("hh:mm", new Date()).toString();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        textviewTime.setText(curTime);
                    }
                });
            }
        }, 60 - Integer.parseInt(android.text.format.DateFormat.format("ss", new Date()).toString()) , 60L * 1000);
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
