package com.remotearthsolutions.expensetracker.activities.main

import android.content.SharedPreferences
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.fragments.main.MainFragment
import com.remotearthsolutions.expensetracker.utils.Constants
import com.remotearthsolutions.expensetracker.utils.SharedPreferenceUtils


class PreferencesChangeListener(private val mainActivity: MainActivity) :
    SharedPreferences.OnSharedPreferenceChangeListener {
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        with(mainActivity) {
            when (key) {
                Constants.PREF_CURRENCY -> {
                    refreshChart()
                }
                Constants.PREF_TIME_FORMAT -> {
                    MainFragment.allExpenseFragment?.updateDateFormat(
                        SharedPreferenceUtils.getInstance(this)!!
                            .getString(
                                Constants.PREF_TIME_FORMAT,
                                resources.getString(R.string.default_time_format)
                            )
                    )
                    refreshChart()
                }
            }
        }
    }
}
