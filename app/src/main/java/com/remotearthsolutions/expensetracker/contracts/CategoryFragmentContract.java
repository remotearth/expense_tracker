package com.remotearthsolutions.expensetracker.contracts;

import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel;

import java.util.List;

public interface CategoryFragmentContract {
    interface View{
        void showCategories(List<CategoryModel> categories);
    }
}
