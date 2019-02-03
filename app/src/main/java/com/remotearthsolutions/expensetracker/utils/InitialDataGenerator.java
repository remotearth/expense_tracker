package com.remotearthsolutions.expensetracker.utils;

import com.remotearthsolutions.expensetracker.databaseutils.models.AccountModel;
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel;

public class InitialDataGenerator {

    public static CategoryModel[] generateCategories() {
        return new CategoryModel[]{
                new CategoryModel("Bills", "flat_icon"),
                new CategoryModel("Car", "flat_icon"),
                new CategoryModel("Cloths", "flat_icon"),
                new CategoryModel("Communication", "flat_icon"),
                new CategoryModel("Eating out", "flat_icon"),
                new CategoryModel("Entertainment", "flat_icon"),
                new CategoryModel("Food", "flat_icon"),
                new CategoryModel("Gift", "flat_icon"),
                new CategoryModel("Health", "flat_icon"),
                new CategoryModel("House", "flat_icon"),
                new CategoryModel("Pets", "flat_icon"),
                new CategoryModel("Sports", "flat_icon"),
                new CategoryModel("Taxi", "flat_icon"),
                new CategoryModel("Toiletry", "flat_icon"),
                new CategoryModel("Transport", "flat_icon"),
                new CategoryModel("Family", "flat_icon"),
        };
    }

    public static AccountModel[] generateAccounts() {
        return new AccountModel[]{
                new AccountModel("Cash", "flat_icon"),
                new AccountModel("Bank", "flat_icon"),
                new AccountModel("Loan", "flat_icon")
        };
    }
}
