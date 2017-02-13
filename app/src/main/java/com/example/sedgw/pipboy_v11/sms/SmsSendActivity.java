package com.example.sedgw.pipboy_v11.sms;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.sedgw.pipboy_v11.R;
import com.example.sedgw.pipboy_v11.data.MainContract.SmsEntry;
import com.example.sedgw.pipboy_v11.data.MainContract.ContactEntry;
import com.example.sedgw.pipboy_v11.data.ObjectBDHelper;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by sedgw on 11.02.2017.
 */

public class SmsSendActivity extends Activity {

    public ObjectBDHelper dbHelper;
    public ListView listViewContact;
    public ArrayList<HashMap<String, Object>> objectsArrayListContact;

    public static final String NAME = "name";

    public View curView;
    public String curNameForView = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_send_sms);

        dbHelper = new ObjectBDHelper(this);
        listViewContact = (ListView) findViewById(R.id.list_contact);
        objectsArrayListContact = new ArrayList<HashMap<String, Object>>();
    }

    public void onClickBack(View view) {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showContacts();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }

    public void onClickClear(View view) {
        EditText editText =(EditText) findViewById(R.id.edit_message);
        editText.setText("");
    }

    public void onClickSend(View view) {
        if (curNameForView.equals("")) {
            Toast.makeText(this, R.string.error_not_change_contact, Toast.LENGTH_SHORT).show();
            return;
        }
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();

        //get number
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] select_column_for_contact = { ContactEntry.COLUMN_NUMBER };
        String where_expression_for_contact = ContactEntry.COLUMN_NAME + " = ?";
        String[] where_args_for_contact = { curNameForView };
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
                String number = cursor_for_contact.getString(cursor_for_contact.getColumnIndex(ContactEntry.COLUMN_NUMBER));
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent("SMS_SENT"), 0);
                    smsManager.sendTextMessage(number, null, message, sentPI, null);
                    Toast.makeText(this, R.string.success_send_message, Toast.LENGTH_LONG).show();
                    addNewOutputMessageToDB(number, message);
                    cursor_for_contact.close();
                }
                catch (Exception e ) {
                    Toast.makeText(this, R.string.error_send_message, Toast.LENGTH_LONG).show();
                    //this for view errors with sms sending
                    //editText.setText(e.toString());
                    cursor_for_contact.close();
                    db.close();
                    return;
                }
            }
        }
        else {
            Toast.makeText(this, R.string.error_find_number, Toast.LENGTH_LONG).show();
        }
        db.close();
    }

    public void addNewOutputMessageToDB(String number, String message) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SmsEntry.COLUMN_NUMBER, number);
        values.put(SmsEntry.COLUMN_MESSAGE, message);
        values.put(SmsEntry.COLUMN_FLAG_INPUT, 0);
        values.put(SmsEntry.COLUMN_TIMESTAMP, android.text.format.DateFormat.format("hh:mm:ss dd MMMM yyyy ", new Date()).toString());
        long newRowId = db.insert(SmsEntry.TABLE_NAME, null, values);
        if (newRowId == -1) {
            Toast.makeText(this, R.string.message_error_on_insert, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.message_success_insert, Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

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
            } else {
                curView = view;
                view.setBackgroundResource(R.drawable.selected_item);
                curNameForView = itemHashMap.get(NAME).toString();
            }
        }
    };
}
