package com.sedg.pipboy_v11;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.sedgw.pipboy_v11.R;
import com.sedg.pipboy_v11.database.DBfirstActivity;
import com.sedg.pipboy_v11.radio.RadioActivity;
import com.sedg.pipboy_v11.sms.SmsActivity;
import com.sedg.pipboy_v11.timer.TimerActivity;
import com.sedg.pipboy_v11.weather.WeatherActivity;

/**
 * Created by sedgw on 19.01.2017.
 */

public class Fragment_Menu extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        ImageButton buttonMap = (ImageButton) view.findViewById(R.id.buttonMap);
        buttonMap.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          Intent intent = new Intent(getActivity(), OSMMapsActivity.class);
                                          startActivity(intent);
                                      }
                                  });
        ImageButton buttonBD = (ImageButton) view.findViewById(R.id.buttonBD);
        buttonBD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DBfirstActivity.class);
                startActivity(intent);
            }
        });
        ImageButton buttonSettings = (ImageButton) view.findViewById(R.id.buttonSettings);
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
            }
        });
        ImageButton buttonWeather = (ImageButton) view.findViewById(R.id.buttonWeather);
        buttonWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WeatherActivity.class);
                startActivity(intent);
            }
        });
        ImageButton buttonSms = (ImageButton) view.findViewById(R.id.buttonChat);
        buttonSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SmsActivity.class);
                startActivity(intent);
            }
        });
        ImageButton buttonRadio = (ImageButton) view.findViewById(R.id.buttonRadio);
        buttonRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RadioActivity.class);
                startActivity(intent);
            }
        });
        ImageButton buttonTimer = (ImageButton) view.findViewById(R.id.buttonTimer);
        buttonTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TimerActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}

