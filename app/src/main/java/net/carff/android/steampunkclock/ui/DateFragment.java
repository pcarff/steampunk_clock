package net.carff.android.steampunkclock.ui;

import androidx.fragment.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Calendar;
import net.carff.android.steampunkclock.BuildConfig;
import net.carff.android.steampunkclock.R;
import net.carff.android.steampunkclock.util.UIUtils;
/* loaded from: classes.dex */
public class DateFragment extends Fragment {
    private TextView dateText;
    private TextView dayText;
    private String font = "duvall.ttf";
    private ImageView highDayImage;
    private int highDayResource;
    private ImageView highMonthImage;
    private int highMonthResource;
    private ImageView highYearImage;
    private int highYearResource;
    private ImageView lowDayImage;
    private int lowDayResource;
    private ImageView lowMonthImage;
    private int lowMonthResource;
    private ImageView lowYearImage;
    private int lowYearResource;
    private int mDay;
    private String mDayOfWeek;
    private int mMonth;
    private int mYear;
    private TextView monthText;
    private TextView yearText;

    @Override // android.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Runnable runnable = new DateRunner();
        Thread myThread = new Thread(runnable);
        myThread.start();
    }

    @Override // android.app.Fragment
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override // android.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.date, container, false);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), this.font);
        TextView textView = (TextView) view.findViewById(R.id.date_title);
        this.dateText = textView;
        textView.setTextSize(20.0f);
        this.dateText.setTypeface(tf);
        TextView textView2 = (TextView) view.findViewById(R.id.day_title);
        this.dayText = textView2;
        textView2.setTypeface(tf);
        TextView textView3 = (TextView) view.findViewById(R.id.month_title);
        this.monthText = textView3;
        textView3.setTypeface(tf);
        TextView textView4 = (TextView) view.findViewById(R.id.year_title);
        this.yearText = textView4;
        textView4.setTypeface(tf);
        this.highMonthImage = (ImageView) view.findViewById(R.id.high_month);
        this.lowMonthImage = (ImageView) view.findViewById(R.id.low_month);
        this.highMonthImage.setImageResource(this.highMonthResource);
        this.lowMonthImage.setImageResource(this.lowMonthResource);
        this.highDayImage = (ImageView) view.findViewById(R.id.high_day);
        this.lowDayImage = (ImageView) view.findViewById(R.id.low_day);
        this.highDayImage.setImageResource(this.highDayResource);
        this.lowDayImage.setImageResource(this.lowDayResource);
        this.highYearImage = (ImageView) view.findViewById(R.id.high_year);
        this.lowYearImage = (ImageView) view.findViewById(R.id.low_year);
        this.highYearImage.setImageResource(this.highYearResource);
        this.lowYearImage.setImageResource(this.lowYearResource);
        return view;
    }

    public void setText(String item) {
        TextView view = (TextView) getView().findViewById(R.id.date_title);
        view.setText(item);
    }

    public void updateDisplay() {
        getActivity().runOnUiThread(new Runnable() { // from class: net.carff.android.steampunkclock.ui.DateFragment.1
            private Context mContext;

            @Override // java.lang.Runnable
            public void run() {
                try {
                    Calendar cal = Calendar.getInstance();
                    DateFragment.this.mMonth = cal.get(2) + 1;
                    DateFragment.this.mDay = cal.get(5);
                    DateFragment.this.mDayOfWeek = DateFragment.this.getDayOfWeek(cal.get(7));
                    DateFragment.this.mYear = cal.get(1);
                    int highMonth = DateFragment.this.mMonth / 10;
                    int lowMonth = DateFragment.this.mMonth % 10;
                    DateFragment.this.highMonthResource = UIUtils.getNumberResource(highMonth);
                    DateFragment.this.lowMonthResource = UIUtils.getNumberResource(lowMonth);
                    DateFragment.this.highMonthImage.setImageResource(DateFragment.this.highMonthResource);
                    DateFragment.this.lowMonthImage.setImageResource(DateFragment.this.lowMonthResource);
                    int highDay = DateFragment.this.mDay / 10;
                    int lowDay = DateFragment.this.mDay % 10;
                    DateFragment.this.highDayResource = UIUtils.getNumberResource(highDay);
                    DateFragment.this.lowDayResource = UIUtils.getNumberResource(lowDay);
                    DateFragment.this.highDayImage.setImageResource(DateFragment.this.highDayResource);
                    DateFragment.this.lowDayImage.setImageResource(DateFragment.this.lowDayResource);
                    int highYear = (DateFragment.this.mYear - 2000) / 10;
                    int lowYear = DateFragment.this.mYear % 10;
                    DateFragment.this.highYearResource = UIUtils.getNumberResource(highYear);
                    DateFragment.this.lowYearResource = UIUtils.getNumberResource(lowYear);
                    DateFragment.this.highYearImage.setImageResource(DateFragment.this.highYearResource);
                    DateFragment.this.lowYearImage.setImageResource(DateFragment.this.lowYearResource);
                } catch (Exception e) {
                }
            }
        });
    }

    /* loaded from: classes.dex */
    class DateRunner implements Runnable {
        DateRunner() {
        }

        @Override // java.lang.Runnable
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    DateFragment.this.updateDisplay();
                    Thread.sleep(144000L);
                } catch (Exception e) {
                }
            }
        }
    }

    protected String getDayOfWeek(int day) {
        switch (day) {
            case 1:
                return "Sunday";
            case 2:
                return "Monday";
            case 3:
                return "Tuesday";
            case 4:
                return "Wednesday";
            case 5:
                return "Thursday";
            case 6:
                return "Friday";
            case 7:
                return "Saturday";
            default:
                return "";
        }
    }
}
