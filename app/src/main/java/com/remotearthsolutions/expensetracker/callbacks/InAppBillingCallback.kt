package com.remotearthsolutions.expensetracker.callbacks

import org.solovyev.android.checkout.Purchase

interface InAppBillingCallback {
    fun onPurchaseSuccessListener(purchase: Purchase?)
    fun onPurchaseFailedListener(error: String?)
}