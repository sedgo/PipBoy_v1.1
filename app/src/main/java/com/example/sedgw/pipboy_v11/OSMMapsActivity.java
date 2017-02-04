package com.example.sedgw.pipboy_v11;

/**
 * Created by sedgw on 03.01.2017.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class OSMMapsActivity extends Activity {

    public MapView map;
    public IMapController mapController;

    private static double currentLat =0;
    private static double currentLon =0;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_osmmaps);

        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        map.setUseDataConnection(false);
        map.setClickable(true);

        mapController = map.getController();
        mapController.setZoom(16);
        map.setMinZoomLevel(12);
        map.setMaxZoomLevel(17);

        map.setScrollableAreaLimitDouble(new BoundingBox(54.0,114.0,52.0,112.0));
        mapController.animateTo(new GeoPoint(52.02,113.2));

        MyLocationNewOverlay myLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(getApplicationContext()), map);
        map.getOverlays().add(myLocationOverlay);
        myLocationOverlay.enableMyLocation();
        myLocationOverlay.enableFollowLocation();


        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                currentLat = location.getLatitude();
                currentLon = location.getLongitude();
                Toast.makeText(getBaseContext(),currentLat+"-"+currentLon, Toast.LENGTH_SHORT).show();
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        startService(new Intent(this, MyLocationService.class));


    }

    public void onClickBack(View view) {
        finish();
    }
}

