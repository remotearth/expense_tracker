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
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.utils.Constants
import com.remotearthsolutions.expensetracker.utils.FabricAnswersUtils.logCustom
import com.remotearthsolutions.expensetracker.utils.SharedPreferenceUtils
import com.remotearthsolutions.expensetracker.utils.Utils.getFlagDrawable

class SettingsFragment : PreferenceFragmentCompat() {

    private var preferenceChangeListener: OnSharedPreferenceChangeListener? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settingspreference)
        val preferencePeriod =
            findPreference<Preference>(Constants.PREF_PERIOD)
        preferencePeriod!!.summary = SharedPreferenceUtils.getInstance(context!!)!!.getString(
            Constants.PREF_PERIOD,
            resources.getString(R.string.daily)
        )
        val preferenceCurrency =
            findPreference<Preference>(Constants.PREF_CURRENCY)
        val currencyName = SharedPreferenceUtils.getInstance(context!!)?.getString(
            Constants.PREF_CURRENCY,
            resources.getString(R.string.default_currency)
        )
        preferenceCurrency!!.summary = currencyName
        preferenceCurrency.setIcon(
            getFlagDrawable(
                context!!
            )
        )
        preferenceChangeListener =
            OnSharedPreferenceChangeListener { sharedPreferences: SharedPreferences, key: String ->
                if (key == Constants.PREF_CURRENCY) {
                    val currencyPreference =
                        findPreference<Preference>(key)
                    val `val` = sharedPreferences.getString(
                        key,
                        resources.getString(R.string.default_currency)
                    )
                    currencyPreference!!.summary = `val`
                    currencyPreference.setIcon(
                        getFlagDrawable(
                            context!!
                        )
                    )
                    logCustom(`val`)
                } else if (key == Constants.PREF_PERIOD) {
                    val periodPreference =
                        findPreference<Preference>(key)
                    periodPreference!!.summary = sharedPreferences.getString(
                        key,
                        resources.getString(R.string.daily)
                    )
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
        context?.let {
            view!!.setBackgroundColor(ContextCompat.getColor(context!!, android.R.color.white))
        }
        return view
    }

    override fun onStop() {
        super.onStop()
        preferenceScreen.sharedPreferences
            .unregisterOnSharedPreferenceChangeListener(preferenceChangeListener)
    }
}