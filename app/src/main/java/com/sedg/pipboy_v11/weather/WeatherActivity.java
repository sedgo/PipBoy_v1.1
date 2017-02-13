package com.sedg.pipboy_v11.weather;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.graphics.Typeface;
import android.text.Html;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sedgw.pipboy_v11.R;

/**
 * Created by sedgw on 08.02.2017.
 */

public class WeatherActivity extends Activity {

    TextView cityField, detailsField, currentTemperatureField, humidity_field, pressure_field, weatherIcon, updatedField, wind_field;
    Typeface weatherFont;
    WeatherFunction.placeIdTask asyncTaskWeather;
    WeatherFunction.findCity asyncTaskCityName;
    public LocationManager locationManager;

    //file pf weather
    public static final String APP_PREFERENCES = "weather_settings";
    private SharedPreferences weatherSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.activity_weather);
        weatherFont = Typeface.createFromAsset(getAssets(), "fonts/weathericons-regular-webfont.ttf");

        cityField = (TextView)findViewById(R.id.city_field);
        updatedField = (TextView)findViewById(R.id.updated_field);
        detailsField = (TextView)findViewById(R.id.details_field);
        currentTemperatureField = (TextView)findViewById(R.id.current_temperature_field);
        currentTemperatureField.setTypeface(weatherFont);
        humidity_field = (TextView)findViewById(R.id.humidity_field);
        pressure_field = (TextView)findViewById(R.id.pressure_field);
        wind_field = (TextView)findViewById(R.id.wind_field);
        weatherIcon = (TextView)findViewById(R.id.weather_icon);
        weatherIcon.setTypeface(weatherFont);

        weatherSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //start coordinates update
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 10, 10, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000 * 10, 10, locationListener);

        if (weatherSettings.contains("weather_city")) {
            cityField.setText(weatherSettings.getString("weather_city", ""));
            updatedField.setText(getResources().getString(R.string.weather_update) + " " + weatherSettings.getString("weather_updatedOn", ""));
            detailsField.setText(weatherSettings.getString("weather_description", ""));
            currentTemperatureField.setText(weatherSettings.getString("weather_temperature", "") + Html.fromHtml("&#xf03c"));
            humidity_field.setText(getResources().getString(R.string.weather_humidity) + " " + weatherSettings.getString("weather_humidity", ""));
            pressure_field.setText(getResources().getString(R.string.weather_pressure) + " " + weatherSettings.getString("weather_pressure", ""));
            wind_field.setText(getResources().getString(R.string.weather_wind) + " " + weatherSettings.getString("weather_windspeed", ""));
            weatherIcon.setText(Html.fromHtml(weatherSettings.getString("weather_iconText", "")));
        }
    }

    public void updateWeather() {
        asyncTaskWeather = new WeatherFunction.placeIdTask(new WeatherFunction.AsyncResponse() {
            public void processFinish(String weather_city, String weather_description, String weather_temperature, String weather_humidity, String weather_pressure, String weather_updatedOn, String weather_iconText, String sun_rise, String weather_windspeed) {

                //cityField.setText(weather_city);
                updatedField.setText(getResources().getString(R.string.weather_update) + " " + weather_updatedOn);
                detailsField.setText(weather_description);
                currentTemperatureField.setText(weather_temperature + Html.fromHtml("&#xf03c"));
                humidity_field.setText(getResources().getString(R.string.weather_humidity) + " " + weather_humidity);
                pressure_field.setText(getResources().getString(R.string.weather_pressure) + " " + weather_pressure);
                wind_field.setText(getResources().getString(R.string.weather_wind) + " " + weather_windspeed);
                weatherIcon.setText(Html.fromHtml(weather_iconText));

                SharedPreferences.Editor editor = weatherSettings.edit();
                //editor.putString("weather_city", weather_city);
                editor.putString("weather_updatedOn", weather_updatedOn);
                editor.putString("weather_description", weather_description);
                editor.putString("weather_temperature", weather_temperature);
                editor.putString("weather_humidity", weather_humidity);
                editor.putString("weather_pressure", weather_pressure);
                editor.putString("weather_windspeed", weather_windspeed);
                editor.putString("weather_iconText", weather_iconText);
                editor.apply();

                Toast.makeText(getApplicationContext(),R.string.weather_update_success, Toast.LENGTH_LONG).show();
            }
        });
        asyncTaskCityName = new WeatherFunction.findCity(new WeatherFunction.AsyncResponseCity() {
            public void processFinish(String output) {
                cityField.setText(output);

                SharedPreferences.Editor editor = weatherSettings.edit();
                editor.putString("weather_city", output);
                editor.apply();
            }
        });

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location == null) location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location == null) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.nav_coordinates_notfind), Toast.LENGTH_LONG).show();
                return;
            }
            asyncTaskWeather.execute(Double.toString(location.getLatitude()), Double.toString(location.getLongitude()));
            asyncTaskCityName.execute(Double.toString(location.getLatitude()), Double.toString(location.getLongitude()));
        }
        else Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_get_provider), Toast.LENGTH_LONG).show();
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    };

    public void onClickBack(View view) {
        finish();
    }

    public void onClickSync(View view) {
        updateWeather();
    }

}
