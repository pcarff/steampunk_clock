package net.carff.android.steampunkclock.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

public class SignalTrendView extends View {
    private Paint linePaint;
    private Paint gridPaint;
    private List<Float> history = new ArrayList<>();
    private static final int MAX_POINTS = 30;

    public SignalTrendView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        linePaint = new Paint();
        linePaint.setColor(Color.parseColor("#ffdd00"));
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(4f);
        linePaint.setAntiAlias(true);

        gridPaint = new Paint();
        gridPaint.setColor(Color.parseColor("#22ffffff"));
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setStrokeWidth(1f);
    }

    public void addDataPoint(float snr) {
        history.add(snr);
        if (history.size() > MAX_POINTS) {
            history.remove(0);
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = getWidth();
        int h = getHeight();

        // 1. Draw Background
        canvas.drawColor(Color.parseColor("#050505"));

        // 2. Draw Grid
        for (int i = 0; i <= 4; i++) {
            float y = (h / 4f) * i;
            canvas.drawLine(0, y, w, y, gridPaint);
        }

        if (history.size() < 2) return;

        // 3. Draw Trendline
        Path path = new Path();
        float xStep = (float) w / (MAX_POINTS - 1);
        
        for (int i = 0; i < history.size(); i++) {
            float x = i * xStep;
            float y = h - (history.get(i) / 50f) * h; // Assuming 0-50 SNR range

            if (i == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
        }

        // Apply Gradient
        linePaint.setShader(new LinearGradient(0, 0, 0, h, 
            Color.parseColor("#ffdd00"), Color.parseColor("#ff6600"), Shader.TileMode.CLAMP));
        
        canvas.drawPath(path, linePaint);
    }
}
