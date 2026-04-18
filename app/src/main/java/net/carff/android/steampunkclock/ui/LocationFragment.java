package net.carff.android.steampunkclock.ui;

import androidx.fragment.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import net.carff.android.steampunkclock.R;
import net.carff.android.steampunkclock.SteampunkClockApplication;
/* loaded from: classes.dex */
public class LocationFragment extends Fragment {
    private String city;
    private TextView cityText;
    private TextView locationText;
    private String TAG = "LocationFragment";
    private String font = "duvall.ttf";

    @Override // android.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateDisplay();
        System.out.println("Starting Location Runner");
        Runnable runnable = new LocationRunner();
        Thread myThread = new Thread(runnable);
        myThread.start();
    }

    @Override // android.app.Fragment
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override // android.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.location, container, false);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), this.font);
        TextView textView = (TextView) view.findViewById(R.id.location_title);
        this.locationText = textView;
        textView.setTextSize(20.0f);
        this.locationText.setTypeface(tf);
        TextView textView2 = (TextView) view.findViewById(R.id.city_title);
        this.cityText = textView2;
        textView2.setTextSize(20.0f);
        this.cityText.setTypeface(tf);
        this.cityText.setText(getCity());
        return view;
    }

    public void updateDisplay() {
        getActivity().runOnUiThread(new Runnable() { // from class: net.carff.android.steampunkclock.ui.LocationFragment.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    LocationFragment.this.cityText.setText(LocationFragment.this.getCity());
                } catch (Exception e) {
                }
            }
        });
    }

    /* loaded from: classes.dex */
    class LocationRunner implements Runnable {
        LocationRunner() {
        }

        @Override // java.lang.Runnable
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    LocationFragment.this.updateDisplay();
                    Thread.sleep(5000L);
                } catch (Exception e) {
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getCity() {
        return ((SteampunkClockApplication) getActivity().getApplication()).getCity();
    }
}
