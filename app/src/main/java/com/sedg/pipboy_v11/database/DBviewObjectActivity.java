package com.sedg.pipboy_v11.database;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.sedg.pipboy_v11.OSMMapsActivity;
import com.example.sedgw.pipboy_v11.R;
import com.sedg.pipboy_v11.data.MainContract.ObjectEntry;
import com.sedg.pipboy_v11.data.ObjectBDHelper;
import com.sedg.pipboy_v11.radio.RadioService;

/**
 * Created by nechuhaev on 26.01.2017.
 */

public class DBviewObjectActivity extends Activity {

    public MediaPlayer audioPlayer;
    public Double lat = 0d;
    public Double lon = 0d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.activity_db_view_object);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //For a good, release a audioPlayer, but give nullPointerException
        // if (audioPlayer.equals(null)) audioPlayer.release();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //stop radio playing
        stopService(new Intent(this, RadioService.class));
        SharedPreferences radioSettings = getSharedPreferences("radio_settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = radioSettings.edit();
        editor.putString("status", "stopped");
        editor.apply();

        String cur_code = getIntent().getExtras().getString("code");
        TextView text_code = (TextView) findViewById(R.id.code_text);
        text_code.setText(cur_code);
        ImageButton button_audio = (ImageButton) findViewById(R.id.button_audio);
        button_audio.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                try {
                    audioPlayer.stop();
                    audioPlayer.prepare();
                }
                catch (Exception e) {
                    Toast.makeText(getApplicationContext(), R.string.error_audio_play, Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });



        //open DB
        ObjectBDHelper dbHelper = new ObjectBDHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        //Array of column and values
        String[] select_column = { ObjectEntry.COLUMN_NAME, ObjectEntry.COLUMN_TYPE, ObjectEntry.COLUMN_TITLE,
                ObjectEntry.COLUMN_LAT, ObjectEntry.COLUMN_LON, ObjectEntry.COLUMN_PATH_TO_IMAGE, ObjectEntry.COLUMN_PATH_TO_SOUND,
                ObjectEntry.COLUMN_PATH_TO_VIDEO };
        String where_expression = ObjectEntry.COLUMN_CODE + " = ?";
        String[] where_args = { cur_code };

        //Execute query
        Cursor cursor = db.query(
                ObjectEntry.TABLE_NAME,
                select_column,
                where_expression,
                where_args,
                null,
                null,
                null
        );

        if (cursor != null) {
            if (cursor.moveToNext()) {
                TextView text_name = (TextView) findViewById(R.id.name_text);
                TextView text_title = (TextView) findViewById(R.id.title_text);
                text_name.setText(cursor.getString(cursor.getColumnIndex(ObjectEntry.COLUMN_NAME)));
                text_title.setText(cursor.getString(cursor.getColumnIndex(ObjectEntry.COLUMN_TITLE)));
                VideoView videoView = (VideoView) findViewById(R.id.video_view);
                audioPlayer = new MediaPlayer();//.create(this, Uri.parse( cursor.getString(cursor.getColumnIndex(ObjectEntry.COLUMN_PATH_TO_SOUND)) ));
                ImageView imageView = (ImageView) findViewById(R.id.image_view);
                imageView.setImageURI(Uri.parse(cursor.getString(cursor.getColumnIndex(ObjectEntry.COLUMN_PATH_TO_IMAGE))));
                videoView.setVideoURI(Uri.parse( cursor.getString(cursor.getColumnIndex(ObjectEntry.COLUMN_PATH_TO_VIDEO)) ));
                lon = cursor.getDouble(cursor.getColumnIndex(ObjectEntry.COLUMN_LON));
                lat = cursor.getDouble(cursor.getColumnIndex(ObjectEntry.COLUMN_LAT));
                try {
                    audioPlayer.setDataSource(this, Uri.parse( cursor.getString(cursor.getColumnIndex(ObjectEntry.COLUMN_PATH_TO_SOUND)) ));
                    audioPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    audioPlayer.prepare();
                }
                catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, R.string.error_audio_play, Toast.LENGTH_LONG).show();
                }
            }
            else {
                Toast.makeText(this, R.string.message_code_not_find, Toast.LENGTH_LONG).show();
            }
            cursor.close();
        }
        else {
            Toast.makeText(this, R.string.message_error_select, Toast.LENGTH_LONG).show();
        }
        db.close();

        //save to opened list
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ObjectEntry.COLUMN_OPENED, true);
        db.update(ObjectEntry.TABLE_NAME,
                values,
                ObjectEntry.COLUMN_CODE + " = ?",
                new String[]{ cur_code });
        db.close();

        dbHelper.close();
    }

    public void onClickBack(View view) {
        finish();
    }

    public void onClickBackVideo(View view) {
        VideoView video = (VideoView) findViewById(R.id.video_view);
        video.stopPlayback();
        video.setVisibility(View.INVISIBLE);
        ImageButton buttonBack = (ImageButton) findViewById(R.id.button_back_video);
        buttonBack.setVisibility(View.INVISIBLE);
        LinearLayout infPanel = (LinearLayout) findViewById(R.id.infPanel);
        infPanel.setVisibility(View.VISIBLE);
    }

    public void onClickVideo(View view) {
        VideoView video = (VideoView) findViewById(R.id.video_view);
        video.setVisibility(View.VISIBLE);
        ImageButton buttonBack = (ImageButton) findViewById(R.id.button_back_video);
        buttonBack.setVisibility(View.VISIBLE);
        LinearLayout infPanel = (LinearLayout) findViewById(R.id.infPanel);
        infPanel.setVisibility(View.INVISIBLE);
        VideoView videoView = (VideoView) findViewById(R.id.video_view);
        videoView.setMediaController(new MediaController(this));
        videoView.requestFocus(0);
        videoView.start();
    }

    public void onClickAudio(View view) {
        try {
            if (audioPlayer.isPlaying()) {
                audioPlayer.pause();
            }
            else {
                audioPlayer.start();
            }
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(), R.string.error_audio_play, Toast.LENGTH_LONG).show();
        }

    }

    public void onClickMap(View view) {
        Intent intent = new Intent(this, OSMMapsActivity.class);
        intent.putExtra("lat", lat);
        intent.putExtra("lon", lon);
        TextView tv = (TextView) findViewById(R.id.name_text);
        intent.putExtra("name", tv.getText().toString());
        startActivity(intent);
    }

}
