package net.carff.android.steampunkclock.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import java.util.Arrays;
import java.util.Calendar;
import net.carff.android.steampunkclock.R;
import net.carff.android.steampunkclock.util.UIUtils;
/* loaded from: classes.dex */
public final class SteampunkClockDateWidgetProvider extends AppWidgetProvider {
    private static int mDay;
    private static String mDayOfWeek;
    private static int mMonth;
    private static int mYear;
    private static String TAG = "SteampunkClockDateWidgetProvider";
    private static String DATE_WIDGET_UPDATE = "net.carff.android.STEAMPUNKCLOCK_DATE_WIDGET_UPDATE";

    @Override // android.appwidget.AppWidgetProvider
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override // android.appwidget.AppWidgetProvider
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override // android.appwidget.AppWidgetProvider
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.i(TAG, "Updating widgets " + Arrays.asList(appWidgetIds));
        for (int appWidgetId : appWidgetIds) {
            Intent intent = new Intent(context, SteampunkClockDateWidgetProvider.class);
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
            RemoteViews views = new RemoteViews(context.getPackageName(), (int) R.layout.date_widget);
            updateDateDisplay(views);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
        new ComponentName(context, SteampunkClockDateWidgetProvider.class);
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override // android.appwidget.AppWidgetProvider, android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d(TAG, "Received intent " + intent);
        if (DATE_WIDGET_UPDATE.equals(intent.getAction())) {
            Log.d(TAG, "Clock Update");
            ComponentName thisAppWidget = new ComponentName(context.getPackageName(), getClass().getName());
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] ids = appWidgetManager.getAppWidgetIds(thisAppWidget);
            for (int appWidgetID : ids) {
                updateAppWidget(context, appWidgetManager, appWidgetID);
            }
        }
    }

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        String str = TAG;
        Log.d(str, "Updating widgetId " + appWidgetId);
        RemoteViews updateViews = new RemoteViews(context.getPackageName(), (int) R.layout.date_widget);
        updateDateDisplay(updateViews);
        appWidgetManager.updateAppWidget(appWidgetId, updateViews);
    }

    public static void updateDateDisplay(RemoteViews rv) {
        Calendar cal = Calendar.getInstance();
        mMonth = cal.get(2) + 1;
        mDay = cal.get(5);
        mDayOfWeek = UIUtils.getDayOfWeek(cal.get(7));
        mYear = cal.get(1);
        int i = mMonth;
        int highMonth = i / 10;
        int lowMonth = i % 10;
        rv.setImageViewResource(R.id.high_month, UIUtils.getNumberResource(highMonth));
        rv.setImageViewResource(R.id.low_month, UIUtils.getNumberResource(lowMonth));
        int i2 = mDay;
        int highDay = i2 / 10;
        int lowDay = i2 % 10;
        rv.setImageViewResource(R.id.high_day, UIUtils.getNumberResource(highDay));
        rv.setImageViewResource(R.id.low_day, UIUtils.getNumberResource(lowDay));
        int i3 = mYear;
        int highYear = (i3 - 2000) / 10;
        int lowYear = i3 % 10;
        rv.setImageViewResource(R.id.high_year, UIUtils.getNumberResource(highYear));
        rv.setImageViewResource(R.id.low_year, UIUtils.getNumberResource(lowYear));
    }
}
