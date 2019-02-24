package com.remotearthsolutions.expensetracker.services;

import android.util.Log;
import com.remotearthsolutions.expensetracker.callbacks.InAppBillingCallback;
import org.solovyev.android.checkout.EmptyRequestListener;
import org.solovyev.android.checkout.Purchase;

public class PurchaseListener extends EmptyRequestListener<Purchase> {
    private InAppBillingCallback billingCallback;

    public PurchaseListener(InAppBillingCallback billingCallback) {
        this.billingCallback = billingCallback;
    }

    @Override
    public void onSuccess(Purchase purchase) {
        Log.d("Success", "onSuccess: ");
        billingCallback.onPurchaseSuccessListener(purchase);
    }

    @Override
    public void onError(int response, Exception e) {
        Log.d("Error", "onError: "+ response);
        billingCallback.onPurchaseFailedListener(e.getMessage());
        e.printStackTrace();
    }
}