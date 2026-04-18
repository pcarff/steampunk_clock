package net.carff.android.steampunkclock.data;
/* loaded from: classes.dex */
public class ForecastData {
    private int condition;
    private String dayOfWeek = null;
    private int low = 0;
    private int high = 0;

    public void setDayOfWeek(String dow) {
        this.dayOfWeek = dow;
    }

    public void setLow(int low) {
        this.low = low;
    }

    public void setHigh(int high) {
        this.high = high;
    }

    public void setCondition(int condition) {
        this.condition = condition;
    }

    public String getDayOfWeek() {
        return this.dayOfWeek;
    }

    public int getLow() {
        return this.low;
    }

    public int getHigh() {
        return this.high;
    }

    public int getCondition() {
        return this.condition;
    }
}
