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
import android.view.View;
import android.view.WindowManager;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.List;
import java.util.logging.ErrorManager;

public class OSMMapsActivity extends Activity {

    private MyLocationNewOverlay mLocationOverlay;
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
        map.setMaxZoomLevel(17);

        //map.setScrollableAreaLimitDouble();

        mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(context),map);
        mLocationOverlay.enableMyLocation();
        map.getOverlays().add(this.mLocationOverlay);

        mapController.animateTo(new GeoPoint(52.02,113.2));
    }

    public void onClickBack(View view) {
        finish();
    }
}

