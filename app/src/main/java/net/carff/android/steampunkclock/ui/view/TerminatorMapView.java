package net.carff.android.steampunkclock.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import net.carff.android.steampunkclock.util.HamUtils;
import java.util.Date;

public class TerminatorMapView extends View {
    private Paint mapPaint;
    private Paint nightPaint;
    private Paint linePaint;
    private Paint markerPaint;
    private double userLat = 0;
    private double userLon = 0;

    public TerminatorMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mapPaint = new Paint();
        mapPaint.setColor(Color.parseColor("#1f2937"));
        mapPaint.setStyle(Paint.Style.FILL);

        nightPaint = new Paint();
        nightPaint.setColor(Color.parseColor("#44FF6600")); // Orange-tinted night
        nightPaint.setStyle(Paint.Style.FILL);

        linePaint = new Paint();
        linePaint.setColor(Color.parseColor("#FF6600"));
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(2f);

        markerPaint = new Paint();
        markerPaint.setColor(Color.YELLOW);
        markerPaint.setStyle(Paint.Style.FILL);
    }

    public void setUserLocation(double lat, double lon) {
        this.userLat = lat;
        this.userLon = lon;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = getWidth();
        int h = getHeight();

        // 1. Draw Background (Sea)
        canvas.drawColor(Color.parseColor("#0d1117"));

        // 2. Draw Simplified Landmasses (Geometric approximations)
        drawLand(canvas, w, h);

        // 3. Draw Day/Night Terminator
        drawTerminator(canvas, w, h);

        // 4. Draw User Marker
        float x = (float) ((userLon + 180) / 360.0 * w);
        float y = (float) ((90 - userLat) / 180.0 * h);
        canvas.drawCircle(x, y, 8, markerPaint);
    }

    private void drawLand(Canvas canvas, int w, int h) {
        // North America / South America approx
        Path path = new Path();
        path.moveTo(0.1f * w, 0.2f * h);
        path.lineTo(0.3f * w, 0.2f * h);
        path.lineTo(0.4f * w, 0.8f * h);
        path.lineTo(0.2f * w, 0.9f * h);
        path.close();
        canvas.drawPath(path, mapPaint);

        // Eurasia / Africa approx
        path = new Path();
        path.moveTo(0.5f * w, 0.2f * h);
        path.lineTo(0.8f * w, 0.1f * h);
        path.lineTo(0.9f * w, 0.6f * h);
        path.lineTo(0.6f * w, 0.8f * h);
        path.close();
        canvas.drawPath(path, mapPaint);
    }

    private void drawTerminator(Canvas canvas, int w, int h) {
        Date now = new Date();
        double dec = HamUtils.getSolarDeclination(now);
        double sunLon = HamUtils.getSubsolarLongitude(now);

        Path termPath = new Path();
        boolean first = true;

        for (int lon = -180; lon <= 180; lon += 5) {
            double lat = Math.toDegrees(Math.atan(-Math.cos(Math.toRadians(lon - sunLon)) / Math.tan(Math.toRadians(dec))));
            float x = (float) ((lon + 180) / 360.0 * w);
            float y = (float) ((90 - lat) / 180.0 * h);

            if (first) {
                termPath.moveTo(x, y);
                first = false;
            } else {
                termPath.lineTo(x, y);
            }
        }

        // Close the path based on hemisphere to fill the "night" side
        if (dec > 0) {
            termPath.lineTo(w, h);
            termPath.lineTo(0, h);
        } else {
            termPath.lineTo(w, 0);
            termPath.lineTo(0, 0);
        }
        termPath.close();

        canvas.drawPath(termPath, nightPaint);
        canvas.drawPath(termPath, linePaint);
    }
}
