package com.example.sedgw.pipboy_v11.sms;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * Created by sedgw on 11.02.2017.
 */

public class SmsSendActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onClickBack(View view) {
        finish();
    }
}
