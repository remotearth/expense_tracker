package com.remotearthsolutions.expensetracker.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import com.remotearthsolutions.expensetracker.R;

public class SettingsFragment extends PreferenceFragment {

    public SettingsFragment() {
    }

    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;
    private static final String PREF_CURRENCY = "pref_currency_list";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settingspreference);

        preferenceChangeListener = (sharedPreferences, key) -> {

            if (key.equals(PREF_CURRENCY))
            {
                Preference currencyPreference = findPreference(key);
                currencyPreference.setSummary(sharedPreferences.getString(key,""));
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        view.setBackgroundColor(getResources().getColor(android.R.color.white));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(preferenceChangeListener);
        Preference currencyPref = findPreference(PREF_CURRENCY);
        currencyPref.setSummary(getPreferenceScreen().getSharedPreferences().getString(PREF_CURRENCY,""));
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);

    }
}
