package com.sedg.pipboy_v11.timer;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.example.sedgw.pipboy_v11.R;

/**
 * Created by sedgw on 13.02.2017.
 */

public class TimerService extends Service {

    public SharedPreferences timerSettings;
    public CountDownTimer timer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        timerSettings = getSharedPreferences("timer_settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editorStartTime = timerSettings.edit();
        editorStartTime.putLong("current", timerSettings.getLong("start", 3600000L));
        editorStartTime.apply();
        timer = new CountDownTimer(20000000, 1000) {

            public void onTick(long millisUntilFinished) {
                long current_value = timerSettings.getLong("current", 3600000L) - 1000L;
                if (current_value <= 0) {
                    SharedPreferences.Editor editorTimer = timerSettings.edit();
                    editorTimer.putLong("current", 0);
                    editorTimer.apply();
                    //send message
                    try {
                        SharedPreferences timesSettings = getSharedPreferences("times_settings", Context.MODE_PRIVATE);
                        if (timesSettings.getBoolean("send_sms", false)) {
                            SharedPreferences allSettings = getSharedPreferences("all_settings", Context.MODE_PRIVATE);
                            SmsManager smsManager = SmsManager.getDefault();
                            PendingIntent sentPI = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent("SMS_SENT"), 0);
                            smsManager.sendTextMessage(allSettings.getString("admin_number", "89143644279"), null,
                                    getString(R.string.timer_end_message_text), sentPI, null);
                        }
                        Toast.makeText(getApplicationContext(), R.string.timer_end_message_text, Toast.LENGTH_LONG).show();
                        SharedPreferences.Editor editor = timerSettings.edit();
                        editor.putBoolean("started", false);
                        editor.apply();
                        stopSelf();
                    }
                    catch (Exception e ) {
                        Toast.makeText(getApplicationContext(), R.string.timer_end_not_sms, Toast.LENGTH_LONG).show();
                    }
                    this.cancel();
                    stopSelf();
                }
                SharedPreferences.Editor editorTimer = timerSettings.edit();
                editorTimer.putLong("current", current_value);
                editorTimer.apply();
            }

            public void onFinish() {
                this.start();
            }
        };
        timer.start();
    }

    @Override
    public void onDestroy() {
        SharedPreferences timesSettings = getSharedPreferences("times_settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = timerSettings.edit();
        editor.putBoolean("started", false);
        editor.apply();
        super.onDestroy();
    }
}
