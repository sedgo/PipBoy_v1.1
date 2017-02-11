package com.example.sedgw.pipboy_v11.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.sedgw.pipboy_v11.R;

/**
 * Created by sedgw on 01.02.2017.
 */

public class AdminMainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.activity_admin_main);
    }

    public void onClickBack(View view) {
        finish();
    }

    public void onClickDB(View view) {
        Intent intent = new Intent(this, AdminListActivity.class);
        startActivity(intent);
    }

    public void onClickSettings(View view) {
        Intent intent = new Intent(this, AdminSettingsActivity.class);
        startActivity(intent);
    }

    public void onClickContacts(View view) {
        Intent intent = new Intent(this, AdminContactActivity.class);
        startActivity(intent);
    }
}
