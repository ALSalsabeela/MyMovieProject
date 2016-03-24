package com.example.lenovo.mymovieapp;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

/**
 * Created by lenovo on 23/03/2016.
 */
public class MySetting extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs_general);
        bindPrefrenceSumarryToValue(findPreference(getString(R.string.pref_sort_order_key)));
    }

    private void bindPrefrenceSumarryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(this);
        onPreferenceChange(preference, PreferenceManager.getDefaultSharedPreferences(getBaseContext())
                .getString(preference.getKey(), ""));

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        String value = newValue.toString();

        if (preference instanceof ListPreference) {
            ListPreference list = (ListPreference) preference;
            int index = list.findIndexOfValue(value);
            if (index >= 0) {
                preference.setSummary(list.getEntries()[index]);
            } else {
                preference.setSummary(value);
            }
        }

        return true;
    }
}


