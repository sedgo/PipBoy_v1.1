package com.example.sedgw.pipboy_v11.data;

import android.provider.BaseColumns;

/**
 * Created by nechuhaev on 24.01.2017.
 */

public class MainContract {

    private MainContract() {
    }

    public static final class ObjectEntry implements BaseColumns {
        public final static String TABLE_NAME = "objects";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_CODE = "code";
        public final static String COLUMN_NAME = "name";
        public final static String COLUMN_TYPE = "type";
        public final static String COLUMN_TITLE = "title";
        public final static String COLUMN_PATH_TO_VIDEO = "path_to_video";
        public final static String COLUMN_PATH_TO_IMAGE = "path_to_image";
        public final static String COLUMN_PATH_TO_SOUND = "path_to_sound";

    }
}