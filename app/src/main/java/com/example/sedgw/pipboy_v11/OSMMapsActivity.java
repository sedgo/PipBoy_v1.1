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
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.osmdroid.api.IMapController;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.MinimapOverlay;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

import java.util.ArrayList;

public class OSMMapsActivity extends Activity implements MapEventsReceiver{

    public Integer radius_marker = 5;
    public MapView map;
    public IMapController mapController;
    public MinimapOverlay minimapOverlay;
    public LocationManager locationManager;
    public Marker navMarker;
    public Marker selectedMarker;

    public double currentLat = 0;
    public double currentLon = 0;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
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

        //Navigation by osmmaps - not worked (why? i don't khow!) maybe gps not working on my phone
        /* MyLocationNewOverlay myLocationOverlay = new MyLocationNewOverlay(map);
        map.getOverlays().add(myLocationOverlay);
        myLocationOverlay.enableMyLocation();
        myLocationOverlay.enableFollowLocation(); */

        minimapOverlay = new MinimapOverlay(getApplicationContext(), map.getTileRequestCompleteHandler());
        minimapOverlay.setZoomDifference(5);
        minimapOverlay.setOptionsMenuEnabled(true);
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        minimapOverlay.setHeight(size.y / 3);
        minimapOverlay.setWidth(size.y / 3);
        map.getOverlays().add(minimapOverlay);
        map.setBackgroundResource(R.drawable.bg_h);

        navMarker = new Marker(map);
        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(this);
        map.getOverlays().add(0, mapEventsOverlay);

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
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        map.getOverlays().remove(selectedMarker);
        selectedMarker = new Marker(map);
        selectedMarker.setPosition(p);
        selectedMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        selectedMarker.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.marker_tap, null));
        selectedMarker.setTitle(getResources().getString(R.string.tap_text) + p.getLatitude() + "\r\n" + p.getLongitude() );
        map.getOverlays().add(selectedMarker);
        InfoWindow.closeAllInfoWindowsOn(map);
        map.invalidate();
        Intent answerIntent = new Intent();
        answerIntent.putExtra("LAT", p.getLatitude());
        answerIntent.putExtra("LON", p.getLongitude());
        setResult(RESULT_OK, answerIntent);
        return true;
    }

    @Override
    public boolean longPressHelper(GeoPoint p) {
        return false;
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
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_show_location), Toast.LENGTH_SHORT).show();
            return;
        }
        GeoPoint p = new GeoPoint(location.getLatitude(), location.getLongitude());
        map.getOverlays().remove(navMarker);
        mapController.animateTo(p);
        navMarker.setPosition(p);
        navMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        navMarker.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.appfunc_screennow, null));
        navMarker.setTitle(getResources().getString(R.string.tap_nav));
        map.getOverlays().add(navMarker);
        map.invalidate();
        TextView coord = (TextView) findViewById(R.id.textview_coordinates);
        coord.setText(formatLocation(location));
    }

    public void toLocation() {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            showLocation(locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER));
        }
        /*else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            showLocation(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
        }*/
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

