package com.sedg.pipboy_v11.radio;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.sedgw.pipboy_v11.R;
import com.sedg.pipboy_v11.data.MainContract.RadioEntry;
import com.sedg.pipboy_v11.data.ObjectBDHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sedgw on 12.02.2017.
 */

public class RadioActivity extends Activity {

    public ObjectBDHelper dbHelper;
    public ListView listView;
    public ArrayList<HashMap<String, Object>> objectsArrayList;

    public static final String NAME = "name";
    public static final String URL = "url";

    ImageButton play;
    public String stream = "";
    public View curView;


    public static final String APP_PREFERENCES = "radio_settings";
    private SharedPreferences radioSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_radio);

        play = (ImageButton) findViewById(R.id.button_play);
        radioSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if (!radioSettings.contains("status")) {
            SharedPreferences.Editor editor = radioSettings.edit();
            editor.putString("status", "stopped");
            editor.apply();
        }
        if (radioSettings.getString("status", "stopped").equals("started")) {
            play.setImageResource(R.drawable.music);
        }


        dbHelper = new ObjectBDHelper(this);
        listView = (ListView) findViewById(R.id.list_view);
        objectsArrayList = new ArrayList<HashMap<String, Object>>();
        updateListView();
    }

    public void onClickBack(View view) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }

    public void onClickPlay(View view) {
        if (radioSettings.getString("status", "stopped").equals("stopped")) {
            if (stream == "") {
                Toast.makeText(this, R.string.select_stations, Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(this, RadioService.class);
            intent.putExtra("stream", stream);
            startService(intent);
            play.setImageResource(R.drawable.music);
            SharedPreferences.Editor editor = radioSettings.edit();
            editor.putString("status", "started");
            editor.apply();
            if (curView != null) curView.setBackgroundResource(R.drawable.selected_item);
        }
        else {
            stopService(new Intent(this, RadioService.class));
            play.setImageResource(R.drawable.poweramp);
            SharedPreferences.Editor editor = radioSettings.edit();
            editor.putString("status", "stopped");
            editor.apply();
            if (curView != null) curView.setBackgroundResource(R.drawable.appfunc_rename);
        }
    }

    public ArrayList<HashMap<String, Object>> selectAllRadio() {
        ArrayList<HashMap<String, Object>> curArrayList = new ArrayList<HashMap<String, Object>>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //Array of column and values
        String[] select_column = { RadioEntry.COLUMN_NAME, RadioEntry.COLUMN_URL };
        //Execute query
        Cursor cursor = db.query(
                RadioEntry.TABLE_NAME,
                select_column,
                null,
                null,
                null,
                null,
                null
        );

        HashMap<String, Object> hm;
        if (cursor != null) {
            while (cursor.moveToNext()) {
                hm = new HashMap<>();
                hm.put(NAME, cursor.getString(cursor.getColumnIndex(RadioEntry.COLUMN_NAME)) );
                hm.put(URL, cursor.getString(cursor.getColumnIndex(RadioEntry.COLUMN_URL)));
                curArrayList.add(hm);
            }
            cursor.close();
        }
        else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.message_error_select), Toast.LENGTH_LONG).show();
        }
        db.close();
        return curArrayList;
    }

    public void updateListView() {
        objectsArrayList = selectAllRadio();
        SimpleAdapter adapter = new SimpleAdapter(this, objectsArrayList,
                R.layout.list_item_radio, new String[]{NAME, URL},
                new int[]{R.id.name_text, R.id.url_text});

        if (adapter.isEmpty()) {
            //Toast.makeText(this, R.string.empty_array_list, Toast.LENGTH_SHORT).show();
            listView.setAdapter(null);
        }
        else listView.setAdapter(adapter);
        listView.setOnItemClickListener(itemClickListener);
    }

    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            HashMap<String, Object> itemHashMap = (HashMap<String, Object>) parent.getItemAtPosition(position);
            if (curView != null) curView.setBackgroundResource(R.drawable.appfunc_rename);
            if (curView == view) {
                onClickPlay(curView);
                view.setBackgroundResource(R.drawable.appfunc_rename);
                curView = null;
                stream = "";
            } else {
                curView = view;
                view.setBackgroundResource(R.drawable.selected_item);
                stream = itemHashMap.get(URL).toString();
                onClickPlay(curView);
            }
        }
    };

    public void onClickSave(View view) {
        EditText edit_name = (EditText) findViewById(R.id.edit_name);
        EditText edit_url = (EditText) findViewById(R.id.edit_url);
        String name = edit_name.getText().toString();
        String url = edit_url.getText().toString();

        if (name.length() == 0) {
            Toast.makeText(this, R.string.required_name, Toast.LENGTH_SHORT).show();
            return;
        }
        if (url.length() == 0) {
            Toast.makeText(this, R.string.required_url, Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RadioEntry.COLUMN_NAME, name);
        values.put(RadioEntry.COLUMN_URL, url);
        long newRowId = db.insert(RadioEntry.TABLE_NAME, null, values);
        if (newRowId == -1) {
            Toast.makeText(this, R.string.message_error_on_insert, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.message_success_insert, Toast.LENGTH_SHORT).show();
            updateListView();
        }
        db.close();
    }

    public void onClickDelete(View view) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(RadioEntry.TABLE_NAME,
                RadioEntry.COLUMN_URL + " = ?",
                new String[] { stream });
        updateListView();
        db.close();
    }
}
