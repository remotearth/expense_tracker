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
import com.remotearthsolutions.expensetracker.utils.FirebaseEventLogUtils
import com.remotearthsolutions.expensetracker.utils.SharedPreferenceUtils
import com.remotearthsolutions.expensetracker.utils.Utils

class CurrencyFragment : PreferenceFragmentCompat() {

    private var preferenceChangeListener: OnSharedPreferenceChangeListener? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.currencypreference)
        val preferenceCurrency =
            findPreference<Preference>(Constants.PREF_CURRENCY)
        preferenceCurrency!!.summary = SharedPreferenceUtils.getInstance(context!!)!!.getString(
            Constants.PREF_CURRENCY,
            context!!.resources.getString(R.string.default_currency)
        )
        preferenceCurrency.setIcon(Utils.getFlagDrawable(context!!))
        preferenceChangeListener =
            OnSharedPreferenceChangeListener { sharedPreferences: SharedPreferences, key: String ->
                if (key == Constants.PREF_CURRENCY) {
                    val currencyPreference =
                        findPreference<Preference>(key)
                    val `val` = sharedPreferences.getString(
                        key,
                        context!!.resources.getString(R.string.default_currency)
                    )
                    currencyPreference!!.summary = sharedPreferences.getString(key, `val`)
                    currencyPreference.setIcon(Utils.getFlagDrawable(context!!))
                    FirebaseEventLogUtils.logCustom(context!!, `val`)
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
        view!!.setBackgroundColor(ContextCompat.getColor(context!!,R.color.lightAccent))
        return view
    }

    override fun onStop() {
        super.onStop()
        preferenceScreen.sharedPreferences
            .unregisterOnSharedPreferenceChangeListener(preferenceChangeListener)
    }
}