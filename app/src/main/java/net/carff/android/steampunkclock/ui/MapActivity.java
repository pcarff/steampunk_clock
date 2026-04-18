package net.carff.android.steampunkclock.ui;

import android.os.Bundle;
import net.carff.android.steampunkclock.R;
import net.carff.android.steampunkclock.ui.view.TerminatorMapView;

public class MapActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_dashboard);
        
        // In a real app, we'd pass location via Intent extras
        // For now, it will use the default or we could fetch last known
    }
}
