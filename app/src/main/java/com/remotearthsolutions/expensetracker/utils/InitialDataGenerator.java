package com.remotearthsolutions.expensetracker.utils;

import com.remotearthsolutions.expensetracker.databaseutils.models.AccountModel;
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel;

public class InitialDataGenerator {

    public static CategoryModel[] generateCategories() {
        return new CategoryModel[]{
                new CategoryModel("Bills", "cat_bills"),
                new CategoryModel("Car", "cat_car"),
                new CategoryModel("Cloths", "cat_clothes"),
                new CategoryModel("Communication", "cat_communication"),
                new CategoryModel("Eating out", "cat_eatingout"),
                new CategoryModel("Entertainment", "cat_entertainment"),
                new CategoryModel("Food", "cat_food"),
                new CategoryModel("Gift", "cat_gift"),
                new CategoryModel("Health", "cat_health"),
                new CategoryModel("House", "cat_pets"),
                new CategoryModel("Pets", "cat_pets"),
                new CategoryModel("Sports", "cat_sports"),
                new CategoryModel("Taxi", "cat_taxi"),
                new CategoryModel("Toiletry", "cat_toiletry"),
                new CategoryModel("Transport", "cat_transport"),
                new CategoryModel("Family", "cat_family"),
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
