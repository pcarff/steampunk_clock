package net.carff.android.steampunkclock.ui;

import androidx.fragment.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.TextView;
import android.content.Intent;
import java.util.Calendar;
import net.carff.android.steampunkclock.R;
import net.carff.android.steampunkclock.SteampunkClockApplication;
import net.carff.android.steampunkclock.util.UIUtils;
/* loaded from: classes.dex */
public class TimeFragment extends Fragment {

    private String font = "duvall.ttf";
    private ImageView highHourImage;
    private int highHourResource;
    private ImageView highMinuteImage;
    private int highMinuteResource;
    private ImageView highSecondImage;
    private int highSecondResource;
    private TextView hourText;
    private ImageView lowHourImage;
    private int lowHourResource;
    private ImageView lowMinuteImage;
    private int lowMinuteResource;
    private ImageView lowSecondImage;
    private int lowSecondResource;
    private int mAmPm;
    private int mHour;
    private int mMinute;
    private int mSecond;
    private TextView minuteText;
    private TextView secondText;
    private TextView timeText;
    private TextView ampmText;

    @Override // android.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Runnable runnable = new TimeRunner();
        Thread myThread = new Thread(runnable);
        myThread.start();
    }

    @Override // android.app.Fragment
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override // android.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.time, container, false);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), this.font);

        TextView textView = (TextView) view.findViewById(R.id.time_title);
        this.timeText = textView;
        textView.setTextSize(20.0f);
        this.timeText.setTypeface(tf);
        this.ampmText = (TextView) view.findViewById(R.id.ampm_text);
        if (this.ampmText != null) {
            this.ampmText.setTypeface(tf);
        }
        TextView textView2 = (TextView) view.findViewById(R.id.hour_title);
        this.hourText = textView2;
        textView2.setTypeface(tf);
        TextView textView3 = (TextView) view.findViewById(R.id.minute_title);
        this.minuteText = textView3;
        textView3.setTypeface(tf);
        TextView textView4 = (TextView) view.findViewById(R.id.second_title);
        this.secondText = textView4;
        textView4.setTypeface(tf);
        this.highHourImage = (ImageView) view.findViewById(R.id.high_hour);
        this.lowHourImage = (ImageView) view.findViewById(R.id.low_hour);
        this.highHourImage.setImageResource(this.highHourResource);
        this.lowHourImage.setImageResource(this.lowHourResource);
        this.highMinuteImage = (ImageView) view.findViewById(R.id.high_minute);
        this.lowMinuteImage = (ImageView) view.findViewById(R.id.low_minute);
        this.highMinuteImage.setImageResource(this.highMinuteResource);
        this.lowMinuteImage.setImageResource(this.lowMinuteResource);
        this.highSecondImage = (ImageView) view.findViewById(R.id.high_second);
        this.lowSecondImage = (ImageView) view.findViewById(R.id.low_second);
        this.highSecondImage.setImageResource(this.highSecondResource);
        this.lowSecondImage.setImageResource(this.lowSecondResource);

        ImageView settingsBtn = (ImageView) view.findViewById(R.id.settings_button);
        if (settingsBtn != null) {
            settingsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), SettingsActivity.class));
                }
            });
        }

        return view;
    }

    public void setText(String item) {
        TextView view = (TextView) getView().findViewById(R.id.time_title);
        view.setText(item);
    }

    public void updateDisplay() {
        getActivity().runOnUiThread(new Runnable() { // from class: net.carff.android.steampunkclock.ui.TimeFragment.2
            @Override // java.lang.Runnable
            public void run() {
                try {
                    Calendar cal = Calendar.getInstance();
                    TimeFragment.this.mHour = cal.get(11);
                    TimeFragment.this.mMinute = cal.get(12);
                    TimeFragment.this.mSecond = cal.get(13);
                    TimeFragment.this.mAmPm = cal.get(9);
                    android.content.SharedPreferences prefs = android.preference.PreferenceManager.getDefaultSharedPreferences(TimeFragment.this.getActivity());
                    boolean isTwelveHour = prefs.getBoolean("twelveHour", true);
                    if (isTwelveHour) {
                        if (TimeFragment.this.mAmPm == 1) {
                            TimeFragment.this.mHour -= 12;
                        }
                        if (TimeFragment.this.mHour == 0) {
                            TimeFragment.this.mHour = 12;
                        }
                        if (TimeFragment.this.ampmText != null) {
                            TimeFragment.this.ampmText.setVisibility(View.VISIBLE);
                            TimeFragment.this.ampmText.setText(TimeFragment.this.mAmPm == 1 ? "PM" : "AM");
                        }
                    } else {
                        if (TimeFragment.this.ampmText != null) {
                            TimeFragment.this.ampmText.setVisibility(View.GONE);
                        }
                    }
                    int highHour = TimeFragment.this.mHour / 10;
                    int lowHour = TimeFragment.this.mHour % 10;
                    TimeFragment.this.highHourResource = UIUtils.getNumberResource(highHour);
                    TimeFragment.this.lowHourResource = UIUtils.getNumberResource(lowHour);
                    TimeFragment.this.highHourImage.setImageResource(TimeFragment.this.highHourResource);
                    TimeFragment.this.lowHourImage.setImageResource(TimeFragment.this.lowHourResource);
                    int highMinute = TimeFragment.this.mMinute / 10;
                    int lowMinute = TimeFragment.this.mMinute % 10;
                    TimeFragment.this.highMinuteResource = UIUtils.getNumberResource(highMinute);
                    TimeFragment.this.lowMinuteResource = UIUtils.getNumberResource(lowMinute);
                    TimeFragment.this.highMinuteImage.setImageResource(TimeFragment.this.highMinuteResource);
                    TimeFragment.this.lowMinuteImage.setImageResource(TimeFragment.this.lowMinuteResource);
                    int highSecond = TimeFragment.this.mSecond / 10;
                    int lowSecond = TimeFragment.this.mSecond % 10;
                    TimeFragment.this.highSecondResource = UIUtils.getNumberResource(highSecond);
                    TimeFragment.this.lowSecondResource = UIUtils.getNumberResource(lowSecond);
                    TimeFragment.this.highSecondImage.setImageResource(TimeFragment.this.highSecondResource);
                    TimeFragment.this.lowSecondImage.setImageResource(TimeFragment.this.lowSecondResource);
                } catch (Exception e) {
                }
            }
        });
    }

    /* loaded from: classes.dex */
    class TimeRunner implements Runnable {
        TimeRunner() {
        }

        @Override // java.lang.Runnable
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    TimeFragment.this.updateDisplay();
                    Thread.sleep(1000L);
                } catch (Exception e) {
                }
            }
        }
    }
}
