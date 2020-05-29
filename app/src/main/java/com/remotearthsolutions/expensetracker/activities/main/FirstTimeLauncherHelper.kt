package com.remotearthsolutions.expensetracker.activities.main

import android.content.Intent
import com.remotearthsolutions.expensetracker.activities.InitialPreferenceActivity
import com.remotearthsolutions.expensetracker.utils.SharedPreferenceUtils


class FirstTimeLauncherHelper {

    fun execute(activity: MainActivity) {
        showForVersionCode43(activity)
    }

    private fun showForVersionCode43(activity: MainActivity) {
        val sharedPreferenceUtils = SharedPreferenceUtils.getInstance(activity)
        if (sharedPreferenceUtils?.getBoolean(KEY_FIRST_TIME_V43, true)!!) {
//            sharedPreferenceUtils.putBoolean(KEY_FIRST_TIME_V43, false)
            activity.finish()
            activity.startActivity(Intent(activity, InitialPreferenceActivity::class.java))
        }
    }

    companion object {
        const val KEY_FIRST_TIME_V43 = "v43_firsttime"
    }
}