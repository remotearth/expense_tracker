package com.remotearthsolutions.expensetracker.callbacks;

import org.solovyev.android.checkout.Purchase;

public interface InAppBillingCallback {
    void onPurchaseSuccessListener(Purchase purchase);
    void onPurchaseFailedListener(String error);
}
