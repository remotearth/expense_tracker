package com.remotearthsolutions.expensetracker.utils;

import android.app.Activity;
import com.remotearthsolutions.expensetracker.activities.ApplicationObject;
import org.solovyev.android.checkout.*;

public class CheckoutUtils {

    private ActivityCheckout mCheckout;
    private static CheckoutUtils instance;

    public static CheckoutUtils getInstance(Activity activity) {
        if (instance == null) {
            instance = new CheckoutUtils(activity);
        }

        return instance;
    }

    private CheckoutUtils(Activity activity) {
        mCheckout = Checkout.forActivity(activity, ApplicationObject.get().getBilling());
    }

    public void start() {
        mCheckout.start();
    }

    public void stop() {
        mCheckout.stop();
    }

    public void createPurchaseFlow(EmptyRequestListener<Purchase> purchaseEmptyRequestListener) {
        mCheckout.createPurchaseFlow(purchaseEmptyRequestListener);
    }

    public PurchaseFlow getPurchaseFlow() {
        return mCheckout.getPurchaseFlow();
    }

    public void load(Inventory.Callback callback, String... productId) {
        Inventory mInventory = mCheckout.makeInventory();
        mInventory.load(Inventory.Request.create()
                .loadAllPurchases()
                .loadSkus(ProductTypes.IN_APP, productId), callback);
    }

    public ActivityCheckout getCheckout() {
        return mCheckout;
    }
}
