package com.remotearthsolutions.expensetracker.fragments

import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.utils.Constants
import com.remotearthsolutions.expensetracker.utils.SharedPreferenceUtils
import com.remotearthsolutions.expensetracker.utils.Utils


class InitialSettingsFragment : PreferenceFragmentCompat() {

    private var preferenceChangeListener: OnSharedPreferenceChangeListener? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.initial_preferences)
        val preferenceCurrency =
            findPreference<Preference>(Constants.PREF_CURRENCY)
        preferenceCurrency!!.summary =
            SharedPreferenceUtils.getInstance(requireContext())!!.getString(
                Constants.PREF_CURRENCY,
                requireContext().resources.getString(R.string.default_currency)
            )
        preferenceCurrency.setIcon(Utils.getFlagDrawable(requireContext()))

        val preferenceLanguage =
            findPreference<Preference>(Constants.PREF_LANGUAGE)
        preferenceLanguage!!.summary =
            SharedPreferenceUtils.getInstance(requireContext())!!.getString(
                Constants.PREF_LANGUAGE,
                requireContext().resources.getString(R.string.default_language)
            )

        preferenceChangeListener =
            OnSharedPreferenceChangeListener { sharedPreferences: SharedPreferences, key: String ->
                if (key == Constants.PREF_CURRENCY) {
                    val currencyPreference =
                        findPreference<Preference>(key)
                    val currency = sharedPreferences.getString(
                        key,
                        requireContext().resources.getString(R.string.default_currency)
                    )
                    currencyPreference!!.summary = sharedPreferences.getString(key, currency)
                    currencyPreference.setIcon(Utils.getFlagDrawable(requireContext()))
                } else if (key == Constants.PREF_LANGUAGE) {
                    val languagePreference =
                        findPreference<Preference>(key)
                    val language = sharedPreferences.getString(
                        key,
                        requireContext().resources.getString(R.string.default_language)
                    )
                    languagePreference!!.summary = sharedPreferences.getString(key, language)
                }
            }
        preferenceScreen.sharedPreferences
            .registerOnSharedPreferenceChangeListener(preferenceChangeListener)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        view?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.lightAccent))

        val itemDecoration =
            DividerItemDecoration(context, RecyclerView.VERTICAL)
        listView.addItemDecoration(itemDecoration)
        return view
    }

    override fun onStop() {
        super.onStop()
        preferenceScreen.sharedPreferences
            .unregisterOnSharedPreferenceChangeListener(preferenceChangeListener)
    }
}