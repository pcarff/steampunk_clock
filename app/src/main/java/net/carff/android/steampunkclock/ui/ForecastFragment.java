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

    public void updateDisplay() {
        getActivity().runOnUiThread(new Runnable() { // from class: net.carff.android.steampunkclock.ui.ForecastFragment.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    int day1LoTemp = ((SteampunkClockApplication) ForecastFragment.this.getActivity().getApplication()).getLow(1);
                    int day2LoTemp = ((SteampunkClockApplication) ForecastFragment.this.getActivity().getApplication()).getLow(2);
                    int day3LoTemp = ((SteampunkClockApplication) ForecastFragment.this.getActivity().getApplication()).getLow(3);
                    int day1HiTemp = ((SteampunkClockApplication) ForecastFragment.this.getActivity().getApplication()).getHigh(1);
                    int day2HiTemp = ((SteampunkClockApplication) ForecastFragment.this.getActivity().getApplication()).getHigh(2);
                    int day3HiTemp = ((SteampunkClockApplication) ForecastFragment.this.getActivity().getApplication()).getHigh(3);
                    String oneDayTitle = ((SteampunkClockApplication) ForecastFragment.this.getActivity().getApplication()).getDayOfWeek(1);
                    ForecastFragment.this.dayOneTitle.setText(oneDayTitle);
                    String twoDayTitle = ((SteampunkClockApplication) ForecastFragment.this.getActivity().getApplication()).getDayOfWeek(2);
                    ForecastFragment.this.dayTwoTitle.setText(twoDayTitle);
                    String threeDayTitle = ((SteampunkClockApplication) ForecastFragment.this.getActivity().getApplication()).getDayOfWeek(3);
                    ForecastFragment.this.dayThreeTitle.setText(threeDayTitle);
                    TextView textView = ForecastFragment.this.dayOneTemp;
                    textView.setText(day1LoTemp + "/" + day1HiTemp);
                    TextView textView2 = ForecastFragment.this.dayTwoTemp;
                    textView2.setText(day2LoTemp + "/" + day2HiTemp);
                    TextView textView3 = ForecastFragment.this.dayThreeTemp;
                    textView3.setText(day3LoTemp + "/" + day3HiTemp);
                    int oneDay = ((SteampunkClockApplication) ForecastFragment.this.getActivity().getApplication()).getCondition(1);
                    int twoDay = ((SteampunkClockApplication) ForecastFragment.this.getActivity().getApplication()).getCondition(2);
                    int threeDay = ((SteampunkClockApplication) ForecastFragment.this.getActivity().getApplication()).getCondition(3);
                    ForecastFragment.this.oneDayForecastIcon.setImageResource(UIUtils.getWeatherIconResource(oneDay));
                    ForecastFragment.this.twoDayForecastIcon.setImageResource(UIUtils.getWeatherIconResource(twoDay));
                    ForecastFragment.this.threeDayForecastIcon.setImageResource(UIUtils.getWeatherIconResource(threeDay));
                } catch (Exception e) {
                }
            }
        });
    }
}
