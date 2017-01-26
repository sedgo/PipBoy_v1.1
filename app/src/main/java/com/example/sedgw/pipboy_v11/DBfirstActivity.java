package com.example.sedgw.pipboy_v11;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sedgw.pipboy_v11.data.MainContract.ObjectEntry;
import com.example.sedgw.pipboy_v11.data.ObjectBDHelper;

/**
 * Created by nechuhaev on 24.01.2017.
 */

public class DBfirstActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_first);
    }

    public void onClickOK(View view) {
        EditText editText = (EditText) findViewById(R.id.code);
        TextView textView = (TextView) findViewById(R.id.textView);

        //hide keyboard
        editText.clearFocus();
        //another method: getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT‌​_INPUT_STATE_ALWAYS_‌​HIDDEN);
        if (editText.getText().length() != R.integer.length_of_code) {
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
            String where_expression = ObjectEntry.COLUMN_CODE + " = '?'";
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
                if (cursor.moveToFirst()) {
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
