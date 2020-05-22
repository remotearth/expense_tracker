package com.remotearthsolutions.expensetracker.fragments.settings

import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.activities.main.MainActivity
import com.remotearthsolutions.expensetracker.utils.AnalyticsManager
import com.remotearthsolutions.expensetracker.utils.Constants
import com.remotearthsolutions.expensetracker.utils.SharedPreferenceUtils
import com.remotearthsolutions.expensetracker.utils.Utils.getFlagDrawable
import kotlinx.android.synthetic.main.activity_main.*


class SettingsFragment : PreferenceFragmentCompat() {

    private var preferenceChangeListener: OnSharedPreferenceChangeListener? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settingspreference)
        val preferencePeriod =
            findPreference<Preference>(Constants.PREF_PERIOD)
        preferencePeriod!!.summary =
            SharedPreferenceUtils.getInstance(requireContext())!!.getString(
                Constants.PREF_PERIOD,
                resources.getString(R.string.daily)
            )

        val preferenceTimeFormat = findPreference<Preference>(Constants.PREF_TIME_FORMAT)
        preferenceTimeFormat!!.summary =
            SharedPreferenceUtils.getInstance(requireContext())!!.getString(
                Constants.PREF_TIME_FORMAT,
                resources.getString(R.string.default_time_format)
            )

        val preferenceCurrency =
            findPreference<Preference>(Constants.PREF_CURRENCY)
        val currencyName = SharedPreferenceUtils.getInstance(requireContext())?.getString(
            Constants.PREF_CURRENCY,
            resources.getString(R.string.default_currency)
        )
        preferenceCurrency!!.summary = currencyName
        preferenceCurrency.setIcon(
            getFlagDrawable(requireContext())
        )
        preferenceChangeListener =
            OnSharedPreferenceChangeListener { sharedPreferences: SharedPreferences, key: String ->
                when (key) {
                    Constants.PREF_CURRENCY -> {
                        val currencyPreference =
                            findPreference<Preference>(key)
                        val currency = sharedPreferences.getString(
                            key,
                            resources.getString(R.string.default_currency)
                        )
                        currencyPreference!!.summary = currency
                        currencyPreference.setIcon(getFlagDrawable(requireContext()))
                        AnalyticsManager.logEvent(currency)
                    }
                    Constants.PREF_PERIOD -> {
                        val periodPreference =
                            findPreference<Preference>(key)
                        periodPreference!!.summary = sharedPreferences.getString(
                            key, resources.getString(R.string.daily)
                        )
                        AnalyticsManager.logEvent(key)
                    }
                    Constants.PREF_TIME_FORMAT -> {
                        val timeFormatPreference =
                            findPreference<Preference>(key)
                        timeFormatPreference!!.summary =
                            sharedPreferences.getString(
                                key,
                                resources.getString(R.string.default_time_format)
                            )
                        AnalyticsManager.logEvent(key)
                    }
                    Constants.PREF_REMIND_TO_EXPORT -> {
                        val shouldRemindToExport = sharedPreferences.getBoolean(key, false)
                        if (shouldRemindToExport) {
                            ExportReminderHelper.setReminderToExport(requireContext())
                        } else {
                            ExportReminderHelper.cancelExportReminder(requireContext())
                        }
                        AnalyticsManager.logEvent("ExportReminder-$shouldRemindToExport")
                    }
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
            view?.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    android.R.color.white
                )
            )
        }
        registerBackButton()
        return view
    }

    override fun onStop() {
        super.onStop()
        preferenceScreen.sharedPreferences
            .unregisterOnSharedPreferenceChangeListener(preferenceChangeListener)
    }

    private fun registerBackButton(callBack: OnBackPressedCallback? = null) {
        val activity = requireActivity()
        val defaultCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val fragmentManager = activity.supportFragmentManager
                val ft = fragmentManager.beginTransaction()
                ft.setCustomAnimations(R.anim.slide_in_up, 0, 0, R.anim.slide_out_down)
                ft.remove(this@SettingsFragment)
                fragmentManager.popBackStack()
                ft.commit()
                activity.toolbar!!.title = getString(R.string.title_home)
                (activity as MainActivity).hideBackButton()
            }
        }
        activity.onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            callBack ?: defaultCallback
        )
    }
}