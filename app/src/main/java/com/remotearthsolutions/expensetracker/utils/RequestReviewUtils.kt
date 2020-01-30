package com.remotearthsolutions.expensetracker.utils

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.contracts.BaseView


object RequestReviewUtils {
    fun request(activity: Activity) {
        AlertDialogUtils.show(activity, activity.getString(R.string.review),
            activity.getString(R.string.share_experience),
            activity.getString(R.string.yes), activity.getString(R.string.no), object :
                BaseView.Callback {
                override fun onOkBtnPressed() {
                    openApplinkForReview(activity)
                }

                override fun onCancelBtnPressed() {

                }

            })
    }

    fun openApplinkForReview(activity: Activity) {
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