package com.remotearthsolutions.expensetracker.utils

import android.app.Activity
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.android.billingclient.api.*
import com.android.billingclient.api.BillingFlowParams.ProductDetailsParams
import com.remotearthsolutions.expensetracker.Configs
import com.remotearthsolutions.expensetracker.activities.ApplicationObject

class PlayBillingUtils(
    // context must be from a activity of fragment
    val context: Context
) {
    private val appContext = context.applicationContext as ApplicationObject

    private val purchasesUpdatedListener =
        PurchasesUpdatedListener { billingResult, purchases ->

            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                for (purchase in purchases) {
                    acknowledgePurchases(purchase)
                }
            } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
                appContext.isPremium = false
                appContext.appShouldShowAds(true)
            } else {
                // Handle any other error codes.
                // Log something went wrong. Could not complete the purchase.
            }
        }

    private var billingClient = BillingClient.newBuilder(context)
        .setListener(purchasesUpdatedListener)
        .enablePendingPurchases()
        .build()

    fun showAvailableProducts() {

        if (billingClient.isReady) {
            val productList = listOf(
                QueryProductDetailsParams.Product.newBuilder()
                    .setProductId(Configs.getAdFreeProductId())
                    .setProductType(BillingClient.ProductType.INAPP)
                    .build()
            )

            val params = QueryProductDetailsParams.newBuilder()
            params.setProductList(productList)

            billingClient.queryProductDetailsAsync(
                params.build()
            ) { billingResult: BillingResult,
                listOfProductDetails: MutableList<ProductDetails> ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    val productDetailsParamsList = mutableListOf<ProductDetailsParams>()

                    listOfProductDetails.forEach {
                        productDetailsParamsList.add(
                            ProductDetailsParams.newBuilder()
                                .setProductDetails(it)
                                .build()
                        )
                    }

                    val billingFlowParams = BillingFlowParams.newBuilder()
                        .setProductDetailsParamsList(productDetailsParamsList)
                        .build()
                    billingClient.launchBillingFlow(context as Activity, billingFlowParams)
                }
            }
        }

    }

    fun checkIfAdFreeVersionPurchased(purchaseStatusChecked: MutableLiveData<Boolean>) {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    val params = QueryPurchasesParams.newBuilder()
                        .setProductType(BillingClient.ProductType.INAPP)
                    billingClient.queryPurchasesAsync(params.build()) { queryResult, purchases ->

                        when (queryResult.responseCode) {
                            BillingClient.BillingResponseCode.OK -> {

                                if (purchases.isEmpty()) {
                                    appContext.isPremium = false
                                    appContext.appShouldShowAds(true)
                                } else {
                                    appContext.isPremium = true
                                    appContext.appShouldShowAds(false)
                                }

                                purchaseStatusChecked.postValue(true)
                            }
                            else -> {
                                purchaseStatusChecked.postValue(false)
                            }
                        }
                    }
                }
            }

            override fun onBillingServiceDisconnected() {
                purchaseStatusChecked.postValue(false)
            }
        })

    }

    fun stopBillingClientConnection() {
        billingClient.endConnection()
    }

    private fun acknowledgePurchases(purchase: Purchase?) {
        purchase?.let {
            if (!it.isAcknowledged) {
                val params = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(it.purchaseToken)
                    .build()

                billingClient.acknowledgePurchase(
                    params
                ) { billingResult ->
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK &&
                        it.purchaseState == Purchase.PurchaseState.PURCHASED
                    ) {
                        appContext.isPremium = true
                        appContext.appShouldShowAds(false)
                    }
                }
            }
        }
    }
}