package com.remotearthsolutions.expensetracker.utils

import android.app.Activity
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.android.billingclient.api.*
import com.android.billingclient.api.BillingFlowParams.ProductDetailsParams
import com.remotearthsolutions.expensetracker.Configs
import com.remotearthsolutions.expensetracker.activities.ApplicationObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class PlayBillingUtils(
    // context must be from a activity of fragment
    val context: Context
) {
    private val purchasesUpdatedListener =
        PurchasesUpdatedListener { billingResult, purchases ->

            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                for (purchase in purchases) {
//                    handlePurchase(purchase)
                }
            } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
                // Handle an error caused by a user cancelling the purchase flow.
            } else {
                // Handle any other error codes.
            }
        }

    private var billingClient = BillingClient.newBuilder(context)
        .setListener(purchasesUpdatedListener)
        .enablePendingPurchases()
        .build()


    fun init() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    runBlocking {
                        showAvailableProducts()
                    }
                }
            }

            override fun onBillingServiceDisconnected() {
                TODO("Not yet implemented")
            }
        })
    }

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

    private val purchasesResponseListener =
        PurchasesResponseListener { billingResult, purchases ->
            val appContext = context.applicationContext as ApplicationObject

            when (billingResult.responseCode) {
                BillingClient.BillingResponseCode.OK -> {

                    if (purchases.isNullOrEmpty()) {
                        appContext.isPremium = false
                        appContext.appShouldShowAds(true)
                    } else {
                        appContext.isPremium = true
                        appContext.appShouldShowAds(false)
                    }

                }
                else -> {
                    throw Exception(RequestError)
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

                        val appContext = context.applicationContext as ApplicationObject

                        when (queryResult.responseCode) {
                            BillingClient.BillingResponseCode.OK -> {

                                if (purchases.isNullOrEmpty()) {
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

    companion object {
        const val RequestError = "billing request not successful"
    }

}