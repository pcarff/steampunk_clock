package net.carff.android.steampunkclock.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.GnssStatus;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import net.carff.android.steampunkclock.R;
import net.carff.android.steampunkclock.ui.view.SignalTrendView;

public class GpsAnalysisActivity extends BaseActivity {
    private SignalTrendView signalTrend;
    private TextView satDetailText;
    private LocationManager locationManager;
    private GnssStatus.Callback gnssStatusCallback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gps_analysis_dashboard);

        signalTrend = findViewById(R.id.analysis_signal_trend);
        satDetailText = findViewById(R.id.sat_detail_text);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        initGnss();
    }

    @SuppressLint("MissingPermission")
    private void initGnss() {
        gnssStatusCallback = new GnssStatus.Callback() {
            @Override
            public void onSatelliteStatusChanged(GnssStatus status) {
                int count = status.getSatelliteCount();
                StringBuilder sb = new StringBuilder("VISIBLE SATELLITES:\n");
                float totalSnr = 0;
                int snrCount = 0;

                for (int i = 0; i < count; i++) {
                    float snr = status.getCn0DbHz(i);
                    sb.append("PRN ").append(status.getSvid(i))
                      .append(": ").append(String.format("%.1f", snr)).append(" dBHz");
                    if (status.usedInFix(i)) sb.append(" [LOCKED]");
                    sb.append("\n");

                    if (snr > 0) {
                        totalSnr += snr;
                        snrCount++;
                    }
                }
                
                final float avgSnr = snrCount > 0 ? totalSnr / snrCount : 0;
                final String details = sb.toString();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        satDetailText.setText(details);
                        signalTrend.addDataPoint(avgSnr);
                    }
                });
            }
        };
        locationManager.registerGnssStatusCallback(gnssStatusCallback, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (gnssStatusCallback != null) {
            locationManager.unregisterGnssStatusCallback(gnssStatusCallback);
        }
    }
}
