package com.example.sedgw.pipboy_v11;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.support.v7.app.AppCompatActivity;
import android.graphics.Typeface;
import android.text.Html;
import android.widget.TextView;

/**
 * Created by sedgw on 08.02.2017.
 */

public class WeatherActivity extends Activity {

    TextView cityField, detailsField, currentTemperatureField, humidity_field, pressure_field, weatherIcon, updatedField, wind_field;
    Typeface weatherFont;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
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

        WeatherFunction.placeIdTask asyncTask =new WeatherFunction.placeIdTask(new WeatherFunction.AsyncResponse() {
            public void processFinish(String weather_city, String weather_description, String weather_temperature, String weather_humidity, String weather_pressure, String weather_updatedOn, String weather_iconText, String sun_rise, String weather_windspeed) {

                cityField.setText(weather_city);
                updatedField.setText("обновлено "+weather_updatedOn);
                detailsField.setText(weather_description);
                currentTemperatureField.setText(weather_temperature+Html.fromHtml("&#xf03c"));
                humidity_field.setText("Влажность: "+weather_humidity);
                pressure_field.setText("Атм. давление: "+weather_pressure);
                wind_field.setText("Ветер: "+weather_windspeed);
                weatherIcon.setText(Html.fromHtml(weather_iconText));
            }
        });
        asyncTask.execute("52.03", "113.55"); //  asyncTask.execute("Latitude", "Longitude") сюда кароче нужно из гпса долготу широту запилить
        //asyncTask.execute("55.86", "37.66");


    }

    public void onClickBack(View view) {
        finish();
    }
}
