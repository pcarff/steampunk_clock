package net.carff.android.steampunkclock.ui;

import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;
import net.carff.android.steampunkclock.util.UIUtils;
/* loaded from: classes.dex */
public abstract class BaseActivity extends FragmentActivity {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIUtils.isGoogleTV(this);
    }
}
