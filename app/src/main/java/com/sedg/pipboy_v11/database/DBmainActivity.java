package com.sedg.pipboy_v11.database;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.sedgw.pipboy_v11.R;
import com.sedg.pipboy_v11.data.MainContract.ObjectEntry;
import com.sedg.pipboy_v11.data.ObjectBDHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by nechuhaev on 26.01.2017.
 */

public class DBmainActivity extends Activity {

    public ObjectBDHelper dbHelper;
    public ListView listViewObjects, listViewMedia;
    public ArrayList<HashMap<String, Object>> objectsArrayList, mediaArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.activity_db_main);

        dbHelper = new ObjectBDHelper(this);
        listViewObjects = (ListView) findViewById(R.id.list_object);
        listViewMedia = (ListView) findViewById(R.id.list_media);
        objectsArrayList = new ArrayList<HashMap<String, Object>>();
        mediaArrayList = new ArrayList<HashMap<String, Object>>();
        updateMediaView();
        updateObjectView();
    }

    public void onClickBack(View view) {
        finish();
    }

    public ArrayList<HashMap<String, Object>> selectOpenedObjects() {
        ArrayList<HashMap<String, Object>> curArrayList = new ArrayList<HashMap<String, Object>>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //Array of column and values
        String[] select_column = { ObjectEntry.COLUMN_NAME, ObjectEntry.COLUMN_CODE };
        String where_exp = ObjectEntry.COLUMN_OPENED + " = 1 and " + ObjectEntry.COLUMN_TYPE + " = 'object'";
        //Execute query
        Cursor cursor = db.query(
                ObjectEntry.TABLE_NAME,
                select_column,
                where_exp,
                null,
                null,
                null,
                null
        );

        HashMap<String, Object> hm;
        if (cursor != null) {
            while (cursor.moveToNext()) {
                hm = new HashMap<>();
                hm.put("CODE", cursor.getString(cursor.getColumnIndex(ObjectEntry.COLUMN_CODE)) );
                hm.put("NAME", cursor.getString(cursor.getColumnIndex(ObjectEntry.COLUMN_NAME)) );
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

    public void updateObjectView() {
        objectsArrayList = selectOpenedObjects();
        SimpleAdapter adapter = new SimpleAdapter(this, objectsArrayList,
                R.layout.list_item_object_player, new String[]{ "CODE", "NAME" },
                new int[]{ R.id.code_text, R.id.name_text });

        if (adapter.isEmpty()) {
            listViewObjects.setAdapter(null);
        }
        else listViewObjects.setAdapter(adapter);
        listViewObjects.setOnItemClickListener(itemClickListenerObject);
    }

    AdapterView.OnItemClickListener itemClickListenerObject = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            HashMap<String, Object> itemHashMap = (HashMap<String, Object>) parent.getItemAtPosition(position);
            Intent intent = new Intent(getApplicationContext(), DBviewObjectActivity.class);
            intent.putExtra("code", itemHashMap.get("CODE").toString());
            intent.putExtra("admin", true);
            startActivity(intent);
        }
    };

    public ArrayList<HashMap<String, Object>> selectOpenedMedia() {
        ArrayList<HashMap<String, Object>> curArrayList = new ArrayList<HashMap<String, Object>>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //Array of column and values
        String[] select_column = { ObjectEntry.COLUMN_NAME, ObjectEntry.COLUMN_CODE };
        String where_exp = ObjectEntry.COLUMN_OPENED + " = 1 and " + ObjectEntry.COLUMN_TYPE + " = 'media'";
        //Execute query
        Cursor cursor = db.query(
                ObjectEntry.TABLE_NAME,
                select_column,
                where_exp,
                null,
                null,
                null,
                null
        );

        HashMap<String, Object> hm;
        if (cursor != null) {
            while (cursor.moveToNext()) {
                hm = new HashMap<>();
                hm.put("CODE", cursor.getString(cursor.getColumnIndex(ObjectEntry.COLUMN_CODE)) );
                hm.put("NAME", cursor.getString(cursor.getColumnIndex(ObjectEntry.COLUMN_NAME)) );
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

    public void updateMediaView() {
        mediaArrayList = selectOpenedMedia();
        SimpleAdapter adapter = new SimpleAdapter(this, mediaArrayList,
                R.layout.list_item_object_player, new String[]{ "CODE", "NAME" },
                new int[]{ R.id.code_text, R.id.name_text });

        if (adapter.isEmpty()) {
            listViewMedia.setAdapter(null);
        }
        else listViewMedia.setAdapter(adapter);
        listViewMedia.setOnItemClickListener(itemClickListenerMedia);
    }

    AdapterView.OnItemClickListener itemClickListenerMedia = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            HashMap<String, Object> itemHashMap = (HashMap<String, Object>) parent.getItemAtPosition(position);
            Intent intent = new Intent(getApplicationContext(), DBviewMediaActivity.class);
            intent.putExtra("code", itemHashMap.get("CODE").toString());
            intent.putExtra("admin", true);
            startActivity(intent);
        }
    };
}
