package ua.android.kuzubov.finalnotepad;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class FinalNotePadActivity extends KuziaSingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new FinalNotePadFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
