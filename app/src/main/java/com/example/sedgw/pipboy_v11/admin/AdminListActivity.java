package com.example.sedgw.pipboy_v11.admin;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.sedgw.pipboy_v11.OSMMapsActivity;
import com.example.sedgw.pipboy_v11.R;
import com.example.sedgw.pipboy_v11.data.MainContract;
import com.example.sedgw.pipboy_v11.data.ObjectBDHelper;
import com.example.sedgw.pipboy_v11.database.DBviewActivity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sedgw on 03.02.2017.
 */

public class AdminListActivity extends Activity {

    public String code = "";
    public String name = "";
    public String title;
    public String message = "";
    public String type = TYPE_OBJECT;

    public Uri videoProvider;
    public Uri audioProvider;
    public Uri imageProvider;

    public Double coorLat = 0d;
    public Double coorLon = 0d;

    public Boolean title_flag;

    static final private int GET_IMAGE = 100;
    static final private int GET_AUDIO = 101;
    static final private int GET_VIDEO = 102;
    static final private int GET_COOR = 103;

    public ObjectBDHelper dbHelper;
    public ListView listView;
    public ArrayList<HashMap<String, Object>> objectsArrayList;

    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String TYPE = "type";

    public static final String TYPE_OBJECT = "object";
    public static final String TYPE_MESSAGE = "message";
    public static final String TYPE_MEDIA = "media";

