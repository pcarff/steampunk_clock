package net.carff.android.steampunkclock.ui;

import androidx.fragment.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import net.carff.android.steampunkclock.R;
import net.carff.android.steampunkclock.SteampunkClockApplication;
import net.carff.android.steampunkclock.util.UIUtils;
/* loaded from: classes.dex */
public class CurrentFragment extends Fragment {
    private int currentCondition;
    private ImageView currentForecastIcon;
    private int currentTempF;
    private TextView currentText;
    private TextView dayText;
    private ImageView tempHundImage;
    private int tempHundResource;
    private ImageView tempOneImage;
    private int tempOneResource;
    private ImageView tempTenImage;
    private int tempTenResource;
    private String TAG = "CurrentFragment";
    private String font = "duvall.ttf";

    @Override // android.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateDisplay();
        System.out.println("Starting Location Runner");
        Runnable runnable = new CurrentRunner();
        Thread myThread = new Thread(runnable);
        myThread.start();
    }

    @Override // android.app.Fragment
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override // android.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.current, container, false);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), this.font);
        TextView textView = (TextView) view.findViewById(R.id.current_title);
        this.currentText = textView;
        textView.setTypeface(tf);
        TextView textView2 = (TextView) view.findViewById(R.id.day_title);
        this.dayText = textView2;
        textView2.setTextSize(20.0f);
        this.dayText.setTypeface(tf);
        this.currentForecastIcon = (ImageView) view.findViewById(R.id.current_forecast);
        this.tempHundImage = (ImageView) view.findViewById(R.id.temp_hund);
        this.tempTenImage = (ImageView) view.findViewById(R.id.temp_ten);
        this.tempOneImage = (ImageView) view.findViewById(R.id.temp_one);
        this.tempHundImage.setImageResource(this.tempHundResource);
        this.tempTenImage.setImageResource(this.tempTenResource);
        this.tempOneImage.setImageResource(this.tempOneResource);
        return view;
    }

    public void updateDisplay() {
        getActivity().runOnUiThread(new Runnable() { // from class: net.carff.android.steampunkclock.ui.CurrentFragment.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    CurrentFragment.this.dayText.setText(CurrentFragment.this.getDay());
                    CurrentFragment.this.currentTempF = CurrentFragment.this.getTempF();
                    int tempHund = 0;
                    if (CurrentFragment.this.currentTempF > 99) {
                        tempHund = 1;
                        CurrentFragment.this.currentTempF -= 100;
                    }
                    int tempTen = CurrentFragment.this.currentTempF / 10;
                    int tempOne = CurrentFragment.this.currentTempF % 10;
                    CurrentFragment.this.tempHundResource = UIUtils.getNumberResource(tempHund);
                    CurrentFragment.this.tempTenResource = UIUtils.getNumberResource(tempTen);
                    CurrentFragment.this.tempOneResource = UIUtils.getNumberResource(tempOne);
                    CurrentFragment.this.tempHundImage.setImageResource(CurrentFragment.this.tempHundResource);
                    CurrentFragment.this.tempTenImage.setImageResource(CurrentFragment.this.tempTenResource);
                    CurrentFragment.this.tempOneImage.setImageResource(CurrentFragment.this.tempOneResource);
                    CurrentFragment.this.currentCondition = CurrentFragment.this.getCurrentCondition();
                    CurrentFragment.this.currentForecastIcon.setImageResource(UIUtils.getWeatherIconResource(CurrentFragment.this.currentCondition));
                } catch (Exception e) {
                }
            }
        });
    }

    /* loaded from: classes.dex */
    class CurrentRunner implements Runnable {
        CurrentRunner() {
        }

        @Override // java.lang.Runnable
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    CurrentFragment.this.updateDisplay();
                    Thread.sleep(5000L);
                } catch (Exception e) {
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getTempF() {
        return ((SteampunkClockApplication) getActivity().getApplication()).getTempF();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getDay() {
        return ((SteampunkClockApplication) getActivity().getApplication()).getDay();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getCurrentCondition() {
        return ((SteampunkClockApplication) getActivity().getApplication()).getCurrentCondition();
    }

    private void checkData() {
        for (int i = 0; i < 4; i++) {
            String str = this.TAG;
            Log.d(str, "Day: " + ((SteampunkClockApplication) getActivity().getApplication()).getDayOfWeek(i));
            String str2 = this.TAG;
            Log.d(str2, "High: " + ((SteampunkClockApplication) getActivity().getApplication()).getHigh(i));
            String str3 = this.TAG;
            Log.d(str3, "Low: " + ((SteampunkClockApplication) getActivity().getApplication()).getLow(i));
            String str4 = this.TAG;
            Log.d(str4, "Condition: " + ((SteampunkClockApplication) getActivity().getApplication()).getCondition(i));
        }
    }
}
