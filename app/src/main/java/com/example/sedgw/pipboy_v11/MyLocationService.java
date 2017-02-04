package com.example.sedgw.pipboy_v11;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

public class MyLocationService extends Service {

    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private static double currentLat =0;
    private static double currentLon =0;

    public MyLocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "MyLocationService запущен", Toast.LENGTH_LONG).show();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        addListenerLocation();
        //Toast.makeText(this, "MyLocationService выполнил команду", Toast.LENGTH_LONG).show();
        return super.onStartCommand(intent, flags, startId);
    }

    private void addListenerLocation() {
        mLocationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                currentLat = location.getLatitude();
                currentLon = location.getLongitude();
                Toast.makeText(getBaseContext(),currentLat+"-"+currentLon, Toast.LENGTH_SHORT).show();
            }

            @Override

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
                checkPermission("android.permission.ACCESS_COARSE_LOCATION", 1, 1);
                Location lastKnownLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if(lastKnownLocation!=null){
                    currentLat = lastKnownLocation.getLatitude();
                    currentLon = lastKnownLocation.getLongitude();
                    Toast.makeText(getBaseContext(),currentLat+"-"+currentLon, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };
        mLocationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 500, 10, mLocationListener);
    }

    @Override
    public void onDestroy() {
        checkPermission("android.permission.ACCESS_COARSE_LOCATION", 1, 1);
        mLocationManager.removeUpdates(mLocationListener);
        super.onDestroy();
    }
}
