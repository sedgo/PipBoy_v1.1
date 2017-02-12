package com.example.sedgw.pipboy_v11.radio;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.sedgw.pipboy_v11.R;

import java.io.IOException;

/**
 * Created by sedgw on 12.02.2017.
 */

public class RadioService extends Service {

    MediaPlayer mediaPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String stream = intent.getExtras().getString("stream");
        try {
            mediaPlayer.setDataSource(stream);
            mediaPlayer.prepare();
            mediaPlayer.start();
        }
        catch (IOException e) {
            Toast.makeText(getApplicationContext(), R.string.error_set_url, Toast.LENGTH_LONG).show();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
    }
}
