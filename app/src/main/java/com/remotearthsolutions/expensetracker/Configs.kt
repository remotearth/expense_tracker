package com.remotearthsolutions.expensetracker

import com.remotearthsolutions.expensetracker.utils.Constants

class Configs {

    companion object {
        fun getAdFreeProductId(): String {
            return if (BuildConfig.DEBUG) {
                Constants.TEST_PURCHASED_ITEM
            } else {
                Constants.PRODUCT_ID
            }
        }
    }

}