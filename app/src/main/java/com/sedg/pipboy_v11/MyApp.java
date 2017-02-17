package com.sedg.pipboy_v11;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.multidex.MultiDexApplication;
import android.widget.Toast;

import com.sedg.pipboy_v11.timer.TimerService;

/**
 * Created by nechuhaev on 08.02.2017.
 */

public class MyApp extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/8267.ttf");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
