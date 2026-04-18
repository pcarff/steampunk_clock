package net.carff.android.steampunkclock.ui;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import net.carff.android.steampunkclock.R;

public class SettingsActivity extends Activity {

    private SharedPreferences prefs;
    private Switch switch12Hour;
    private Spinner spinnerBackground;

    private String[] backgroundKeys = {"dkwalnut", "mahogany", "oak", "burl"};
    private String[] backgroundNames = {"Dark Walnut", "Mahogany", "Oak", "Burl Wood"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        switch12Hour = findViewById(R.id.switch_12_hour);
        spinnerBackground = findViewById(R.id.spinner_background);

        switch12Hour.setChecked(prefs.getBoolean("twelveHour", true));
        switch12Hour.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                prefs.edit().putBoolean("twelveHour", isChecked).apply();
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, backgroundNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBackground.setAdapter(adapter);

        String currentBg = prefs.getString("background", "dkwalnut");
        for (int i = 0; i < backgroundKeys.length; i++) {
            if (backgroundKeys[i].equals(currentBg)) {
                spinnerBackground.setSelection(i);
                break;
            }
        }

        spinnerBackground.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                prefs.edit().putString("background", backgroundKeys[position]).apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}
