package com.remotearthsolutions.expensetracker.utils;

import com.remotearthsolutions.expensetracker.databaseutils.models.AccountModel;
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel;

public class InitialDataGenerator {

    public static CategoryModel[] generateCategories() {
        return new CategoryModel[]{
                new CategoryModel("Bills", "cat_bills", 1),
                new CategoryModel("Car", "cat_car", 1),
                new CategoryModel("Cloths", "cat_clothes", 1),
                new CategoryModel("Communication", "cat_communication", 1),
                new CategoryModel("Eating out", "cat_eatingout", 1),
                new CategoryModel("Entertainment", "cat_entertainment", 1),
                new CategoryModel("Food", "cat_food", 1),
                new CategoryModel("Gift", "cat_gift", 1),
                new CategoryModel("Health", "cat_health", 1),
                new CategoryModel("Pets", "cat_pets", 1),
                new CategoryModel("Sports", "cat_sports", 1),
                new CategoryModel("Taxi", "cat_taxi", 1),
                new CategoryModel("Toiletry", "cat_toiletry", 1),
                new CategoryModel("Transport", "cat_transport", 1),
                new CategoryModel("Family", "cat_family", 1)
        };
    }

    public static AccountModel[] generateAccounts() {
        return new AccountModel[]{
                new AccountModel("Cash", "cat_taxi", 0, 1),
                new AccountModel("Bank", "cat_family", 0, 1),
                new AccountModel("Loan", "cat_transport", 0, 1)
        };
    }
}
