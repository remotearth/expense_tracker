package com.remotearthsolutions.expensetracker.services

import org.solovyev.android.checkout.Inventory
import org.solovyev.android.checkout.Inventory.Products

class InventoryCallback : Inventory.Callback {
    override fun onLoaded(products: Products) {}
}