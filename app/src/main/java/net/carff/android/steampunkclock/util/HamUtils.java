package net.carff.android.steampunkclock.util;

import java.util.Calendar;
import java.util.Date;

public class HamUtils {

    /**
     * Converts Latitude and Longitude to a 6-digit Maidenhead Grid Square.
     */
    public static String toMaidenhead(double lat, double lon) {
        lon += 180;
        lat += 90;

        String upper = "ABCDEFGHIJKLMNOPQRSTUVWX";
        String lower = "abcdefghijklmnopqrstuvwx";

        char f1 = upper.charAt((int) (lon / 20));
        char f2 = upper.charAt((int) (lat / 10));
        
        int s1 = (int) ((lon % 20) / 2);
        int s2 = (int) (lat % 10);
        
        char t1 = lower.charAt((int) ((lon % 2) * 12));
        char t2 = lower.charAt((int) ((lat % 1) * 24));

        return "" + f1 + f2 + s1 + s2 + t1 + t2;
    }

    /**
     * Calculates the Solar Declination for a given date.
     * Essential for drawing the Day/Night terminator.
     */
    public static double getSolarDeclination(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayOfYear = cal.get(Calendar.DAY_OF_YEAR);
        
        // Simple approximation: declination = 23.45 * sin(360/365 * (284 + n))
        return 23.45 * Math.sin(Math.toRadians((360.0 / 365.0) * (284 + dayOfYear)));
    }

    /**
     * Calculates the Subsolar Point Longitude.
     */
    public static double getSubsolarLongitude(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        double utcHours = cal.get(Calendar.HOUR_OF_DAY) + 
                          cal.get(Calendar.MINUTE) / 60.0 + 
                          cal.get(Calendar.SECOND) / 3600.0;
        
        // Approx: 15 degrees per hour from noon UTC
        return (12.0 - utcHours) * 15.0;
    }
}
