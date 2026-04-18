package net.carff.android.steampunkclock.ui;

import androidx.fragment.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import net.carff.android.steampunkclock.R;
import net.carff.android.steampunkclock.SteampunkClockApplication;
import net.carff.android.steampunkclock.util.UIUtils;
/* loaded from: classes.dex */
public class ForecastFragment extends Fragment {
    private static String TAG = "ForecastFragment";
    private TextView dayOneTemp;
    private TextView dayOneTitle;
    private TextView dayThreeTemp;
    private TextView dayThreeTitle;
    private TextView dayTwoTemp;
    private TextView dayTwoTitle;
    private String font = "duvall.ttf";
    private TextView forecastText;
    private ImageView oneDayForecastIcon;
    private ImageView threeDayForecastIcon;
    private ImageView twoDayForecastIcon;

    @Override // android.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Runnable runnable = new ForecastRunner();
        Thread myThread = new Thread(runnable);
        myThread.start();
    }

    @Override // android.app.Fragment
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override // android.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.forecast, container, false);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), this.font);
        TextView textView = (TextView) view.findViewById(R.id.forecast_title);
        this.forecastText = textView;
        textView.setTextSize(20.0f);
        this.forecastText.setTypeface(tf);
        this.dayOneTemp = (TextView) view.findViewById(R.id.day_one_temp);
        this.dayTwoTemp = (TextView) view.findViewById(R.id.day_two_temp);
        this.dayThreeTemp = (TextView) view.findViewById(R.id.day_three_temp);
        this.dayOneTemp.setTypeface(tf);
        this.dayTwoTemp.setTypeface(tf);
        this.dayThreeTemp.setTypeface(tf);
        this.dayOneTitle = (TextView) view.findViewById(R.id.one_day_title);
        this.dayTwoTitle = (TextView) view.findViewById(R.id.two_day_title);
        this.dayThreeTitle = (TextView) view.findViewById(R.id.three_day_title);
        this.dayOneTitle.setTypeface(tf);
        this.dayTwoTitle.setTypeface(tf);
        this.dayThreeTitle.setTypeface(tf);
        this.oneDayForecastIcon = (ImageView) view.findViewById(R.id.one_day_forecast);
        this.twoDayForecastIcon = (ImageView) view.findViewById(R.id.two_day_forecast);
        this.threeDayForecastIcon = (ImageView) view.findViewById(R.id.three_day_forecast);
        return view;
    }

    /* loaded from: classes.dex */
    class ForecastRunner implements Runnable {
        ForecastRunner() {
        }

        @Override // java.lang.Runnable
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    ForecastFragment.this.updateDisplay();
                    Thread.sleep(10000L);
                } catch (Exception e) {
                }
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void updateDisplay() {
        if (getActivity() == null) return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (getActivity() == null) return;
                    SteampunkClockApplication app = (SteampunkClockApplication) getActivity().getApplication();
                    
                    // Forecast usually has 4+ days (0=today, 1=tomorrow, 2=next, 3=next)
                    // We check if we have enough data
                    if (app == null) return;
                    
                    // Safety: find how many days we have
                    // In a real app we'd expose size, but here we'll just try/catch carefully
                    
                    updateDay(app, 1, dayOneTitle, dayOneTemp, oneDayForecastIcon);
                    updateDay(app, 2, dayTwoTitle, dayTwoTemp, twoDayForecastIcon);
                    updateDay(app, 3, dayThreeTitle, dayThreeTemp, threeDayForecastIcon);
                } catch (Exception e) {}
            }
        });
    }

    private void updateDay(SteampunkClockApplication app, int index, TextView title, TextView temp, ImageView icon) {
        try {
            String dayTitle = app.getDayOfWeek(index);
            int low = app.getLow(index);
            int high = app.getHigh(index);
            int cond = app.getCondition(index);
            
            title.setText(dayTitle != null ? dayTitle.toUpperCase() : "---");
            temp.setText(low + "/" + high);
            icon.setImageResource(UIUtils.getWeatherIconResource(cond));
            icon.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            icon.setVisibility(View.INVISIBLE);
        }
    }
}
