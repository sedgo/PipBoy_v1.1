package com.example.sedgw.pipboy_v11.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.text.format.DateFormat;

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
            String sms_timestamp = DateFormat.format("EEEE, MMMM dd, yyyy h:mm:ss", new Date(timestamp)).toString();
            StringBuilder bodyText = new StringBuilder();
            for (int i = 0; i < messages.length; i++) {
                bodyText.append(messages[i].getMessageBody());
            }
            String body = bodyText.toString();
            Intent mIntent = new Intent(context, SmsService.class);
            mIntent.putExtra("phone_number", sms_from);
            mIntent.putExtra("sms_body", body);
            mIntent.putExtra("sms_timestamp", sms_timestamp);
            context.startService(mIntent);

            abortBroadcast();
        }
    }
}
