package com.example.sedgw.pipboy_v11.admin;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sedgw.pipboy_v11.OSMMapsActivity;
import com.example.sedgw.pipboy_v11.R;

/**
 * Created by sedgw on 03.02.2017.
 */

public class AdminListActivity extends Activity {

    private String code = "";
    private String name = "";
    private String title;
    private String message = "";

    private Uri videoProvider;
    private Uri audioProvider;
    private Uri imageProvider;

    private Double coorLat = 0d;
    private Double coorLon = 0d;

    static final private int GET_IMAGE = 100;
    static final private int GET_AUDIO = 101;
    static final private int GET_VIDEO = 102;
    static final private int GET_COOR = 103;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_admin_list);
    }

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
    }

    public void onClickMessage(View view) {
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
        title = edit_title.getText().toString();
    }

    public void onClickDelete(View view) {

    }

    public void onClickAdd(View view) {
    }

    public void onClickView(View view) {
    }
}
