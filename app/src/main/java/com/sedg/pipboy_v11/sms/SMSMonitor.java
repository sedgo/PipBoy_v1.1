package com.sedg.pipboy_v11.sms;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.telephony.SmsMessage;
import android.text.format.DateFormat;
import android.widget.Toast;

import com.example.sedgw.pipboy_v11.R;
import com.sedg.pipboy_v11.data.MainContract;
import com.sedg.pipboy_v11.data.ObjectBDHelper;

import java.util.Date;

/**
 * Created by sedgw on 11.02.2017.
 */

public class SMSMonitor extends BroadcastReceiver {

    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null &&
                ACTION.compareToIgnoreCase(intent.getAction()) == 0) {
            Object[] pduArray = (Object[]) intent.getExtras().get("pdus");
            SmsMessage[] messages = new SmsMessage[pduArray.length];
            for (int i = 0; i < pduArray.length; i++) {
                messages[i] = SmsMessage.createFromPdu((byte[]) pduArray[i]);
            }
            String sms_from = messages[0].getDisplayOriginatingAddress();
            long timestamp = messages[0].getTimestampMillis();
            String sms_timestamp = DateFormat.format("h:mm:ss dd MMMM yyyy ", new Date(timestamp)).toString();
            StringBuilder bodyText = new StringBuilder();
            for (int i = 0; i < messages.length; i++) {
                bodyText.append(messages[i].getMessageBody());
            }
            String body = bodyText.toString();

            //open DB
            ObjectBDHelper dbHelper = new ObjectBDHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(MainContract.SmsEntry.COLUMN_FLAG_INPUT, 1);
            values.put(MainContract.SmsEntry.COLUMN_NUMBER, sms_from);
            values.put(MainContract.SmsEntry.COLUMN_MESSAGE, body);
            values.put(MainContract.SmsEntry.COLUMN_TIMESTAMP, sms_timestamp);
            long newRowId = 0;
            newRowId = db.insert(MainContract.SmsEntry.TABLE_NAME, null, values);
            if (newRowId == -1) {
                Toast.makeText(context, context.getString(R.string.new_message_error_on_insert), Toast.LENGTH_LONG).show();
            }

            db.close();
            dbHelper.close();

            Intent mIntent = new Intent(context, ServiceDialogNewMessage.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mIntent.putExtra("phone_number", sms_from);
            mIntent.putExtra("sms_body", body);
            context.startActivity(mIntent);
            //Toast.makeText(context, "senderNum: "+ sms_from + ", message: " + body, Toast.LENGTH_LONG).show();
            abortBroadcast();
        }
    }
}
