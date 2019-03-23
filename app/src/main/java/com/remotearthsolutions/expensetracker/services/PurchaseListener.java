package com.remotearthsolutions.expensetracker.services;

import android.content.Context;
import android.util.Log;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.callbacks.InAppBillingCallback;
import com.remotearthsolutions.expensetracker.utils.Constants;
import org.solovyev.android.checkout.*;

public class PurchaseListener extends EmptyRequestListener<Purchase> {
    private Context context;
    private InAppBillingCallback billingCallback;

    public PurchaseListener(Context context,InAppBillingCallback billingCallback) {
        this.context = context;
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
            billingCallback.onPurchaseFailedListener(context.getResources().getString(R.string.already_purchased));
        }

    }
}