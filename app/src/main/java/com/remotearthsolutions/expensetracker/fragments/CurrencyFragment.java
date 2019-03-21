package com.remotearthsolutions.expensetracker.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.utils.Constants;
import com.remotearthsolutions.expensetracker.utils.SharedPreferenceUtils;

public class CurrencyFragment extends PreferenceFragmentCompat {

    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public CurrencyFragment() {
    }

    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.currencypreference);

        Preference preferenceCurrency = findPreference(Constants.PREF_CURRENCY);
        preferenceCurrency.setSummary(SharedPreferenceUtils.getInstance(context).getString(Constants.PREF_CURRENCY,
                context.getResources().getString(R.string.default_currency)));

        preferenceChangeListener = (sharedPreferences, key) -> {
            if (key.equals(Constants.PREF_CURRENCY)) {
                Preference currencyPreference = findPreference(key);
                currencyPreference.setSummary(sharedPreferences.getString(key, context.getResources().getString(R.string.default_currency)));
            }
        };

        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        view.setBackgroundColor(getResources().getColor(R.color.lightAccent));

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

}
