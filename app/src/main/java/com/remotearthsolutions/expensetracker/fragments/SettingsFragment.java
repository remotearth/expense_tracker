package com.remotearthsolutions.expensetracker.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.utils.Constants;
import com.remotearthsolutions.expensetracker.utils.SharedPreferenceUtils;

public class SettingsFragment extends PreferenceFragmentCompat {

    private Context context;
    private Resources resources;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        resources = context.getResources();
    }

    public SettingsFragment() {
    }

    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settingspreference);

        Preference preferencePeriod = findPreference(Constants.PREF_PERIOD);
        preferencePeriod.setSummary(SharedPreferenceUtils.getInstance(context).getString(Constants.PREF_PERIOD, Constants.KEY_DAILY));
        Preference preferenceCurrency = findPreference(Constants.PREF_CURRENCY);
        preferenceCurrency.setSummary(SharedPreferenceUtils.getInstance(context).getString(Constants.PREF_CURRENCY, resources.getString(R.string.default_currency)));

        preferenceChangeListener = (sharedPreferences, key) -> {

            if (key.equals(Constants.PREF_CURRENCY)) {

                Preference currencyPreference = findPreference(key);
                String val = sharedPreferences.getString(key, resources.getString(R.string.default_currency));
                currencyPreference.setSummary(val);
                Answers.getInstance().logCustom(new CustomEvent(val));

            } else if (key.equals(Constants.PREF_PERIOD)) {

                Preference periodPreference = findPreference(key);
                periodPreference.setSummary(sharedPreferences.getString(key, Constants.KEY_DAILY));

            }
        };

        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        view.setBackgroundColor(resources.getColor(android.R.color.white));

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

}
