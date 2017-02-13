package com.sedg.pipboy_v11.database;

import android.app.Activity;
import android.app.PendingIntent;
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
import android.telephony.SmsManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.sedgw.pipboy_v11.R;
import com.sedg.pipboy_v11.OSMMapsActivity;
import com.sedg.pipboy_v11.data.MainContract.ObjectEntry;
import com.sedg.pipboy_v11.data.ObjectBDHelper;

/**
 * Created by nechuhaev on 26.01.2017.
 */

public class DBviewMessageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.activity_db_view_message);
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
        String cur_code = getIntent().getExtras().getString("code");
        TextView text_code = (TextView) findViewById(R.id.code_text);
        text_code.setText(cur_code);

        //open DB
        ObjectBDHelper dbHelper = new ObjectBDHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        //Array of column and values
        String[] select_column = { ObjectEntry.COLUMN_NAME, ObjectEntry.COLUMN_TITLE, ObjectEntry.COLUMN_MESSAGE };
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
                try {
                    SharedPreferences allSettings = getSharedPreferences("all_settings", Context.MODE_PRIVATE);
                    SmsManager smsManager = SmsManager.getDefault();
                    PendingIntent sentPI = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent("SMS_SENT"), 0);
                    smsManager.sendTextMessage(allSettings.getString("admin_number", "89143644279"), null,
                            cursor.getString(cursor.getColumnIndex(ObjectEntry.COLUMN_MESSAGE)), sentPI, null);
                }
                catch (Exception e ) { }
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

}
