package com.sujan.trackingme;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceActivity;

import static com.sujan.trackingme.R.*;


public class SettingsActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        addPreferencesFromResource(R.xml.set);
    }
}
