package com.example.sedgw.pipboy_v11.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.sedgw.pipboy_v11.data.MainContract.ObjectEntry;

/**
 * Created by nechuhaev on 24.01.2017.
 */

public class ObjectBDHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = ObjectBDHelper.class.getSimpleName();

    //Name of BD
    private static final String DATABASE_NAME = "object.db";

    //Version of BD
    private static final int DATABASE_VERSION = 1;

    public ObjectBDHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_OBJECT_TABLE = "CREATE TABLE " + ObjectEntry.TABLE_NAME + " (" +
                ObjectEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ObjectEntry.COLUMN_CODE + " TEXT NOT NULL, " +
                ObjectEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                ObjectEntry.COLUMN_TYPE + " TEXT NOT NULL, " +
                ObjectEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                ObjectEntry.COLUMN_LAT + " REAL, " +
                ObjectEntry.COLUMN_LON + " REAL, " +
                ObjectEntry.COLUMN_MESSAGE + " TEXT, " +
                ObjectEntry.COLUMN_PATH_TO_VIDEO + " TEXT NOT NULL, " +
                ObjectEntry.COLUMN_PATH_TO_IMAGE + " TEXT NOT NULL, " +
                ObjectEntry.COLUMN_PATH_TO_SOUND + " TEXT NOT NULL)";
        db.execSQL(SQL_CREATE_OBJECT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