    public String curCodeForView = "";
    public View curView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.activity_admin_list);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //openDB
        dbHelper = new ObjectBDHelper(this);
        listView = (ListView) findViewById(R.id.list_items);
        objectsArrayList = new ArrayList<HashMap<String, Object>>();

        // Radio Group
        RadioButton rbuttonMedia = (RadioButton)findViewById(R.id.rbutton_media);
        rbuttonMedia.setOnClickListener(radioButtonClickListener);
        RadioButton rbuttonMessage = (RadioButton)findViewById(R.id.rbutton_message);
        rbuttonMessage.setOnClickListener(radioButtonClickListener);
        RadioButton rbuttonObject = (RadioButton)findViewById(R.id.rbutton_object);
        rbuttonObject.setOnClickListener(radioButtonClickListener);

        updateListView();
    }

    View.OnClickListener radioButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RadioButton rb = (RadioButton) v;
            switch (rb.getId()) {
                case R.id.rbutton_object:
                    type = TYPE_OBJECT;
                    break;
                case R.id.rbutton_message:
                    type = TYPE_MESSAGE;
                    break;
                case R.id.rbutton_media:
                    type = TYPE_MEDIA;
                    break;
                default:
                    break;
            }
        }
    };

    public void onClickBack(View view) {
        finish();
    }

    public void alert(String alert_message) {
        Toast toast = Toast.makeText(getApplicationContext(), alert_message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void onClickImage(View view) {
        Intent intent = new Intent()
                .setType("*/*")
                .setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Select a file"), GET_IMAGE);
    }

    public void onClickAudio(View view) {
        Intent intent = new Intent()
                .setType("*/*")
                .setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Select a file"), GET_AUDIO);
    }

    public void onClickVideo(View view) {
        Intent intent = new Intent()
                .setType("*/*")
                .setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Select a file"), GET_VIDEO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri selectedFile = data.getData();
            switch (requestCode) {
                case GET_IMAGE:
                    imageProvider = selectedFile;
                    break;
                case GET_AUDIO:
                    audioProvider = selectedFile;
                    break;
                case GET_VIDEO:
                    videoProvider = selectedFile;
                    break;
                case GET_COOR:
                    coorLat = Double.parseDouble(data.getExtras().get("LAT").toString());
                    coorLon = Double.parseDouble(data.getExtras().get("LON").toString());
                    Toast.makeText(getApplicationContext(), coorLat + " " + coorLon, Toast.LENGTH_LONG).show();
                    break;
                default:
                    alert(getResources().getString(R.string.admin_alert_error_reqcode));
                    break;
            }
        }
    }

    public void onClickCoor(View view) {
        Intent intent = new Intent(this, OSMMapsActivity.class);
        startActivityForResult(intent, GET_COOR);
    }

    public void onClickTitle(View view) {
        LinearLayout ll = (LinearLayout) findViewById(R.id.LL);
        ll.setVisibility(View.INVISIBLE);
        EditText edit_title = (EditText) findViewById(R.id.edit_title);
        edit_title.setVisibility(View.VISIBLE);
        ImageButton button_title_erase = (ImageButton) findViewById(R.id.button_title_erase);
        button_title_erase.setVisibility(View.VISIBLE);
        ImageButton button_title_ok = (ImageButton) findViewById(R.id.button_title_ok);
        button_title_ok.setVisibility(View.VISIBLE);
        title_flag = true;
    }

    public void onClickMessage(View view) {
        LinearLayout ll = (LinearLayout) findViewById(R.id.LL);
        ll.setVisibility(View.INVISIBLE);
        EditText edit_title = (EditText) findViewById(R.id.edit_title);
        edit_title.setVisibility(View.VISIBLE);
        ImageButton button_title_erase = (ImageButton) findViewById(R.id.button_title_erase);
        button_title_erase.setVisibility(View.VISIBLE);
        ImageButton button_title_ok = (ImageButton) findViewById(R.id.button_title_ok);
        button_title_ok.setVisibility(View.VISIBLE);
        title_flag = false;
    }

    public void onClickTitleErase(View view) {
        EditText edit_title = (EditText) findViewById(R.id.edit_title);
        edit_title.setText("");
    }

    public void onClickTitleOK(View view) {
        EditText edit_title = (EditText) findViewById(R.id.edit_title);
        edit_title.setVisibility(View.INVISIBLE);
        ImageButton button_title_erase = (ImageButton) findViewById(R.id.button_title_erase);
        button_title_erase.setVisibility(View.INVISIBLE);
        ImageButton button_title_ok = (ImageButton) findViewById(R.id.button_title_ok);
        button_title_ok.setVisibility(View.INVISIBLE);
        LinearLayout ll = (LinearLayout) findViewById(R.id.LL);
        ll.setVisibility(View.VISIBLE);
        if (title_flag) title = edit_title.getText().toString();
        else message = edit_title.getText().toString();
        edit_title.setText("");
    }

    public ArrayList<HashMap<String, Object>> selectAllObjects() {
        ArrayList<HashMap<String, Object>> curArrayList = new ArrayList<HashMap<String, Object>>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //Array of column and values
        String[] select_column = { MainContract.ObjectEntry._ID,
                MainContract.ObjectEntry.COLUMN_CODE,
                MainContract.ObjectEntry.COLUMN_NAME,
                MainContract.ObjectEntry.COLUMN_TYPE };
        //Execute query
        Cursor cursor = db.query(
                MainContract.ObjectEntry.TABLE_NAME,
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
                hm.put(CODE, cursor.getString(cursor.getColumnIndex(MainContract.ObjectEntry.COLUMN_CODE)) );
                hm.put(NAME, cursor.getString(cursor.getColumnIndex(MainContract.ObjectEntry.COLUMN_NAME)) );
                Integer icon = 0;
                switch (cursor.getString(cursor.getColumnIndex(MainContract.ObjectEntry.COLUMN_TYPE))) {
                    case TYPE_OBJECT:
                        icon = R.drawable.menu_expendbar;
                        break;
                    case TYPE_MESSAGE:
                        icon = R.drawable.menu_wallpaper;
                        break;
                    case TYPE_MEDIA:
                        icon = R.drawable.menu_feedback;
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), R.string.message_type_not_find + ": " +
                                cursor.getString(cursor.getColumnIndex(MainContract.ObjectEntry.COLUMN_CODE)),
                                Toast.LENGTH_LONG).show();
                        break;
                }
                hm.put(TYPE, icon);
                curArrayList.add(hm);
            }
            cursor.close();
        }
        else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.message_error_select), Toast.LENGTH_LONG).show();
        }
        //closing sql objects
        db.close();
        return curArrayList;
    }

    public void updateListView() {
        objectsArrayList = selectAllObjects();
        SimpleAdapter adapter = new SimpleAdapter(this, objectsArrayList,
                R.layout.list_item_object, new String[]{CODE, NAME, TYPE},
                new int[]{R.id.code_text, R.id.name_text, R.id.type});

        if (adapter.isEmpty()) {
            Toast.makeText(this, R.string.empty_array_list, Toast.LENGTH_LONG).show();
            listView.setAdapter(null);
        }
        else listView.setAdapter(adapter);
        listView.setOnItemClickListener(itemClickListener);
    }

    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            HashMap<String, Object> itemHashMap = (HashMap <String, Object>) parent.getItemAtPosition(position);
            if (curView != null) curView.setBackgroundResource(R.drawable.appfunc_rename);
            curView = view;
            view.setBackgroundResource(R.drawable.selected_item);
            curCodeForView = itemHashMap.get(CODE).toString();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }

    public void onClickDelete(View view) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(MainContract.ObjectEntry.TABLE_NAME,
                MainContract.ObjectEntry.COLUMN_CODE + " = ?",
                new String[] { curCodeForView });
        updateListView();
    }

    public void onClickAdd(View view) {
        EditText edit_code = (EditText) findViewById(R.id.edit_code);
        EditText edit_name = (EditText) findViewById(R.id.edit_name);
        code = edit_code.getText().toString();
        name = edit_name.getText().toString();

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        if (code.length() != getResources().getInteger(R.integer.length_of_code) ) {
            Toast.makeText(this, R.string.error_lenght_code, Toast.LENGTH_LONG).show();
            return;
        }
        else {
            //find this code in DB, if finding - cancel add (not to dublicate!)
            String[] select_column = { MainContract.ObjectEntry.COLUMN_CODE };
            String where_expression = MainContract.ObjectEntry.COLUMN_CODE + " = ?";
            String[] where_args = { code };
            //Execute query
            Cursor cursor = db.query(
                    MainContract.ObjectEntry.TABLE_NAME,
                    select_column,
                    where_expression,
                    where_args,
                    null,
                    null,
                    null
            );
            if ( cursor != null && cursor.moveToNext() ) {
                Toast.makeText(this, R.string.error_lenght_code, Toast.LENGTH_LONG).show();
                cursor.close();
                return;
            }
            cursor.close();
        }
        if (name.length() <= 0 ) {
            Toast.makeText(this, R.string.error_name_null, Toast.LENGTH_LONG).show();
            return;
        }

        //Array of column and values
        ContentValues values = new ContentValues();
        values.put(MainContract.ObjectEntry.COLUMN_CODE, code);
        values.put(MainContract.ObjectEntry.COLUMN_NAME, name);
        values.put(MainContract.ObjectEntry.COLUMN_TYPE, type);
        long newRowId = 0;
        switch (type) {
            case TYPE_OBJECT:
                if (audioProvider == null) {
                    Toast.makeText(this, R.string.empty_audio, Toast.LENGTH_LONG).show();
                    return;
                }
                if (videoProvider == null) {
                    Toast.makeText(this, R.string.empty_video, Toast.LENGTH_LONG).show();
                    return;
                }
                if (imageProvider == null) {
                    Toast.makeText(this, R.string.empty_image, Toast.LENGTH_LONG).show();
                    return;
                }
                values.put(MainContract.ObjectEntry.COLUMN_TITLE, title);
                values.put(MainContract.ObjectEntry.COLUMN_LAT, coorLat);
                values.put(MainContract.ObjectEntry.COLUMN_LON, coorLon);
                values.put(MainContract.ObjectEntry.COLUMN_PATH_TO_IMAGE, imageProvider.toString());
                values.put(MainContract.ObjectEntry.COLUMN_PATH_TO_VIDEO, videoProvider.toString());
                values.put(MainContract.ObjectEntry.COLUMN_PATH_TO_SOUND, audioProvider.toString());
                newRowId = db.insert(MainContract.ObjectEntry.TABLE_NAME, null, values);
                break;
            case TYPE_MESSAGE:
                values.put(MainContract.ObjectEntry.COLUMN_TITLE, title);
                values.put(MainContract.ObjectEntry.COLUMN_MESSAGE, message);
                newRowId = db.insert(MainContract.ObjectEntry.TABLE_NAME, null, values);
                break;
            case TYPE_MEDIA:
                if (audioProvider == null) {
                    Toast.makeText(this, R.string.empty_audio, Toast.LENGTH_LONG).show();
                    return;
                }
                if (videoProvider == null) {
                    Toast.makeText(this, R.string.empty_video, Toast.LENGTH_LONG).show();
                    return;
                }
                values.put(MainContract.ObjectEntry.COLUMN_PATH_TO_VIDEO, videoProvider.toString());
                values.put(MainContract.ObjectEntry.COLUMN_PATH_TO_SOUND, audioProvider.toString());
                newRowId = db.insert(MainContract.ObjectEntry.TABLE_NAME, null, values);
                break;
            default:
                break;
        }
        if (newRowId == -1) {
            Toast.makeText(this, R.string.message_error_on_insert, Toast.LENGTH_LONG).show();
        } else if (newRowId == 0) {
            Toast.makeText(this, R.string.message_type_not_checked, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, R.string.message_success_insert, Toast.LENGTH_LONG).show();
            updateListView();
        }
        db.close();
    }

    public void onClickView(View view) {
        if (curCodeForView == "") {
            Toast.makeText(getApplicationContext(), R.string.message_not_check_item, Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent = new Intent(this, DBviewActivity.class);
        intent.putExtra("code", curCodeForView);
        startActivity(intent);
    }
}
