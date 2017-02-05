package com.example.sedgw.pipboy_v11;

/**
 * Created by sedgw on 03.01.2017.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Dimension;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MinimapOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.gestures.RotationGestureDetector;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.Date;

public class OSMMapsActivity extends Activity {

    public MapView map;
    public IMapController mapController;
    public MinimapOverlay minimapOverlay;
    public LocationManager locationManager;
    public Integer widthMiniMap = 100;
    public Integer heightMiniMap = 100;

    private static double currentLat =0;
    private static double currentLon =0;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_osmmaps);

        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
        //map.setUseDataConnection(false);
        map.setClickable(true);

        //zoom
        mapController = map.getController();
        map.setBuiltInZoomControls(true);
        mapController.setZoom(16);
        map.setMinZoomLevel(12);
        map.setMaxZoomLevel(16);

        //blocking borders of loading maps
        //map.setScrollableAreaLimitDouble(new BoundingBox(54.0,114.0,52.0,112.0));

        //Navigation by osmmaps - not worked (why? i don't khow!)
        /* MyLocationNewOverlay myLocationOverlay = new MyLocationNewOverlay(map);
        map.getOverlays().add(myLocationOverlay);
        myLocationOverlay.enableMyLocation();
        myLocationOverlay.enableFollowLocation(); */

        minimapOverlay = new MinimapOverlay(getApplicationContext(), map.getTileRequestCompleteHandler());
        minimapOverlay.setWidth(widthMiniMap);
        minimapOverlay.setHeight(heightMiniMap);
        minimapOverlay.setZoomDifference(5);
        minimapOverlay.setOptionsMenuEnabled(true);
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        minimapOverlay.setPadding(size.y - heightMiniMap);
        map.getOverlays().add(minimapOverlay);

        //check for SDK (with marshmallow android i need to use checkPermission fuck)
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.nav_error_sdk), Toast.LENGTH_LONG ).show();
            return;
        }

        //start service for navigation
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 10, 10, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000    * 10, 10, locationListener);
        checkEnabled();
        toLocation();
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
            /* if (provider.equals(LocationManager.GPS_PROVIDER)) {
                tvStatusGPS.setText(getResources().getString(R.string.nav_status) + ": " + String.valueOf(status));
            }
            else if (provider.equals(LocationManager.NETWORK_PROVIDER)) {
                tvStatusNet.setText(getResources().getString(R.string.nav_status) + ": " + String.valueOf(status));
            } */
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
            Toast.makeText(getApplicationContext(), location. + " " + getResources().getString(R.string.error_show_location), Toast.LENGTH_SHORT).show();
            return;
        }
        mapController.animateTo(new GeoPoint(location.getLatitude(), location.getLongitude()));
        TextView coord = (TextView) findViewById(R.id.textview_coordinates);
        coord.setText(formatLocation(location));
    }

    public void toLocation() {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showLocation(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
        }
        else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            showLocation(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
        }
        else Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_get_provider), Toast.LENGTH_LONG).show();
    }

    private String formatLocation(Location location) {
        if (location == null) return "";
        return String.format("%1.4f : %2.4f",
                location.getLatitude(), location.getLongitude());
    }

    private void checkEnabled() {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            ImageButton but_status = (ImageButton) findViewById(R.id.status);
            but_status.setImageResource(R.drawable.location2);
        } else {
            ImageButton but_status = (ImageButton) findViewById(R.id.status);
            but_status.setImageResource(R.drawable.alert);
        }
    }

    public void onClickBack(View view) {
        finish();
    }

    public void onClickLocation(View view) {
        toLocation();
    }
}

