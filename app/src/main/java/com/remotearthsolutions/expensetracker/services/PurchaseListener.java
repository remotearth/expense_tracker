package com.remotearthsolutions.expensetracker.services;

import android.util.Log;
import com.remotearthsolutions.expensetracker.callbacks.InAppBillingCallback;
import org.solovyev.android.checkout.*;

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
        Log.d("Exception", ""+ e.getMessage());
        if(response == ResponseCodes.ITEM_ALREADY_OWNED) {
            billingCallback.onPurchaseFailedListener("You already purchased this item");
        }

    }
}