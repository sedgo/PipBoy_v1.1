package com.example.sedgw.pipboy_v11;

import android.os.Bundle;
import android.app.Activity;
import android.view.WindowManager;

/**
 * Created by nechuhaev on 26.01.2017.
 */

public class DBmainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_db_main);
    }
}
