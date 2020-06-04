package com.remotearthsolutions.expensetracker.activities.main

import android.content.Intent
import android.os.Handler
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.activities.InitialPreferenceActivity
import com.remotearthsolutions.expensetracker.contracts.BaseView
import com.remotearthsolutions.expensetracker.utils.AlertDialogUtils
import com.remotearthsolutions.expensetracker.utils.SharedPreferenceUtils


class FirstTimeLauncherHelper {

    fun execute(activity: MainActivity) {
        showForVersionCode43(activity)
    }

    private fun showForVersionCode43(activity: MainActivity) {
        //this will show option to select language to existing users who have updated the app from a older version.
        //new user will not see the dialog as they already select language at first run in InitialPreferenceActivity screen.
        val sharedPreferenceUtils = SharedPreferenceUtils.getInstance(activity)
        if (sharedPreferenceUtils?.getBoolean(KEY_FIRST_TIME_V43, true)!!) {
            Handler().postDelayed(Runnable {
                AlertDialogUtils.show(
                    activity,
                    "",
                    activity.getString(R.string.can_change_language),
                    activity.getString(R.string.ok),
                    null, null,
                    object : BaseView.Callback {
                        override fun onOkBtnPressed() {
                            activity.finish()
                            activity.startActivity(
                                Intent(
                                    activity,
                                    InitialPreferenceActivity::class.java
                                )
                            )
                        }
                    })
            }, 3000)
        }
    }

    companion object {
        const val KEY_FIRST_TIME_V43 = "v43_firsttime"
    }
}