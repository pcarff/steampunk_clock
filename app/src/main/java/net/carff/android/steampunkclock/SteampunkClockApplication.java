package net.carff.android.steampunkclock;

import android.app.Application;
import java.util.ArrayList;
import net.carff.android.steampunkclock.data.ForecastData;
import net.carff.android.steampunkclock.data.WeatherData;
/* loaded from: classes.dex */
public class SteampunkClockApplication extends Application {
    private ArrayList<ForecastData> mForecastData;
    private WeatherData mWeatherData;
    private boolean twelveHourMode = true;

    @Override // android.app.Application
    public void onCreate() {
        super.onCreate();
        this.mWeatherData = new WeatherData();
        this.mForecastData = new ArrayList<>();
        this.mWeatherData.setCity("pending");
    }

    public void setCity(String c) {
        this.mWeatherData.setCity(c);
    }

    public String getCity() {
        return this.mWeatherData.getCity();
    }

    public void setTempF(int t) {
        this.mWeatherData.setTempF(t);
    }

    public int getTempF() {
        return this.mWeatherData.getTempF();
    }

    public void setDay(String d) {
        this.mWeatherData.setDay(d);
    }

    public String getDay() {
        return this.mWeatherData.getDay();
    }

    public void setCurrentCondition(int cc) {
        this.mWeatherData.setCurrentCondition(cc);
    }

    public int getCurrentCondition() {
        return this.mWeatherData.getCurrentCondition();
    }

    public void setHumidity(String h) { this.mWeatherData.setHumidity(h); }
    public String getHumidity() { return this.mWeatherData.getHumidity(); }

    public void setWind(String w) { this.mWeatherData.setWind(w); }
    public String getWind() { return this.mWeatherData.getWind(); }

    public void setWindDirection(String wd) { this.mWeatherData.setWindDirection(wd); }
    public String getWindDirection() { return this.mWeatherData.getWindDirection(); }

    public void setFeelsLike(String f) { this.mWeatherData.setFeelsLike(f); }
    public String getFeelsLike() { return this.mWeatherData.getFeelsLike(); }

    public void setPressure(String p) { this.mWeatherData.setPressure(p); }
    public String getPressure() { return this.mWeatherData.getPressure(); }

    public void setUvIndex(String u) { this.mWeatherData.setUvIndex(u); }
    public String getUvIndex() { return this.mWeatherData.getUvIndex(); }

    public void addForecast() {
        this.mForecastData.add(new ForecastData());
    }

    public void clearForecast() {
        this.mForecastData.clear();
    }

    public void setHigh(int counter, int th) {
        this.mForecastData.get(counter).setHigh(th);
    }

    public void setLow(int counter, int tl) {
        this.mForecastData.get(counter).setLow(tl);
    }

    public void setCondition(int counter, int c) {
        this.mForecastData.get(counter).setCondition(c);
    }

    public int getHigh(int counter) {
        return this.mForecastData.get(counter).getHigh();
    }

    public int getLow(int counter) {
        return this.mForecastData.get(counter).getLow();
    }

    public int getCondition(int counter) {
        return this.mForecastData.get(counter).getCondition();
    }

    public void setDayOfWeek(int counter, String d) {
        this.mForecastData.get(counter).setDayOfWeek(d);
    }

    public String getDayOfWeek(int counter) {
        return this.mForecastData.get(counter).getDayOfWeek();
    }

    public void setTwelveHour(boolean t) {
        this.twelveHourMode = t;
    }

    public boolean getTwelveHour() {
        return this.twelveHourMode;
    }
}
