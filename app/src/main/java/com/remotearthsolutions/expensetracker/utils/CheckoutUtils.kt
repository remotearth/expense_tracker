package com.remotearthsolutions.expensetracker.utils

import android.app.Activity
import android.util.Log
import com.remotearthsolutions.expensetracker.activities.ApplicationObject
import org.solovyev.android.checkout.*

class CheckoutUtils private constructor(activity: Activity) {
    val checkout: ActivityCheckout = Checkout.forActivity(activity, (activity.application as ApplicationObject).billing)
    private var isStarted = false
    private var purchaseFlowCreated = false
    fun start() {
        try {
            if (!isStarted) {
                checkout.start()
                isStarted = true
            }
        } catch (e: Exception) {
            Log.d("Exception", "" + e.message)
        }
    }

    fun stop() {
        checkout.stop()
        checkout.destroyPurchaseFlow()
        purchaseFlowCreated = false
        isStarted = purchaseFlowCreated
    }

    fun createPurchaseFlow(purchaseEmptyRequestListener: EmptyRequestListener<Purchase>?) {
        if (!purchaseFlowCreated) {
            checkout.createPurchaseFlow(purchaseEmptyRequestListener!!)
            purchaseFlowCreated = true
        }
    }

    val purchaseFlow: PurchaseFlow
        get() = checkout.purchaseFlow

    fun load(callback: Inventory.Callback?, vararg productId: String?) {
        val mInventory = checkout.makeInventory()
        mInventory.load(
            Inventory.Request.create()
                .loadAllPurchases()
                .loadSkus(ProductTypes.IN_APP, *productId), callback!!
        )
    }

    companion object {
        private var instance: CheckoutUtils? = null
        fun getInstance(activity: Activity): CheckoutUtils? {
            if (instance == null) {
                instance =
                    CheckoutUtils(activity)
            }
            return instance
        }

        fun clearInstance() {
            instance = null
        }
    }

}