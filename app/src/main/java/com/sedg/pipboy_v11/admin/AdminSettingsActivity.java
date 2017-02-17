package com.sedg.pipboy_v11.admin;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.sedgw.pipboy_v11.R;

/**
 * Created by sedgw on 03.02.2017.
 */

public class AdminSettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.activity_admin_settings);


        changeSrc();
    }

    public void changeSrc() {
        SharedPreferences timer_settings = getSharedPreferences("timer_settings", Context.MODE_PRIVATE);
        ImageButton button_sms = (ImageButton) findViewById(R.id.button_timer_sms_change);
        if (timer_settings.getBoolean("send_sms", false)) {
            button_sms.setImageResource(R.drawable.d_add);
        }
        else {
            button_sms.setImageResource(R.drawable.d_del);
        }
    }

    public void onClickBack(View view) {
        finish();
    }

    public void onClickPhoneChange(View view) {
        EditText editText = (EditText) findViewById(R.id.admin_number);
        String str_number = editText.getText().toString();
        if (isPhoneNumberValid(str_number)) {
            SharedPreferences allSettings = getSharedPreferences("all_settings", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = allSettings.edit();
            editor.putString("admin_number", str_number);
            editor.apply();
            Toast.makeText(getApplicationContext(), R.string.admin_number_change, Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(), R.string.admin_number_bad, Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isPhoneNumberValid(String phoneNumber) {
        String regex = "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$";

        if (!phoneNumber.matches(regex)) {
            return false;
        }
        return true;
    }

    public void onClickTimerSmsChange(View view) {
        SharedPreferences timer_settings = getSharedPreferences("timer_settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = timer_settings.edit();
        editor.putBoolean("send_sms", !timer_settings.getBoolean("send_sms", false));
        editor.apply();
        changeSrc();
    }

    public void onClickTimerChange(View view) {
        EditText editText = (EditText) findViewById(R.id.start_timer);
        String str_time = editText.getText().toString();
        try  {
            Long time = Long.parseLong(str_time);
            SharedPreferences timer_settings = getSharedPreferences("timer_settings", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = timer_settings.edit();
            editor.putLong("start", time * 1000);
            editor.apply();
            Toast.makeText(getApplicationContext(), R.string.admin_start_timer_change, Toast.LENGTH_SHORT).show();
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(), R.string.admin_start_timer_bad, Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickCodeChange(View view) {
        EditText editText = (EditText) findViewById(R.id.start_code);
        SharedPreferences allSettings = getSharedPreferences("all_settings", Context.MODE_PRIVATE);
        if (editText.length() != allSettings.getInt("length_of_code", 8)) {
            Toast.makeText(this, R.string.message_not_enter_code, Toast.LENGTH_SHORT).show();
        }
        else {
            SharedPreferences timer_settings = getSharedPreferences("timer_settings", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = timer_settings.edit();
            editor.putString("start_code", editText.getText().toString());
            editor.apply();
            Toast.makeText(getApplicationContext(), R.string.admin_start_code_change, Toast.LENGTH_SHORT).show();
        }
    }
}
