package com.example.sedgw.pipboy_v11.admin;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.sedgw.pipboy_v11.R;

/**
 * Created by sedgw on 03.02.2017.
 */

public class AdminSettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_admin_settings);
    }

    public void onClickBack(View view) {
        finish();
    }
}
