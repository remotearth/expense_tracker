package com.remotearthsolutions.expensetracker.contracts;

import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel;
import com.remotearthsolutions.expensetracker.entities.ExpeneChartData;

import java.util.List;

public interface HomeFragmentContract {
    interface View {

        void showCategories(List<CategoryModel> categories);

        void loadExpenseChart(List<ExpeneChartData> listOfCategoryWithAmount);
    }
}
