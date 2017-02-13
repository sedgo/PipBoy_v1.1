package com.sedg.pipboy_v11;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.sedgw.pipboy_v11.R;
import com.sedg.pipboy_v11.timer.TimerService;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    public SharedPreferences timerSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.activity_main);

        init();

        //time
        Timer curTime = new Timer();
        final Handler handler = new Handler();
        final TextView textviewTime = (TextView) findViewById(R.id.time_text);
        curTime.schedule(new TimerTask() {
            @Override
            public void run() {
                final String curTime = android.text.format.DateFormat.format("HH:mm", new Date()).toString();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        textviewTime.setText(curTime);
                    }
                });
            }
        }, 60 - Integer.parseInt(android.text.format.DateFormat.format("ss", new Date()).toString()) , 60L * 1000);

        //timer
        timerSettings = getSharedPreferences("timer_settings", Context.MODE_PRIVATE);
        Timer curTimer = new Timer();
        final Handler handlerTimer = new Handler();
        final TextView textviewTimer = (TextView) findViewById(R.id.timer_text);
        if (timerSettings.getLong("current", 3600000L) == 0) textviewTimer.setText("00:00:00");
        else curTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                final String curTimer = android.text.format.DateFormat.format("HH:mm:ss",
                        new Date(timerSettings.getLong("current", timerSettings.getLong("start", 3600000L))) ).toString();
                handlerTimer.post(new Runnable() {
                    @Override
                    public void run() {
                        textviewTimer.setText(curTimer);
                    }
                });
            }
        }, 0 , 1000);
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

        //Timer
        SharedPreferences timerSettings = getSharedPreferences("timer_settings", Context.MODE_PRIVATE);
        if (!timerSettings.contains("start")) {
            SharedPreferences.Editor editorTimer = timerSettings.edit();
            editorTimer.putLong("start", 3600000L);
            editorTimer.apply();
        }
        if (!timerSettings.contains("current")) {
            SharedPreferences.Editor editorTimer = timerSettings.edit();
            editorTimer.putLong("current", 3600000L);
            editorTimer.apply();
        }
    }

    public void onClickStart(View view) {
        startService(new Intent(this, TimerService.class));
    }
}
