package com.remotearthsolutions.expensetracker.utils;

import android.content.Context;
import android.content.res.Resources;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.databaseutils.models.AccountModel;
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel;

public class InitialDataGenerator {

    public static CategoryModel[] generateCategories(Context context) {
        Resources resources = context.getResources();
        return new CategoryModel[]{
                new CategoryModel(resources.getString(R.string.bills), "cat_bills", 1),
                new CategoryModel(resources.getString(R.string.car), "cat_car", 1),
                new CategoryModel(resources.getString(R.string.clothes), "cat_clothes", 1),
                new CategoryModel(resources.getString(R.string.communication), "cat_communication", 1),
                new CategoryModel(resources.getString(R.string.eating_out), "cat_eatingout", 1),
                new CategoryModel(resources.getString(R.string.entertainment), "cat_entertainment", 1),
                new CategoryModel(resources.getString(R.string.food), "cat_food", 1),
                new CategoryModel(resources.getString(R.string.gift), "cat_gift", 1),
                new CategoryModel(resources.getString(R.string.health), "cat_health", 1),
                new CategoryModel(resources.getString(R.string.pets), "cat_pets", 1),
                new CategoryModel(resources.getString(R.string.sports), "cat_sports", 1),
                new CategoryModel(resources.getString(R.string.taxi), "cat_taxi", 1),
                new CategoryModel(resources.getString(R.string.toiletry), "cat_toiletry", 1),
                new CategoryModel(resources.getString(R.string.transport), "cat_transport", 1),
                new CategoryModel(resources.getString(R.string.family), "cat_family", 1)
        };
    }

    public static AccountModel[] generateAccounts(Context context) {
        Resources resources = context.getResources();
        return new AccountModel[]{
                new AccountModel(resources.getString(R.string.cash), "ic_cash1", 0, 1),
                new AccountModel(resources.getString(R.string.bank), "ic_bank", 0, 1),
                new AccountModel(resources.getString(R.string.loan), "ic_loan1", 0, 1)
        };
    }
}
