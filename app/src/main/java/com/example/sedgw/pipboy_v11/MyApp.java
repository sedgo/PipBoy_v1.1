package com.example.sedgw.pipboy_v11;

import android.support.multidex.MultiDexApplication;

/**
 * Created by nechuhaev on 08.02.2017.
 */

public class MyApp extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts");
    }
}
