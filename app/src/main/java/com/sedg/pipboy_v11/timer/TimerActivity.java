package com.sedg.pipboy_v11.timer;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.sedgw.pipboy_v11.R;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by sedgw on 13.02.2017.
 */

public class TimerActivity extends Activity {

    public SharedPreferences timerSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_timer);

        timerSettings = getSharedPreferences("timer_settings", Context.MODE_PRIVATE);

        final TextView textviewTimer = (TextView) findViewById(R.id.timer_text);
        if (timerSettings.getLong("current", 3600000L) <= 0) {
            textviewTimer.setText("00:00:00");
            return;
        }
        Timer curTimer = new Timer();
        final Handler handler = new Handler();
        curTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                final String curTime = android.text.format.DateFormat.format("HH:mm:ss",
                        new Date(timerSettings.getLong("current", timerSettings.getLong("start", 3600000L))) ).toString();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        textviewTimer.setText(curTime);
                    }
                });
            }
        }, 0 , 1000);
    }

    public void onClickBack(View view) {
        finish();
    }
}
