package com.remotearthsolutions.expensetracker.utils

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.contracts.BaseView


object RequestReviewUtils {
    fun request(activity: Activity) {
        val sharedPreferenceUtils = SharedPreferenceUtils.getInstance(activity)!!
        with(AnalyticsManager) { logEvent(ASKED_TO_REVIEW_APP) }
        val countNeeded = sharedPreferenceUtils.getInt(
            Constants.ENTRY_NEEDED,
            Constants.DEFAULT_NUMBER_OF_ENTRY_NEEDED
        )
        AlertDialogUtils.show(activity,
            activity.getString(R.string.review),
            activity.getString(R.string.share_experience),
            activity.getString(R.string.ok),
            activity.getString(R.string.later),
            activity.getString(R.string.never),
            object :
                BaseView.Callback {
                override fun onOkBtnPressed() {
                    openApplinkForReview(activity)
                    sharedPreferenceUtils.putInt(
                        Constants.ENTRY_NEEDED,
                        countNeeded + 200
                    )
                }

                override fun onCancelBtnPressed() {
                    sharedPreferenceUtils.putInt(
                        Constants.ENTRY_NEEDED,
                        countNeeded + Constants.DEFAULT_NUMBER_OF_ENTRY_NEEDED
                    )
                }

                override fun onNeutralBtnPressed() {
                    sharedPreferenceUtils.putBoolean(Constants.USER_TOLD_NEVER_ASK_TO_REVIEW, true)
                }
            })
    }

    fun openApplinkForReview(activity: Activity) {
        with(AnalyticsManager) { logEvent(OPENED_GOOGLE_PLAY_FOR_REVIEW) }
        val uri = Uri.parse("market://details?id=${activity.packageName}")
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        try {
            activity.startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            activity.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=${activity.packageName}")
                )
            )
        }
    }
}