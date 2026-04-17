package net.carff.android.steampunkclock.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.core.app.NotificationCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import java.util.Calendar;
import java.util.Locale;
import net.carff.android.steampunkclock.BuildConfig;
import net.carff.android.steampunkclock.R;
import net.carff.android.steampunkclock.SteampunkClockApplication;
import net.carff.android.steampunkclock.service.WeatherDataFetchService;
import net.carff.android.steampunkclock.util.UIUtils;
/* loaded from: classes.dex */
public class HomeActivity extends BaseActivity {
    private Address address;
    private View dimLayout;
    private RadioGroup dimmerRadioGroup;
    private Geocoder geocoder;
    LocationManager mLocMgr;
    private ViewPager mViewPager;
    private Location myLocation;
    private String provider;
    private static final String TAG = HomeActivity.class.getSimpleName();
    private static final int[] DIM_LAYERS = {R.color.dim1, R.color.dim2, R.color.dim3, R.color.dim4, R.color.dim5};
    private static final int[] DIM_CONTROLS = {R.id.dimbutton1, R.id.dimbutton2, R.id.dimbutton3, R.id.dimbutton4, R.id.dimbutton5};
    private String zipCode = null;
    private String city = null;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // net.carff.android.steampunkclock.ui.BaseActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isFinishing()) {
            return;
        }
        if (checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION") != 0) {
            requestPermissions(new String[]{"android.permission.ACCESS_COARSE_LOCATION"}, 1);
        }
        getApplicationContext();
        this.mLocMgr = (LocationManager) getSystemService("location");
        Criteria criteria = new Criteria();
        this.provider = this.mLocMgr.getBestProvider(criteria, false);
        Log.d(TAG, "Got Provider " + this.provider);
        if (this.provider != null) {
            this.myLocation = this.mLocMgr.getLastKnownLocation(this.provider);
        }
        this.geocoder = new Geocoder(this, Locale.getDefault());
        this.zipCode = getZipCode();
        Log.d(TAG, "Zip = " + this.zipCode);
        String fetchUrl = "";
        if (!this.zipCode.equals("00000")) {
            fetchUrl = getString(R.string.api_url_front) + getString(R.string.api_url_key) + getString(R.string.api_url_middle) + this.zipCode + getString(R.string.api_url_end);
        }
        Log.d(TAG, "Starting Fetch Service");
        Intent intent = new Intent(getApplicationContext(), WeatherDataFetchService.class);
        intent.setData(Uri.parse(fetchUrl));
        PendingIntent pintent = PendingIntent.getService(getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);
        startService(intent);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.add(13, 180000);
        am.setRepeating(0, cal.getTimeInMillis(), 180000L, pintent);
        setContentView(R.layout.clock);
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        this.mViewPager = viewPager;
        if (viewPager != null) {
            viewPager.setAdapter(new SteampunkPagerAdapter());
            this.mViewPager.setCurrentItem(0);
        }
        if (UIUtils.isGoogleTV(getBaseContext())) {
            View findViewById = findViewById(R.id.dimmer_overlay);
            this.dimLayout = findViewById;
            findViewById.setVisibility(0);
            RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
            this.dimmerRadioGroup = radioGroup;
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class: net.carff.android.steampunkclock.ui.HomeActivity.1
                @Override // android.widget.RadioGroup.OnCheckedChangeListener
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId == R.id.dimbutton1) {
                        HomeActivity.this.dimLayout.setBackgroundResource(R.color.dim1);
                    } else if (checkedId == R.id.dimbutton2) {
                        HomeActivity.this.dimLayout.setBackgroundResource(R.color.dim2);
                    } else if (checkedId == R.id.dimbutton3) {
                        HomeActivity.this.dimLayout.setBackgroundResource(R.color.dim3);
                    } else if (checkedId == R.id.dimbutton4) {
                        HomeActivity.this.dimLayout.setBackgroundResource(R.color.dim4);
                    } else if (checkedId == R.id.dimbutton5) {
                        HomeActivity.this.dimLayout.setBackgroundResource(R.color.dim5);
                    }
                }
            });
            checkIntentParameters();
        }
    }

    private void checkIntentParameters() {
        Uri uri;
        String dimLevel;
        Intent intent = getIntent();
        if (intent != null && (uri = intent.getData()) != null && (dimLevel = uri.getQueryParameter("dimlevel")) != null) {
            try {
                int requested = Integer.parseInt(dimLevel);
                setDimLevel(requested - 1);
            } catch (NumberFormatException e) {
            }
        }
    }

    private void setDimLevel(int level) {
        if (level < 0 || level > DIM_CONTROLS.length) {
            return;
        }
        this.dimLayout.setBackgroundResource(DIM_LAYERS[level]);
        this.dimmerRadioGroup.check(DIM_CONTROLS[level]);
    }

    private String getZipCode() {
        try {
            String str = TAG;
            Log.d(str, "LAT: " + this.myLocation.getLatitude());
            String str2 = TAG;
            Log.d(str2, "LONG: " + this.myLocation.getLongitude());
            Address address = this.geocoder.getFromLocation(this.myLocation.getLatitude(), this.myLocation.getLongitude(), 1).get(0);
            this.address = address;
            String city = address.getLocality();
            if (city != null) {
                ((SteampunkClockApplication) getApplication()).setCity(city);
            }
            String zipCode = address.getPostalCode();
            return zipCode;
        } catch (Exception e) {
            String str3 = TAG;
            Log.e(str3, "Exception: " + e);
            return "00000";
        }
    }

    /* loaded from: classes.dex */
    private class SteampunkPagerAdapter extends PagerAdapter {
        private SteampunkPagerAdapter() {
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public int getCount() {
            return 2;
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public Object instantiateItem(View collection, int position) {
            LayoutInflater inflater = (LayoutInflater) collection.getContext().getSystemService("layout_inflater");
            int resId = 0;
            if (position == 0) {
                resId = R.layout.time_date_page;
            } else if (position == 1) {
                resId = R.layout.location_page;
            }
            View view = inflater.inflate(resId, (ViewGroup) null);
            ((ViewPager) collection).addView(view, 0);
            return view;
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView((View) arg2);
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == ((View) arg1);
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public Parcelable saveState() {
            return null;
        }
    }
}
