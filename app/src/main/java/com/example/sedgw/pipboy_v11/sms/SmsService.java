package com.example.sedgw.pipboy_v11.sms;

import android.app.AlertDialog;
import android.app.Service;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.sedgw.pipboy_v11.R;
import com.example.sedgw.pipboy_v11.data.MainContract;
import com.example.sedgw.pipboy_v11.data.MainContract.SmsEntry;
import com.example.sedgw.pipboy_v11.data.ObjectBDHelper;

import static android.R.id.message;

/**
 * Created by sedgw on 11.02.2017.
 */

public class SmsService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String sms_body = intent.getExtras().getString("sms_body");
        String sms_number = intent.getExtras().getString("phone_number");
        String sms_timestamp = intent.getExtras().getString("sms_timestamp");

        //open DB
        ObjectBDHelper dbHelper = new ObjectBDHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SmsEntry.COLUMN_FLAG_INPUT, true);
        values.put(SmsEntry.COLUMN_NUMBER, sms_number);
        values.put(SmsEntry.COLUMN_MESSAGE, sms_body);
        values.put(SmsEntry.COLUMN_TIMESTAMP, sms_timestamp);
        long newRowId = 0;
        newRowId = db.insert(MainContract.ObjectEntry.TABLE_NAME, null, values);
        if (newRowId == -1) {
            Toast.makeText(this, R.string.new_message_error_on_insert, Toast.LENGTH_LONG).show();
        }

        db.close();
        dbHelper.close();

        alert_msg(sms_body, sms_number);
        return START_STICKY;
        //return super.onStartCommand(intent, flags, startId);
    }

    private void alert_msg(final String sms_body, final String phone_number) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getApplicationContext());
        alert.setTitle(getString(R.string.new_message_title));
        alert.setMessage(phone_number + "\r\n" + sms_body);
        alert.setPositiveButton(getString(R.string.new_message_positive), new OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                Intent intent = new Intent(getApplicationContext(), SmsActivity.class);
                startActivity(intent);
            }
        });
        alert.setNegativeButton(getString(R.string.new_message_negative), new OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.cancel();
            }
        });
        alert.setCancelable(true);
        alert.setOnCancelListener(new OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
            }
        });
        alert.show();
    }
}
