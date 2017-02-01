package com.example.sedgw.pipboy_v11;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

/**
 * Created by sedgw on 02.01.2017.
 */

public class AboutActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_about);
    }
}
