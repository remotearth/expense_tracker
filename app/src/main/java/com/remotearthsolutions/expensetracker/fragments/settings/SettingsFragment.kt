package com.remotearthsolutions.expensetracker.fragments.settings

import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Build
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
import com.remotearthsolutions.expensetracker.fragments.currenypicker.CurrencyPickerDialogFragmentCompat
import com.remotearthsolutions.expensetracker.fragments.currenypicker.CurrencyPickerPreference
import com.remotearthsolutions.expensetracker.utils.AnalyticsManager
import com.remotearthsolutions.expensetracker.utils.Constants
import com.remotearthsolutions.expensetracker.utils.SharedPreferenceUtils
import com.remotearthsolutions.expensetracker.utils.Utils
import com.remotearthsolutions.expensetracker.utils.Utils.getFlagDrawable


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
                            val mainActivity = requireActivity() as MainActivity
                            if (mainActivity.notificationPermissionUtils.hasNotificationPermissionGranted) {
                                ReminderWorkerHelper.setExportReminder(requireContext())
                                with(AnalyticsManager) { logEvent(EXPORT_REMINDER_ENABLED) }
                            } else {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    mainActivity.notificationPermissionUtils.startNotificationPermissionLauncher {
                                        ReminderWorkerHelper.setExportReminder(requireContext())
                                        with(AnalyticsManager) { logEvent(EXPORT_REMINDER_ENABLED) }
                                    }
                                }
                            }
                        } else {
                            ReminderWorkerHelper.cancelReminder(
                                requireContext(),
                                Constants.KEY_EXPORT_REMINDER_WORKREQUEST_ID
                            )
                            with(AnalyticsManager) { logEvent(EXPORT_REMINDER_DISABLED) }
                        }
                    }
                    Constants.PREF_REMIND_TO_ADDEXPENSE -> {
                        val shouldRemindToAddExpense = sharedPreferences.getBoolean(key, false)
                        if (shouldRemindToAddExpense) {
                            val mainActivity = requireActivity() as MainActivity
                            if (mainActivity.notificationPermissionUtils.hasNotificationPermissionGranted) {
                                ReminderWorkerHelper.setAddExpenseReminder(requireContext())
                                with(AnalyticsManager) { logEvent(ADD_EXPENSE_DAILY_REMINDER_ENABLED) }
                            } else {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    mainActivity.notificationPermissionUtils.startNotificationPermissionLauncher {
                                        ReminderWorkerHelper.setAddExpenseReminder(
                                            requireContext()
                                        )
                                        with(AnalyticsManager) {
                                            logEvent(
                                                ADD_EXPENSE_DAILY_REMINDER_ENABLED
                                            )
                                        }
                                    }
                                }
                            }
                        } else {
                            ReminderWorkerHelper.cancelReminder(
                                requireContext(),
                                Constants.KEY_ADDEXPENSE_REMINDER_WORKREQUEST_ID
                            )
                            with(AnalyticsManager) { logEvent(ADD_EXPENSE_DAILY_REMINDER_DISABLED) }
                        }
                    }
                }
            }
        preferenceScreen.sharedPreferences
            ?.registerOnSharedPreferenceChangeListener(preferenceChangeListener)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        context?.let {
            view.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.background
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
            ?.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener)
    }

    private fun registerBackButton(callBack: OnBackPressedCallback? = null) {
        val activity = activity as MainActivity
        val defaultCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val fragmentManager = activity.supportFragmentManager
                val ft = fragmentManager.beginTransaction()
                ft.setCustomAnimations(R.anim.slide_in_up, 0, 0, R.anim.slide_out_down)
                ft.remove(this@SettingsFragment)
                fragmentManager.popBackStack()
                ft.commit()
                activity.hideBackButton()
                activity.updateTitle()
            }
        }
        activity.onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            callBack ?: defaultCallback
        )
    }

    override fun onDisplayPreferenceDialog(preference: Preference) {
        if (preference is CurrencyPickerPreference) {
            val dialogFragment = CurrencyPickerDialogFragmentCompat.newInstance(preference.key)
            dialogFragment.setTargetFragment(this, 0)
            dialogFragment.show(
                requireActivity().supportFragmentManager,
                CurrencyPickerDialogFragmentCompat::class.java.name
            )
        } else {
            super.onDisplayPreferenceDialog(preference)
        }
    }
}