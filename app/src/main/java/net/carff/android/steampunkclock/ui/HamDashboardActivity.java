package net.carff.android.steampunkclock.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.GnssStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import java.util.List;
import java.util.Locale;
import net.carff.android.steampunkclock.R;
import net.carff.android.steampunkclock.SteampunkClockApplication;
import net.carff.android.steampunkclock.ui.view.SignalTrendView;
import net.carff.android.steampunkclock.ui.view.TerminatorMapView;
import net.carff.android.steampunkclock.util.HamUtils;

public class HamDashboardActivity extends BaseActivity {
    private LocationManager locationManager;
    private TerminatorMapView terminatorMap;
    private SignalTrendView signalTrend;
    private TextView gridText;
    private TextView latLonText;
    private TextView cityText;
    private TextView tempText;
    private TextView solarText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ham_dashboard);

        gridText = findViewById(R.id.hub_grid_text);
        latLonText = findViewById(R.id.hub_latlon_text);
        cityText = findViewById(R.id.hub_city_text);
        tempText = findViewById(R.id.hub_temp_text);
        solarText = findViewById(R.id.hub_solar_text);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        
        // --- Hub Navigation ---
        findViewById(R.id.btn_weather).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HamDashboardActivity.this, WeatherActivity.class));
            }
        });

        findViewById(R.id.btn_grid).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HamDashboardActivity.this, GpsAnalysisActivity.class));
            }
        });

        findViewById(R.id.btn_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HamDashboardActivity.this, MapActivity.class));
            }
        });

        findViewById(R.id.btn_solar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HamDashboardActivity.this, SolarActivity.class));
            }
        });

        if (checkSelfPermission("android.permission.ACCESS_FINE_LOCATION") != 0) {
            requestPermissions(new String[]{"android.permission.ACCESS_FINE_LOCATION"}, 100);
        } else {
            initGps();
        }
        startSolarUpdates();
        updateWeatherOverview();
        startService(new Intent(this, net.carff.android.steampunkclock.service.WeatherDataFetchService.class));
    }

    private void updateWeatherOverview() {
        // Fetch current city from app state
        String city = ((SteampunkClockApplication) getApplication()).getCity();
        if (city != null) cityText.setText(city.toUpperCase());
        
        // This would eventually fetch real temperature from the weather service
        tempText.setText("21°C"); 
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == 0) {
            initGps();
        }
    }

    private long lastGpsFixTime = 0;
    private static final long GPS_TIMEOUT_MS = 30000; // 30 seconds

    @SuppressLint("MissingPermission")
    private void initGps() {
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                boolean isGps = location.getProvider().equals(LocationManager.GPS_PROVIDER);
                long now = System.currentTimeMillis();
                
                if (isGps) {
                    lastGpsFixTime = now;
                    updateLocation(location);
                } else {
                    // Only use Network if GPS has timed out or never fixed
                    if (now - lastGpsFixTime > GPS_TIMEOUT_MS) {
                        updateLocation(location);
                    }
                }
            }
            @Override public void onStatusChanged(String provider, int status, Bundle extras) {}
            @Override public void onProviderEnabled(String provider) {}
            @Override public void onProviderDisabled(String provider) {}
        };

        // Request from both for hybrid coverage
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 100, locationListener);
        
        // Try to get a quick initial fix from either
        Location lastNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (lastNetwork != null) updateLocation(lastNetwork);
        
        Location lastGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (lastGps != null) {
            lastGpsFixTime = System.currentTimeMillis();
            updateLocation(lastGps);
        }
    }

    private void updateLocation(final Location loc) {
        String grid = HamUtils.toMaidenhead(loc.getLatitude(), loc.getLongitude());
        gridText.setText(grid);
        latLonText.setText(String.format("%.2f, %.2f", loc.getLatitude(), loc.getLongitude()));
        
        // Update City/State via Geocoder
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Geocoder geocoder = new Geocoder(HamDashboardActivity.this, Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
                    if (addresses != null && !addresses.isEmpty()) {
                        final String city = addresses.get(0).getLocality();
                        final String state = addresses.get(0).getAdminArea();
                        final String display = (city != null ? city : "") + (state != null ? ", " + state : "");
                        
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (city != null) {
                                    cityText.setText(display.toUpperCase());
                                    ((SteampunkClockApplication) getApplication()).setCity(city);
                                    
                                    // Trigger weather refresh for new city
                                    startService(new Intent(HamDashboardActivity.this, net.carff.android.steampunkclock.service.WeatherDataFetchService.class));
                                }
                            }
                        });
                    }
                } catch (Exception e) {}
            }
        }).start();
    }

    private void startSolarUpdates() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    final int sfi = 142 + (int)(Math.random() * 5);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            solarText.setText("SFI " + sfi);
                        }
                    });
                    try { Thread.sleep(600000); } catch (Exception e) {}
                }
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
