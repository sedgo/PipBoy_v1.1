package com.example.sedgw.pipboy_v11.sms;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sedgw.pipboy_v11.R;
import com.example.sedgw.pipboy_v11.data.MainContract.SmsEntry;
import com.example.sedgw.pipboy_v11.data.ObjectBDHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sedgw on 11.02.2017.
 */

public class SmsActivity extends Activity {

    public ObjectBDHelper dbHelper;
    public ListView listView;
    public ArrayList<HashMap<String, Object>> objectsArrayList;

    public static final String NUMBER = "number";
    public static final String MESSAGE = "message";
    public static final String TIMESTAMP = "timestamp";

    public Boolean flagInput = true;

    public View curView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sms);

        dbHelper = new ObjectBDHelper(this);
        listView = (ListView) findViewById(R.id.list_items);
        objectsArrayList = new ArrayList<HashMap<String, Object>>();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showMessages(flagInput);
    }

    public ArrayList<HashMap<String, Object>> selectInputSms(Boolean flag) {
        ArrayList<HashMap<String, Object>> curArrayList = new ArrayList<HashMap<String, Object>>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //Array of column and values
        String[] select_column = { SmsEntry._ID,
                SmsEntry.COLUMN_NUMBER,
                SmsEntry.COLUMN_MESSAGE,
                SmsEntry.COLUMN_TIMESTAMP};
        String flagChange;
        if (flag) flagChange = " = 1";
        else flagChange = " = 0";
        String where_expression = SmsEntry.COLUMN_FLAG_INPUT + flagChange;
        //Execute query
        Cursor cursor = db.query(
                SmsEntry.TABLE_NAME,
                select_column,
                where_expression,
                null,
                null,
                null,
                SmsEntry._ID + " DESC"
        );

        HashMap<String, Object> hm;
        if (cursor != null) {
            while (cursor.moveToNext()) {
                hm = new HashMap<>();
                hm.put(NUMBER, cursor.getString(cursor.getColumnIndex(SmsEntry.COLUMN_NUMBER)) );
                hm.put(MESSAGE, cursor.getString(cursor.getColumnIndex(SmsEntry.COLUMN_MESSAGE)) );
                hm.put(TIMESTAMP, cursor.getString(cursor.getColumnIndex(SmsEntry.COLUMN_TIMESTAMP)) );
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

    public void showMessages(Boolean flag) {
        objectsArrayList = selectInputSms(flag);
        SimpleAdapter adapter = new SimpleAdapter(this, objectsArrayList,
                R.layout.list_item_sms, new String[]{NUMBER, MESSAGE, TIMESTAMP},
                new int[]{R.id.number_text, R.id.message_text, R.id.timestamp_text});

        if (adapter.isEmpty()) {
            Toast.makeText(this, R.string.empty_array_list, Toast.LENGTH_LONG).show();
            listView.setAdapter(null);
        }
        else listView.setAdapter(adapter);
        //if need to task on click items on listview
        //listView.setOnItemClickListener(itemClickListener);
    }

    /* AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            HashMap<String, Object> itemHashMap = (HashMap <String, Object>) parent.getItemAtPosition(position);
            if (curView != null) curView.setBackgroundResource(R.drawable.appfunc_rename);
            curView = view;
            view.setBackgroundResource(R.drawable.selected_item);
        }
    };*/

    public void onClickBack(View view) {
        finish();
    }

    public void onClickChangeType(View view) {
        TextView textView = (TextView) findViewById(R.id.text_type);
        flagInput = !flagInput;
        if (flagInput) {
            textView.setText(getString(R.string.input_message));
            showMessages(true);
        }
        else {
            textView.setText(R.string.output_message);
            showMessages(false);
        }
    }

    public void onClickSend(View view) {
        Intent intent = new Intent(this, SmsSendActivity.class);
        startActivity(intent);
    }
}
