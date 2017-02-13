package com.sedg.pipboy_v11.sms;

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
import com.sedg.pipboy_v11.data.MainContract.SmsEntry;
import com.sedg.pipboy_v11.data.MainContract.ContactEntry;
import com.sedg.pipboy_v11.data.ObjectBDHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sedgw on 11.02.2017.
 */

public class SmsActivity extends Activity {

    public ObjectBDHelper dbHelper;
    public ListView listViewSms;
    public ArrayList<HashMap<String, Object>> objectsArrayListSms;
    public ListView listViewContact;
    public ArrayList<HashMap<String, Object>> objectsArrayListContact;

    public static final String NUMBER = "number";
    public static final String MESSAGE = "message";
    public static final String TIMESTAMP = "timestamp";

    public static final String NAME = "name";

    public Boolean flagInput = true;

    public View curView;
    public String curNameForView = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sms);

        dbHelper = new ObjectBDHelper(this);
        listViewSms = (ListView) findViewById(R.id.list_items);
        objectsArrayListSms = new ArrayList<HashMap<String, Object>>();
        listViewContact = (ListView) findViewById(R.id.list_contact);
        objectsArrayListContact = new ArrayList<HashMap<String, Object>>();
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
        showContacts();
    }

    public ArrayList<HashMap<String, Object>> selectSms(Boolean flag) {
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
        String number_str;
        if (cursor != null) {
            while (cursor.moveToNext()) {
                number_str = cursor.getString(cursor.getColumnIndex(SmsEntry.COLUMN_NUMBER));
                String[] select_column_for_contact = { ContactEntry.COLUMN_NAME };
                String where_expression_for_contact = ContactEntry.COLUMN_NUMBER + " = ?";
                String[] where_args_for_contact = { number_str };
                //Execute query
                Cursor cursor_for_contact = db.query(
                        ContactEntry.TABLE_NAME,
                        select_column_for_contact,
                        where_expression_for_contact,
                        where_args_for_contact,
                        null,
                        null,
                        null);
                if (cursor_for_contact != null) {
                    if (cursor_for_contact.moveToNext()) {
                        number_str = cursor_for_contact.getString(cursor_for_contact.getColumnIndex(ContactEntry.COLUMN_NAME));
                    }
                }
                if (cursor_for_contact != null) cursor_for_contact.close();
                if (!curNameForView.equals("")) {
                    if (number_str.equals(curNameForView)) {
                        hm = new HashMap<>();
                        hm.put(NUMBER, number_str);
                        hm.put(MESSAGE, cursor.getString(cursor.getColumnIndex(SmsEntry.COLUMN_MESSAGE)));
                        hm.put(TIMESTAMP, cursor.getString(cursor.getColumnIndex(SmsEntry.COLUMN_TIMESTAMP)));
                        curArrayList.add(hm);
                    }
                }
                else {
                    hm = new HashMap<>();
                    hm.put(NUMBER, number_str);
                    hm.put(MESSAGE, cursor.getString(cursor.getColumnIndex(SmsEntry.COLUMN_MESSAGE)));
                    hm.put(TIMESTAMP, cursor.getString(cursor.getColumnIndex(SmsEntry.COLUMN_TIMESTAMP)));
                    curArrayList.add(hm);
                }
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
        objectsArrayListSms = selectSms(flag);
        SimpleAdapter adapter = new SimpleAdapter(this, objectsArrayListSms,
                R.layout.list_item_sms, new String[]{NUMBER, MESSAGE, TIMESTAMP},
                new int[]{R.id.number_text, R.id.message_text, R.id.timestamp_text});

        if (adapter.isEmpty()) {
            //Toast.makeText(this, R.string.empty_array_list, Toast.LENGTH_LONG).show();
            listViewSms.setAdapter(null);
        }
        else listViewSms.setAdapter(adapter);
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

    public ArrayList<HashMap<String, Object>> selectContacts() {
        ArrayList<HashMap<String, Object>> curArrayList = new ArrayList<HashMap<String, Object>>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //Array of column and values
        String[] select_column = { ContactEntry.COLUMN_NAME };
        //Execute query
        Cursor cursor = db.query(
                ContactEntry.TABLE_NAME,
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
                hm.put(NAME, cursor.getString(cursor.getColumnIndex(ContactEntry.COLUMN_NAME)) );
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

    public void showContacts() {
        objectsArrayListContact = selectContacts();
        SimpleAdapter adapter = new SimpleAdapter(this, objectsArrayListContact,
                R.layout.list_item_contact_without_number, new String[]{ NAME },
                new int[]{ R.id.name_contact_text });

        if (adapter.isEmpty()) {
            //Toast.makeText(this, R.string.empty_array_list, Toast.LENGTH_LONG).show();
            listViewContact.setAdapter(null);
        }
        else listViewContact.setAdapter(adapter);
        listViewContact.setOnItemClickListener(itemClickListener);
    }

    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            HashMap<String, Object> itemHashMap = (HashMap<String, Object>) parent.getItemAtPosition(position);
            if (curView != null) curView.setBackgroundResource(R.drawable.appfunc_rename);
            if (curView == view) {
                view.setBackgroundResource(R.drawable.appfunc_rename);
                curView = null;
                curNameForView = "";
                showMessages(flagInput);
            } else {
                curView = view;
                view.setBackgroundResource(R.drawable.selected_item);
                curNameForView = itemHashMap.get(NAME).toString();
                showMessages(flagInput);
            }
        }
    };

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
