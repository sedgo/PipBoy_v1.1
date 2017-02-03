package com.example.sedgw.pipboy_v11.admin;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.sedgw.pipboy_v11.R;

/**
 * Created by sedgw on 03.02.2017.
 */

public class AdminListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_admin_list);
    }


    public void onClickBack(View view) {
        finish();
    }

    public void onClickImage(View view) {
    }

    public void onClickAudio(View view) {
    }

    public void onClickVideo(View view) {
    }

    public void onClickCoor(View view) {
    }

    public void onClickTitle(View view) {
    }
}
