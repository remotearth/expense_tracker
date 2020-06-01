package com.remotearthsolutions.expensetracker.fragments.settings

import android.content.Intent
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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
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
                Constants.PREF_PERIOD, requireContext().getString(R.string.daily)
            )

        val preferenceLanguage = findPreference<Preference>(Constants.PREF_LANGUAGE)
        preferenceLanguage!!.summary =
            SharedPreferenceUtils.getInstance(requireContext())!!.getString(
                Constants.PREF_LANGUAGE, requireContext().getString(R.string.default_language)
            )

        val preferenceTimeFormat = findPreference<Preference>(Constants.PREF_TIME_FORMAT)
        preferenceTimeFormat!!.summary =
            SharedPreferenceUtils.getInstance(requireContext())!!.getString(
                Constants.PREF_TIME_FORMAT, requireContext().getString(R.string.default_time_format)
            )

        val preferenceCurrency =
            findPreference<Preference>(Constants.PREF_CURRENCY)
        val currencyName = SharedPreferenceUtils.getInstance(requireContext())?.getString(
            Constants.PREF_CURRENCY, requireContext().getString(R.string.default_currency)
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
                        AnalyticsManager.logEvent(currency!!)
                    }
                    Constants.PREF_PERIOD -> {
                        val periodPreference =
                            findPreference<Preference>(key)
                        periodPreference!!.summary = sharedPreferences.getString(
                            key, resources.getString(R.string.daily)
                        )
                        AnalyticsManager.logEvent(key)
                    }
                    Constants.PREF_LANGUAGE -> {
                        val languagePreference =
                            findPreference<Preference>(key)
                        val selectedLang = sharedPreferences.getString(
                            key, resources.getString(R.string.default_language)
                        )
                        languagePreference!!.summary = selectedLang
                        AnalyticsManager.logEvent("Choosen Language - $selectedLang")
                        (requireActivity() as MainActivity).finish()
                        val intent = Intent(requireActivity(), MainActivity::class.java)
                        startActivity(intent)
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
                            ReminderWorkerHelper.setExportReminder(requireContext())
                            AnalyticsManager.logEvent(AnalyticsManager.EXPORT_REMINDER_ENABLED)
                        } else {
                            ReminderWorkerHelper.cancelReminder(
                                requireContext(),
                                Constants.KEY_EXPORT_REMINDER_WORKREQUEST_ID
                            )
                            AnalyticsManager.logEvent(AnalyticsManager.EXPORT_REMINDER_DISABLED)
                        }
                    }
                    Constants.PREF_REMIND_TO_ADDEXPENSE -> {
                        val shouldRemindToAddExpense = sharedPreferences.getBoolean(key, false)
                        if (shouldRemindToAddExpense) {
                            ReminderWorkerHelper.setAddExpenseReminder(requireContext())
                            AnalyticsManager.logEvent(AnalyticsManager.ADD_EXPENSE_DAILY_REMINDER_ENABLED)
                        } else {
                            ReminderWorkerHelper.cancelReminder(
                                requireContext(),
                                Constants.KEY_ADDEXPENSE_REMINDER_WORKREQUEST_ID
                            )
                            AnalyticsManager.logEvent(AnalyticsManager.ADD_EXPENSE_DAILY_REMINDER_DISABLED)
                        }
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

        val itemDecoration =
            DividerItemDecoration(context, RecyclerView.VERTICAL)
        listView.addItemDecoration(itemDecoration)

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