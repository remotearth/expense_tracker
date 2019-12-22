package com.remotearthsolutions.expensetracker.utils

import android.content.Context
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.databaseutils.models.AccountModel
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel

object InitialDataGenerator {
    fun generateCategories(context: Context): Array<CategoryModel> {
        val resources = context.resources
        return arrayOf(
            CategoryModel(resources.getString(R.string.bills), "cat_bills", 1),
            CategoryModel(resources.getString(R.string.car), "cat_car", 1),
            CategoryModel(resources.getString(R.string.clothes), "cat_clothes", 1),
            CategoryModel(resources.getString(R.string.communication), "cat_communication", 1),
            CategoryModel(resources.getString(R.string.eating_out), "cat_eatingout", 1),
            CategoryModel(resources.getString(R.string.entertainment), "cat_entertainment", 1),
            CategoryModel(resources.getString(R.string.food), "cat_food", 1),
            CategoryModel(resources.getString(R.string.gift), "cat_gift", 1),
            CategoryModel(resources.getString(R.string.health), "cat_health", 1),
            CategoryModel(resources.getString(R.string.pets), "cat_pets", 1),
            CategoryModel(resources.getString(R.string.sports), "cat_sports", 1),
            CategoryModel(resources.getString(R.string.taxi), "cat_taxi", 1),
            CategoryModel(resources.getString(R.string.toiletry), "cat_toiletry", 1),
            CategoryModel(resources.getString(R.string.transport), "cat_transport", 1),
            CategoryModel(resources.getString(R.string.family), "cat_family", 1)
        )
    }

    fun generateAccounts(context: Context): Array<AccountModel> {
        val resources = context.resources
        return arrayOf(
            AccountModel(resources.getString(R.string.cash), "ic_cash1", 0.0, 1),
            AccountModel(resources.getString(R.string.bank), "ic_bank", 0.0, 1),
            AccountModel(resources.getString(R.string.loan), "ic_loan1", 0.0, 1)
        )
    }
}