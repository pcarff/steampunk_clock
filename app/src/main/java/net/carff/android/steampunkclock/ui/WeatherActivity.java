package net.carff.android.steampunkclock.ui;

import android.os.Bundle;
import android.widget.TextView;
import net.carff.android.steampunkclock.R;
import net.carff.android.steampunkclock.SteampunkClockApplication;
import net.carff.android.steampunkclock.util.UIUtils;
import android.widget.ImageView;

public class WeatherActivity extends BaseActivity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_dashboard);
        
        TextView cityTitle = findViewById(R.id.weather_city_title);
        String city = ((SteampunkClockApplication) getApplication()).getCity();
        cityTitle.setText(city != null ? city.toUpperCase() : "WEATHER STATION");

        updateStats();
        startUpdateLoop();
    }

    private void startUpdateLoop() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateStats();
                        }
                    });
                    try { Thread.sleep(10000); } catch (Exception e) {}
                }
            }
        }).start();
    }

    private void updateStats() {
        SteampunkClockApplication app = (SteampunkClockApplication) getApplication();
        ((TextView) findViewById(R.id.stat_humidity)).setText(app.getHumidity() != null ? app.getHumidity() : "--%");
        
        String windInfo = (app.getWindDirection() != null ? app.getWindDirection() + " " : "") + 
                         (app.getWind() != null ? app.getWind() : "-- mph");
        ((TextView) findViewById(R.id.stat_wind)).setText(windInfo);
        
        ((TextView) findViewById(R.id.stat_feels)).setText(app.getFeelsLike() != null ? app.getFeelsLike() : "--°F");
        ((TextView) findViewById(R.id.stat_pressure)).setText("29.92"); // Static fallback for now
        
        int condition = app.getCurrentCondition();
        if (condition > 0) {
            ((ImageView) findViewById(R.id.current_weather_icon)).setImageResource(UIUtils.getWeatherIconResource(condition));
        }
    }
}
