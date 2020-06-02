package com.remotearthsolutions.expensetracker.activities.main

import android.content.Context
import com.remotearthsolutions.expensetracker.activities.ApplicationObject
import com.remotearthsolutions.expensetracker.callbacks.InAppBillingCallback
import com.remotearthsolutions.expensetracker.utils.AnalyticsManager
import com.remotearthsolutions.expensetracker.utils.Utils
import org.solovyev.android.checkout.Inventory
import org.solovyev.android.checkout.ProductTypes
import org.solovyev.android.checkout.Purchase


class InAppPurchaseCallback(val context: Context) : InAppBillingCallback, Inventory.Callback {
    private val appContext = context.applicationContext as ApplicationObject
    private val productId = appContext.adProductId

    override fun onPurchaseSuccessListener(purchase: Purchase?) {
        appContext.isPremium = true
        if (purchase?.sku == productId) {
            with(AnalyticsManager) { logEvent(APP_PURCHASED) }
            appContext.appShouldShowAds(false)
        }
    }

    override fun onPurchaseFailedListener(error: String?) {
        Utils.showToast(context, error!!)
    }

    override fun onLoaded(products: Inventory.Products) {
        if (!products[ProductTypes.IN_APP].isPurchased(productId)) {
            appContext.isPremium = false
            appContext.appShouldShowAds(true)
        } else {
            appContext.isPremium = true
        }
    }
}
