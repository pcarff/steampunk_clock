package net.carff.android.steampunkclock.service;

import android.app.AlarmManager;
import android.app.Service;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import net.carff.android.steampunkclock.BuildConfig;
import net.carff.android.steampunkclock.R;
import net.carff.android.steampunkclock.SteampunkClockApplication;
import net.carff.android.steampunkclock.util.UIUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
/* loaded from: classes.dex */
public class WeatherDataFetchService extends Service {
    private static final String TAG = WeatherDataFetchService.class.getSimpleName();
    private static final String ns = null;
    private Address address;
    private AlarmManager alarm;
    private Geocoder geocoder;
    private DataFetcherTask mDFT;
    LocationManager mLocMgr;
    private Location myLocation;
    private String provider;
    private String zipCode;

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int flags, int startId) {
        getApplicationContext();
        this.zipCode = getZipCode();
        Log.d(TAG, "Zip = " + this.zipCode);
        String fetchUrl = "";
        if (this.myLocation != null) {
            fetchUrl = "https://api.open-meteo.com/v1/forecast?latitude=" + this.myLocation.getLatitude() + "&longitude=" + this.myLocation.getLongitude() + "&current_weather=true&daily=weathercode,temperature_2m_max,temperature_2m_min&temperature_unit=fahrenheit&timezone=auto";
        } else if (!this.zipCode.equals("00000")) {
            // Fallback if zipCode is known but location is null (though getZipCode sets location)
            // For now, we prefer myLocation directly
            fetchUrl = getString(R.string.api_url_front) + getString(R.string.api_url_key) + getString(R.string.api_url_middle) + this.zipCode + getString(R.string.api_url_end);
        }
        try {
            String url = fetchUrl;
            Log.d(TAG, "url " + url);
            if (url != null && url.length() > 0) {
                URL downloadPath = new URL(url);
                DataFetcherTask dataFetcherTask = new DataFetcherTask();
                this.mDFT = dataFetcherTask;
                dataFetcherTask.execute(downloadPath);
            }
        } catch (MalformedURLException e) {
            Log.e(TAG, "Bad URL ", e);
        } catch (Exception e2) {
            Log.e(TAG, "Problem fetching data " + e2);
        }
        return 1;
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    /* loaded from: classes.dex */
    private class DataFetcherTask extends AsyncTask<URL, Void, Boolean> {
        private static final String DEBUG_TAG = "WearherDataFetcherService$DataFetcherTask";

        private DataFetcherTask() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Boolean doInBackground(URL... params) {
            boolean succeeded = false;
            URL downloadPath = params[0];
            String str = WeatherDataFetchService.TAG;
            Log.d(str, "URL: " + downloadPath);
            if (downloadPath != null) {
                if (downloadPath.toString().contains("open-meteo.com")) {
                    succeeded = jsonParse(downloadPath);
                } else {
                    succeeded = xmlParse(downloadPath);
                }
            }
            return Boolean.valueOf(succeeded);
        }

        private boolean jsonParse(URL downloadPath) {
            Log.d(WeatherDataFetchService.TAG, "Starting to Parse JSON");
            try {
                HttpURLConnection conn = (HttpURLConnection) downloadPath.openConnection();
                conn.setRequestMethod("GET");
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                reader.close();

                JSONObject json = new JSONObject(sb.toString());
                SteampunkClockApplication app = (SteampunkClockApplication) WeatherDataFetchService.this.getApplication();
                app.clearForecast();

                // Current weather
                if (json.has("current_weather")) {
                    JSONObject current = json.getJSONObject("current_weather");
                    app.setTempF((int) Math.round(current.getDouble("temperature")));
                    app.setCurrentCondition(mapWmoToWwo(current.getInt("weathercode")));
                    Log.d(WeatherDataFetchService.TAG, "Current temp: " + app.getTempF() + ", code: " + app.getCurrentCondition());
                }

                // Forecast
                if (json.has("daily")) {
                    JSONObject daily = json.getJSONObject("daily");
                    JSONArray timeArr = daily.getJSONArray("time");
                    JSONArray codeArr = daily.getJSONArray("weathercode");
                    JSONArray maxTempArr = daily.getJSONArray("temperature_2m_max");
                    JSONArray minTempArr = daily.getJSONArray("temperature_2m_min");

                    for (int i = 0; i < timeArr.length() && i < 4; i++) {
                        app.addForecast();
                        String dateStr = timeArr.getString(i);
                        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
                        SimpleDateFormat df = new SimpleDateFormat("E");
                        
                        if (i == 0) {
                            app.setDay(df.format(date));
                        }
                        app.setDayOfWeek(i, df.format(date));
                        app.setHigh(i, (int) Math.round(maxTempArr.getDouble(i)));
                        app.setLow(i, (int) Math.round(minTempArr.getDouble(i)));
                        app.setCondition(i, mapWmoToWwo(codeArr.getInt(i)));
                    }
                }
                return true;
            } catch (Exception e) {
                Log.e(WeatherDataFetchService.TAG, "Error during JSON parsing ", e);
            }
            return false;
        }

        private int mapWmoToWwo(int wmoCode) {
            if (wmoCode == 0) return 113;
            if (wmoCode >= 1 && wmoCode <= 3) return 116;
            if (wmoCode == 45 || wmoCode == 48) return 248;
            if (wmoCode >= 51 && wmoCode <= 55) return 266;
            if (wmoCode >= 61 && wmoCode <= 65) return 302;
            if (wmoCode >= 71 && wmoCode <= 75) return 332;
            if (wmoCode >= 80 && wmoCode <= 82) return 356;
            if (wmoCode >= 95) return 389;
            return 113;
        }

        private boolean xmlParse(URL downloadPath) {
            boolean currentCondition = false;
            boolean inWeather = false;
            Log.d(WeatherDataFetchService.TAG, "Starting to Parse");
            try {
                SteampunkClockApplication app = (SteampunkClockApplication) WeatherDataFetchService.this.getApplication();
                app.clearForecast();
                XmlPullParser weatherStream = XmlPullParserFactory.newInstance().newPullParser();
                weatherStream.setInput(downloadPath.openStream(), null);
                int forecastCounter = 0;
                for (int eventType = -1; eventType != 1; eventType = weatherStream.next()) {
                    if (eventType == 0) {
                        Log.d(WeatherDataFetchService.TAG, "START_DOCUMENT");
                    } else if (eventType == 2) {
                        String tagName = weatherStream.getName();
                        if (tagName.equals("areaName")) {
                            weatherStream.next();
                            ((SteampunkClockApplication) WeatherDataFetchService.this.getApplication()).setCity(weatherStream.getText());
                            String str = WeatherDataFetchService.TAG;
                            Log.d(str, "City from app: " + ((SteampunkClockApplication) WeatherDataFetchService.this.getApplication()).getCity());
                        } else if (tagName.equals("current_condition")) {
                            currentCondition = true;
                        } else if (tagName.equals("weather")) {
                            inWeather = true;
                            Log.d(WeatherDataFetchService.TAG, "Setting inWeather");
                            ((SteampunkClockApplication) WeatherDataFetchService.this.getApplication()).addForecast();
                        } else if (tagName.equals("temp_F")) {
                            weatherStream.next();
                            ((SteampunkClockApplication) WeatherDataFetchService.this.getApplication()).setTempF(Integer.valueOf(weatherStream.getText()).intValue());
                            String str2 = WeatherDataFetchService.TAG;
                            Log.d(str2, "temp f " + ((SteampunkClockApplication) WeatherDataFetchService.this.getApplication()).getTempF());
                        } else if (tagName.equals("weatherCode") && currentCondition) {
                            weatherStream.next();
                            ((SteampunkClockApplication) WeatherDataFetchService.this.getApplication()).setCurrentCondition(Integer.valueOf(weatherStream.getText()).intValue());
                            String str3 = WeatherDataFetchService.TAG;
                            Log.d(str3, "Current Weather Code " + ((SteampunkClockApplication) WeatherDataFetchService.this.getApplication()).getCurrentCondition());
                            currentCondition = false;
                        } else if (tagName.equals("date")) {
                            weatherStream.next();
                            Date date = null;
                            SimpleDateFormat df = null;
                            try {
                                Log.d(WeatherDataFetchService.TAG, "trying to get date");
                                String str4 = WeatherDataFetchService.TAG;
                                Log.d(str4, "forecastCounter: " + forecastCounter);
                                date = new SimpleDateFormat("yyyy-MM-dd").parse(weatherStream.getText());
                                df = new SimpleDateFormat("E");
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            String str5 = WeatherDataFetchService.TAG;
                            Log.d(str5, "date: " + date.toString());
                            String str6 = WeatherDataFetchService.TAG;
                            Log.d(str6, "dateFormat: " + df.format(date));
                            if (forecastCounter == 0) {
                                ((SteampunkClockApplication) WeatherDataFetchService.this.getApplication()).setDay(df.format(date));
                            }
                            ((SteampunkClockApplication) WeatherDataFetchService.this.getApplication()).setDayOfWeek(forecastCounter, df.format(date));
                        } else if (tagName.equals("tempMaxF")) {
                            weatherStream.next();
                            ((SteampunkClockApplication) WeatherDataFetchService.this.getApplication()).setHigh(forecastCounter, Integer.valueOf(weatherStream.getText()).intValue());
                        } else if (tagName.equals("tempMinF")) {
                            weatherStream.next();
                            ((SteampunkClockApplication) WeatherDataFetchService.this.getApplication()).setLow(forecastCounter, Integer.valueOf(weatherStream.getText()).intValue());
                        } else if (tagName.equals("weatherCode") && inWeather) {
                            weatherStream.next();
                            ((SteampunkClockApplication) WeatherDataFetchService.this.getApplication()).setCondition(forecastCounter, Integer.valueOf(weatherStream.getText()).intValue());
                            Log.d(WeatherDataFetchService.TAG, "incrementing counter");
                            forecastCounter++;
                            Log.d(WeatherDataFetchService.TAG, "Clearing inWeather");
                            inWeather = false;
                        }
                    }
                }
            } catch (IOException e2) {
                Log.e(WeatherDataFetchService.TAG, "IO Error during parsing ", e2);
            } catch (XmlPullParserException e3) {
                Log.e(WeatherDataFetchService.TAG, "Error during parsing ", e3);
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Boolean result) {
        }
    }

    private String getZipCode() {
        try {
            this.mLocMgr = (LocationManager) getSystemService("location");
            Criteria criteria = new Criteria();
            this.provider = this.mLocMgr.getBestProvider(criteria, false);
            String str = TAG;
            Log.d(str, "Got Provider " + this.provider);
            String str2 = TAG;
            Log.d(str2, "Is Google TV? " + UIUtils.isGoogleTV(this));
            if (UIUtils.isGoogleTV(this)) {
                this.myLocation = this.mLocMgr.getLastKnownLocation("static");
            } else if (this.provider != null) {
                this.myLocation = this.mLocMgr.getLastKnownLocation(this.provider);
            }
            Geocoder geocoder = new Geocoder(this);
            this.geocoder = geocoder;
            Address address = geocoder.getFromLocation(this.myLocation.getLatitude(), this.myLocation.getLongitude(), 1).get(0);
            this.address = address;
            String city = address.getLocality();
            if (city != null) {
                ((SteampunkClockApplication) getApplication()).setCity(city);
            }
            String zipCode = address.getPostalCode();
            return zipCode;
        } catch (Exception e) {
            String str3 = TAG;
            Log.e(str3, "Exception: " + e);
            return "00000";
        }
    }
}
