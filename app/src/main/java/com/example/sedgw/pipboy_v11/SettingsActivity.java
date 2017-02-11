package com.example.sedgw.pipboy_v11;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.sedgw.pipboy_v11.admin.AdminMainActivity;

/**
 * Created by sedgw on 01.02.2017.
 */

public class SettingsActivity extends Activity {
    public Integer counterClickAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.activity_settings);
        counterClickAdmin = 0;
    }

    public void onClick(View view) {
        counterClickAdmin++;
        if (counterClickAdmin >= getResources().getInteger(R.integer.need_to_admin)) {
            Intent intent = new Intent(this, AdminMainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        counterClickAdmin = 0;
    }

    public void onClickBack(View view) {
        finish();
    }

    public void onClickNavigation(View view) {
        Intent intent = new Intent(this, ActivityCheckNavigation.class);
        startActivity(intent);
    }
}
