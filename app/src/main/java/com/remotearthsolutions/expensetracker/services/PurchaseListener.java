package com.remotearthsolutions.expensetracker.services;

import android.util.Log;
import com.remotearthsolutions.expensetracker.callbacks.InAppBillingCallback;
import com.remotearthsolutions.expensetracker.utils.Constants;
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
        e.printStackTrace();
        if(response == ResponseCodes.ITEM_ALREADY_OWNED) {
            billingCallback.onPurchaseFailedListener(Constants.KEY_PURCHASED_ITEM_MESSAGE);
        }

    }
}