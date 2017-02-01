package com.example.sedgw.pipboy_v11;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

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
        return view;
    }
}

