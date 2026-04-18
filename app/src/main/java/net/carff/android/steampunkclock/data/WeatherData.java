package net.carff.android.steampunkclock.data;
/* loaded from: classes.dex */
public class WeatherData {
    private int currentCondition;
    private String city = null;
    private String zip = null;
    private String day = null;
    private int tempF = 0;
    private String tempC = null;
    private String humidity = null;
    private String currentIcon = null;
    private String wind = null;
    private int forecast_counter = 0;

    public void setCity(String city) {
        this.city = city;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public void setCurrentCondition(int currentCondition) {
        this.currentCondition = currentCondition;
    }

    public void setTempF(int tempF) {
        this.tempF = tempF;
    }

    public void setTempC(String tempC) {
        this.tempC = tempC;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public void setForecastCounter(int counter) {
        this.forecast_counter = counter;
    }

    public String getCity() {
        return this.city;
    }

    public String getZip() {
        return this.zip;
    }

    public int getCurrentCondition() {
        return this.currentCondition;
    }

    public int getTempF() {
        return this.tempF;
    }

    public String getTempC() {
        return this.tempC;
    }

    public String getHumidity() {
        return this.humidity;
    }

    public String getDay() {
        return this.day;
    }

    public String getCurrentIcon() {
        return this.currentIcon;
    }

    public String getWind() {
        return this.wind;
    }

    public int getForecastCounter() {
        return this.forecast_counter;
    }

    public void incrementCounter() {
        this.forecast_counter++;
    }
}
