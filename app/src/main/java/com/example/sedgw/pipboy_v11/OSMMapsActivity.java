package com.example.sedgw.pipboy_v11;

/**
 * Created by sedgw on 03.01.2017.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.WindowManager;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.List;
import java.util.logging.ErrorManager;

public class OSMMapsActivity extends Activity {

    private LocationManager locationManager;
    private MapView map;
    private IMapController mapController;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_osmmaps);

        map = (MapView) findViewById(R.id.map);

        //org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants.setUserAgentValue(BuildConfig.APPLICATION_ID);
        //map.setTileSource(new XYTileSource("tiles", 0, 15, 256, ".png", new String[] {}));
        map.setTileSource(TileSourceFactory.MAPNIK);

        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        map.setUseDataConnection(false);

        mapController = map.getController();
        mapController.setZoom(16);
        map.setMinZoomLevel(12);
        map.setMaxZoomLevel(18);

        //map.setScrollableAreaLimitDouble();

        MyLocationNewOverlay mLocationOverlay = new MyLocationNewOverlay(map);
        mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this),map);
        mLocationOverlay.enableMyLocation();
        map.getOverlays().add(mLocationOverlay);

        map.setScrollableAreaLimitDouble(new BoundingBox(54.0,114.0,52.0,112.0));
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mapController.animateTo(new GeoPoint(52.02,113.2));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) == PackageManager.PERMISSION_GRANTED ) {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 5, 10, locationListener);
        }
        else {
            //обработка ошибки доступа
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) == PackageManager.PERMISSION_GRANTED ) {

            locationManager.removeUpdates(locationListener);
        }
        else {
            //обработка ошибки доступа
        }
    }

    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            showLocation(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
            if ( ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION ) == PackageManager.PERMISSION_GRANTED ) {

                showLocation(locationManager.getLastKnownLocation(provider));
            }
            else {
                //обработка ошибки доступа
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    private void showLocation(Location location) {
        if (location == null)
            return;
        if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
            mapController.animateTo(new GeoPoint(location.getLatitude(),location.getLongitude()));
        }
    }

}

