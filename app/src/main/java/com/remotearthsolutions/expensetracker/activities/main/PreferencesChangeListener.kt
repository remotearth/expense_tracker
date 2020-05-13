package com.remotearthsolutions.expensetracker.activities.main

import android.content.SharedPreferences
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.fragments.AllExpenseFragment
import com.remotearthsolutions.expensetracker.fragments.main.MainFragment
import com.remotearthsolutions.expensetracker.utils.Constants
import com.remotearthsolutions.expensetracker.utils.SharedPreferenceUtils
import com.remotearthsolutions.expensetracker.utils.findViewPagerFragmentByTag

class PreferencesChangeListener(private val mainActivity: MainActivity) :
    SharedPreferences.OnSharedPreferenceChangeListener {
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        with(mainActivity) {
            when (key) {
                Constants.PREF_CURRENCY -> {
                    refreshChart()
                }
                Constants.PREF_TIME_FORMAT -> {
                    val mainFragment =
                        supportFragmentManager.findFragmentByTag(MainFragment::class.java.name) as MainFragment?
                    val allExpenseFragment =
                        mainFragment?.childFragmentManager?.findViewPagerFragmentByTag<AllExpenseFragment>(
                            R.id.viewpager,
                            1
                        )
                    allExpenseFragment?.updateDateFormat(
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
