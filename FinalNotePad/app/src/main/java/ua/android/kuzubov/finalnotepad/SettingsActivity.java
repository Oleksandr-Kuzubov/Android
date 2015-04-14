package ua.android.kuzubov.finalnotepad;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by Kuzia on 02.02.2015.
 */
public class SettingsActivity extends PreferenceActivity{

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }
}
