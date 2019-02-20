package com.remotearthsolutions.expensetracker.services;

import org.solovyev.android.checkout.EmptyRequestListener;
import org.solovyev.android.checkout.Purchase;

public class PurchaseListener extends EmptyRequestListener<Purchase> {
    @Override
    public void onSuccess(Purchase purchase) {

    }

    @Override
    public void onError(int response, Exception e) {

    }
}