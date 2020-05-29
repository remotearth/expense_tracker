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
        Handler().postDelayed(Runnable {
            AlertDialogUtils.show(
                activity,
                "",
                "In this new version of the app user can choose the app by himself.",
                activity.getString(R.string.ok),
                null,
                object : BaseView.Callback {
                    override fun onOkBtnPressed() {
                        val sharedPreferenceUtils = SharedPreferenceUtils.getInstance(activity)
                        if (sharedPreferenceUtils?.getBoolean(KEY_FIRST_TIME_V43, true)!!) {
                            activity.finish()
                            activity.startActivity(
                                Intent(
                                    activity,
                                    InitialPreferenceActivity::class.java
                                )
                            )
                        }
                    }
                })
        }, 3000)
    }

    companion object {
        const val KEY_FIRST_TIME_V43 = "v43_firsttime"
    }
}