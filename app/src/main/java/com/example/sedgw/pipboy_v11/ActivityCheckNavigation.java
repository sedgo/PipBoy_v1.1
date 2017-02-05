package com.example.sedgw.pipboy_v11;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

/**
 * Created by sedgw on 05.02.2017.
 */

public class ActivityCheckNavigation extends Activity {
    TextView tvEnabledGPS;
    TextView tvStatusGPS;
    TextView tvLocationGPS;
    TextView tvEnabledNet;
    TextView tvStatusNet;
    TextView tvLocationNet;

    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_check_navigation);

        tvEnabledGPS = (TextView) findViewById(R.id.tvEnabledGPS);
        tvStatusGPS = (TextView) findViewById(R.id.tvStatusGPS);
        tvLocationGPS = (TextView) findViewById(R.id.tvLocationGPS);
        tvEnabledNet = (TextView) findViewById(R.id.tvEnabledNet);
        tvStatusNet = (TextView) findViewById(R.id.tvStatusNet);
        tvLocationNet = (TextView) findViewById(R.id.tvLocationNet);

        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.nav_error_sdk), Toast.LENGTH_LONG ).show();
            return;
        }

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 10, 10, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000 * 10, 10, locationListener);
        checkEnabled();
        if (locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null)
            tvLocationGPS.setText(formatLocation(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)));
        if (locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) != null)
            tvLocationNet.setText(formatLocation(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)));
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(locationListener);
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            showLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            if (provider.equals(LocationManager.GPS_PROVIDER)) {
                tvStatusGPS.setText(getResources().getString(R.string.nav_status) + ": " + String.valueOf(status));
            }
            else if (provider.equals(LocationManager.NETWORK_PROVIDER)) {
                tvStatusNet.setText(getResources().getString(R.string.nav_status) + ": " + String.valueOf(status));
            }
        }

        @Override
        public void onProviderEnabled(String provider) {
            checkEnabled();
            showLocation(locationManager.getLastKnownLocation(provider));
        }

        @Override
        public void onProviderDisabled(String provider) {
            checkEnabled();
        }
    };

    private void showLocation(Location location) {
        if (location == null) {
            return;
        }
        if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
            tvLocationGPS.setText(formatLocation(location));
        }
        else if (location.getProvider().equals(LocationManager.NETWORK_PROVIDER)) {
            tvLocationNet.setText(formatLocation(location));
        }
    }

    private String formatLocation(Location location) {
        if (location == null) return "";
        return String.format(getResources().getString(R.string.nav_coordinates) + ":\r\nlat = %1.4f\r\nlon = %2.4f\n" + getResources().getString(R.string.nav_time) + " = %3$tF %3$tT",
                location.getLatitude(), location.getLongitude(), new Date(location.getTime()));
    }

    private void checkEnabled() {
        String enabledGPS = "";
        String enabledNET = "";
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) enabledGPS = getResources().getString(R.string.YES);
        else enabledGPS = getResources().getString(R.string.NO);
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) enabledNET = getResources().getString(R.string.YES);
        else enabledNET = getResources().getString(R.string.NO);
        tvEnabledGPS.setText(getResources().getString(R.string.nav_enabled) + " " + enabledGPS);
        tvEnabledNet.setText(getResources().getString(R.string.nav_enabled) + " " + enabledNET);
    }

    public void onClickLocationSettings(View view) {
        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }

    public void onClickBack(View view) {
        finish();
    }
}
