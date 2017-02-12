package com.example.sedgw.pipboy_v11.admin;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.sedgw.pipboy_v11.R;
import com.example.sedgw.pipboy_v11.data.MainContract;
import com.example.sedgw.pipboy_v11.data.MainContract.ContactEntry;
import com.example.sedgw.pipboy_v11.data.ObjectBDHelper;
import com.example.sedgw.pipboy_v11.sms.SmsActivity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sedgw on 11.02.2017.
 */



public class AdminContactActivity extends Activity {

    public ObjectBDHelper dbHelper;
    public ListView listView;
    public ArrayList<HashMap<String, Object>> objectsArrayList;

    public static final String NAME = "name";
    public static final String NUMBER = "number";

    public String curNumberForView = "";
    public View curView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.activity_admin_contact);

        dbHelper = new ObjectBDHelper(this);
        listView = (ListView) findViewById(R.id.list_view);
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
        updateListView();
    }

    public void onClickBack(View view) {
        finish();
    }

    public void onClickToSms(View view) {
        Intent intent = new Intent(this, SmsActivity.class);
        startActivity(intent);
    }

    public void onClickDelete(View view) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(ContactEntry.TABLE_NAME,
                ContactEntry.COLUMN_NUMBER + " = ?",
                new String[] { curNumberForView });
        updateListView();
        db.close();
    }

    public void onClickSave(View view) {
        EditText edit_name = (EditText) findViewById(R.id.edit_name);
        EditText edit_number = (EditText) findViewById(R.id.edit_number);
        String name = edit_name.getText().toString();
        String number = edit_number.getText().toString();

        if (!isPhoneNumberValid(number)) {
            Toast.makeText(this, R.string.admin_incorrect_number, Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //find this number in DB, if finding - update, else - add
        String[] select_column = { ContactEntry.COLUMN_NUMBER };
        String where_expression = ContactEntry.COLUMN_NUMBER + " = ?";
        String[] where_args = { number };
        //Execute query
        Cursor cursor = db.query(
                ContactEntry.TABLE_NAME,
                select_column,
                where_expression,
                where_args,
                null,
                null,
                null
        );
        if ( cursor != null && cursor.moveToNext() ) {
            cursor.close();
            ContentValues values = new ContentValues();
            values.put(ContactEntry.COLUMN_NAME, name);
            long newRowId = db.update(ContactEntry.TABLE_NAME,
                    values,
                    ContactEntry.COLUMN_NUMBER + " = ?",
                    new String[]{ number });
            if (newRowId == -1) {
                Toast.makeText(this, R.string.message_error_on_update, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.message_success_update, Toast.LENGTH_SHORT).show();
                updateListView();
            }
        }
        else {
            if (cursor != null) cursor.close();
            ContentValues values = new ContentValues();
            values.put(ContactEntry.COLUMN_NAME, name);
            values.put(ContactEntry.COLUMN_NUMBER, number);
            long newRowId = db.insert(ContactEntry.TABLE_NAME, null, values);
            if (newRowId == -1) {
                Toast.makeText(this, R.string.message_error_on_insert, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.message_success_insert, Toast.LENGTH_SHORT).show();
                updateListView();
            }
        }
        db.close();
    }

    public ArrayList<HashMap<String, Object>> selectAllContacts() {
        ArrayList<HashMap<String, Object>> curArrayList = new ArrayList<HashMap<String, Object>>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //Array of column and values
        String[] select_column = { ContactEntry.COLUMN_NAME, ContactEntry.COLUMN_NUMBER };
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
                hm.put(NUMBER, cursor.getString(cursor.getColumnIndex(ContactEntry.COLUMN_NUMBER)));
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
        objectsArrayList = selectAllContacts();
        SimpleAdapter adapter = new SimpleAdapter(this, objectsArrayList,
                R.layout.list_item_contact, new String[]{NAME, NUMBER},
                new int[]{R.id.name_text, R.id.number_text});

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
                view.setBackgroundResource(R.drawable.appfunc_rename);
                curView = null;
                curNumberForView = "";
            } else {
                curView = view;
                view.setBackgroundResource(R.drawable.selected_item);
                curNumberForView = itemHashMap.get(NUMBER).toString();
                EditText ed_name = (EditText) findViewById(R.id.edit_name);
                ed_name.setText(itemHashMap.get(NAME).toString());
                EditText ed_number = (EditText) findViewById(R.id.edit_number);
                ed_number.setText(curNumberForView);
            }
        }
    };

    public static boolean isPhoneNumberValid(String phoneNumber) {
        String regex = "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$";

        if (!phoneNumber.matches(regex)) {
            return false;
        }
        return true;
    }
}
