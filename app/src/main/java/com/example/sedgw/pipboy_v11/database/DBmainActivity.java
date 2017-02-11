package com.example.sedgw.pipboy_v11.database;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.WindowManager;

import com.example.sedgw.pipboy_v11.R;

/**
 * Created by nechuhaev on 26.01.2017.
 */

public class DBmainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.activity_db_main);
    }
}
