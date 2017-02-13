package com.example.sedgw.pipboy_v11.database;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sedgw.pipboy_v11.R;
import com.example.sedgw.pipboy_v11.data.MainContract.ObjectEntry;
import com.example.sedgw.pipboy_v11.data.ObjectBDHelper;

/**
 * Created by nechuhaev on 24.01.2017.
 */

public class DBfirstActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.activity_db_first);
    }


    public void onClickOK(View view) {
        EditText editText = (EditText) findViewById(R.id.code);
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText("");

        //hide keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

        SharedPreferences allSettings = getSharedPreferences("all_settings", Context.MODE_PRIVATE);
        if (editText.length() != allSettings.getInt("length_of_code", 8)) {
            textView.setText(R.string.message_not_enter_code);
        }
        else {
            textView.setText("");
            String cur_code = editText.getText().toString();
            //open DB
            ObjectBDHelper dbHelper = new ObjectBDHelper(this);
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            //Array of column and values
            String[] select_column = { ObjectEntry._ID, ObjectEntry.COLUMN_CODE };
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
                    //loading next activity of viewing item
                    Intent intent = new Intent(DBfirstActivity.this, DBmainActivity.class);
                    intent.putExtra("code", cursor.getString(cursor.getColumnIndex(ObjectEntry.COLUMN_CODE)));
                    startActivity(intent);
                }
                else {
                    textView.setText(R.string.message_code_not_find);
                }
            }
            else {
                textView.setText(R.string.message_error_select);
            }

            //closing sql objects
            cursor.close();
            db.close();
            dbHelper.close();
        }
    }
}
