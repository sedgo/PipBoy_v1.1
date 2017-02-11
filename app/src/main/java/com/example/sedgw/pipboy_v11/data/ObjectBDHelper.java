package com.example.sedgw.pipboy_v11.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.sedgw.pipboy_v11.data.MainContract.ObjectEntry;
import com.example.sedgw.pipboy_v11.data.MainContract.SmsEntry;

/**
 * Created by nechuhaev on 24.01.2017.
 */

public class ObjectBDHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = ObjectBDHelper.class.getSimpleName();

    //Name of BD
    private static final String DATABASE_NAME = "object.db";

    //Version of BD
    private static final int DATABASE_VERSION = 3;

    public ObjectBDHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //objects
        String SQL_CREATE_OBJECT_TABLE_OBJECT = "CREATE TABLE " + ObjectEntry.TABLE_NAME + " (" +
                ObjectEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ObjectEntry.COLUMN_CODE + " TEXT NOT NULL, " +
                ObjectEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                ObjectEntry.COLUMN_TYPE + " TEXT NOT NULL, " +
                ObjectEntry.COLUMN_TITLE + " TEXT, " +
                ObjectEntry.COLUMN_LAT + " REAL, " +
                ObjectEntry.COLUMN_LON + " REAL, " +
                ObjectEntry.COLUMN_MESSAGE + " TEXT, " +
                ObjectEntry.COLUMN_PATH_TO_VIDEO + " TEXT, " +
                ObjectEntry.COLUMN_PATH_TO_IMAGE + " TEXT, " +
                ObjectEntry.COLUMN_PATH_TO_SOUND + " TEXT)";
        db.execSQL(SQL_CREATE_OBJECT_TABLE_OBJECT);

        //sms
        String SQL_CREATE_OBJECT_TABLE_SMS = "CREATE TABLE " + SmsEntry.TABLE_NAME + " (" +
                SmsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SmsEntry.COLUMN_FLAG_INPUT + " INTEGER NOT NULL, " +
                SmsEntry.COLUMN_NUMBER + " TEXT, " +
                SmsEntry.COLUMN_MESSAGE + " TEXT NOT NULL, " +
                SmsEntry.COLUMN_TIMESTAMP + " TEXT NOT NULL )";
        db.execSQL(SQL_CREATE_OBJECT_TABLE_SMS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.w("SQLite", "UPDATE VERSION DB from version " + oldVersion + " to version " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + SmsEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ObjectEntry.TABLE_NAME);
        onCreate(db);
    }
}
