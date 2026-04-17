package net.carff.android.steampunkclock.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import java.util.Arrays;
import java.util.Calendar;
import net.carff.android.steampunkclock.R;
import net.carff.android.steampunkclock.util.UIUtils;
/* loaded from: classes.dex */
public class SteampunkClockTimeWidgetProvider extends AppWidgetProvider {
    private static String TAG = "SteampunkTimeidgetProvider";
    private static String TIME_WIDGET_UPDATE = "net.carff.android.STEAMPUNKCLOCK_TIME_WIDGET_UPDATE";
    private static int mAmPm;
    private static int mHour;
    private static int mMinute;
    private static int mSecond;

    @Override // android.appwidget.AppWidgetProvider
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.d(TAG, "Widget Provider enabled.  Starting timer to update widger every second");
        AlarmManager am = (AlarmManager) context.getSystemService(NotificationCompat.CATEGORY_ALARM);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.add(13, 1);
        am.setRepeating(0, cal.getTimeInMillis(), 1000L, createClockTickIntent(context));
    }

    @Override // android.appwidget.AppWidgetProvider
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Log.d(TAG, "Widget Provider disabled.  Turner off timer");
        AlarmManager am = (AlarmManager) context.getSystemService(NotificationCompat.CATEGORY_ALARM);
        am.cancel(createClockTickIntent(context));
    }

    @Override // android.appwidget.AppWidgetProvider
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.i(TAG, "Updating widgets " + Arrays.asList(appWidgetIds));
        for (int appWidgetId : appWidgetIds) {
            Intent intent = new Intent(context, SteampunkClockTimeWidgetProvider.class);
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
            RemoteViews views = new RemoteViews(context.getPackageName(), (int) R.layout.time_widget);
            updateTimeDisplay(views);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
        new ComponentName(context, SteampunkClockTimeWidgetProvider.class);
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private PendingIntent createClockTickIntent(Context context) {
        Intent intent = new Intent(TIME_WIDGET_UPDATE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        return pendingIntent;
    }

    @Override // android.appwidget.AppWidgetProvider, android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d(TAG, "Received intent " + intent);
        if (TIME_WIDGET_UPDATE.equals(intent.getAction())) {
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
        RemoteViews updateViews = new RemoteViews(context.getPackageName(), (int) R.layout.time_widget);
        updateTimeDisplay(updateViews);
        appWidgetManager.updateAppWidget(appWidgetId, updateViews);
    }

    public static void updateTimeDisplay(RemoteViews rv) {
        Calendar cal = Calendar.getInstance();
        mHour = cal.get(10);
        mMinute = cal.get(12);
        mSecond = cal.get(13);
        int i = cal.get(9);
        mAmPm = i;
        if (i == 1) {
            mHour += 12;
        }
        int i2 = mHour;
        int highHour = i2 / 10;
        int lowHour = i2 % 10;
        rv.setImageViewResource(R.id.high_hour, UIUtils.getNumberResource(highHour));
        rv.setImageViewResource(R.id.low_hour, UIUtils.getNumberResource(lowHour));
        int i3 = mMinute;
        int highMinute = i3 / 10;
        int lowMinute = i3 % 10;
        rv.setImageViewResource(R.id.high_minute, UIUtils.getNumberResource(highMinute));
        rv.setImageViewResource(R.id.low_minute, UIUtils.getNumberResource(lowMinute));
        int i4 = mSecond;
        int highSecond = i4 / 10;
        int lowSecond = i4 % 10;
        rv.setImageViewResource(R.id.high_second, UIUtils.getNumberResource(highSecond));
        rv.setImageViewResource(R.id.low_second, UIUtils.getNumberResource(lowSecond));
    }
}
