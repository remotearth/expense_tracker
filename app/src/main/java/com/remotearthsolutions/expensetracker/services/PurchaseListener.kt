package com.remotearthsolutions.expensetracker.services

import android.content.Context
import android.util.Log
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.callbacks.InAppBillingCallback
import com.remotearthsolutions.expensetracker.utils.AnalyticsManager
import org.solovyev.android.checkout.EmptyRequestListener
import org.solovyev.android.checkout.Purchase
import org.solovyev.android.checkout.ResponseCodes

class PurchaseListener(
    private val context: Context,
    private val billingCallback: InAppBillingCallback
) : EmptyRequestListener<Purchase>() {
    override fun onSuccess(purchase: Purchase) {
        Log.d("Success", "onSuccess: ")
        billingCallback.onPurchaseSuccessListener(purchase)
        AnalyticsManager.logEvent("App purchase")
    }

    override fun onError(response: Int, e: Exception) {
        Log.d("Error", "onError: $response")
        Log.d("Exception", "" + e.message)
        if (response == ResponseCodes.ITEM_ALREADY_OWNED) {
            billingCallback.onPurchaseFailedListener(context.resources.getString(R.string.already_purchased))
        }
    }

}