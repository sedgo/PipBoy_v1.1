package com.example.sedgw.pipboy_v11.sms;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.sedgw.pipboy_v11.R;

/**
 * Created by sedgw on 11.02.2017.
 */

public class ServiceDialogNewMessage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String sms_body = intent.getExtras().getString("sms_body");
        String sms_number = intent.getExtras().getString("phone_number");

        //getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        AlertDialog.Builder alert = new AlertDialog.Builder(ServiceDialogNewMessage.this);
        alert.setTitle(getString(R.string.new_message_title));
        alert.setMessage(sms_number + "\r\n" + sms_body);
        alert.setPositiveButton(getString(R.string.new_message_positive), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                Intent intent = new Intent(getApplicationContext(), SmsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        alert.setNegativeButton(getString(R.string.new_message_negative), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.cancel();
                finish();
            }
        });
        alert.setCancelable(true);
        alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
            }
        });
        alert.show();
    }
}
