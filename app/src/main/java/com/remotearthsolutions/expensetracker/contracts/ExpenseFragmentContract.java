package com.remotearthsolutions.expensetracker.contracts;

import com.remotearthsolutions.expensetracker.databaseutils.models.AccountModel;
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel;
import com.remotearthsolutions.expensetracker.databaseutils.models.DateModel;
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.CategoryExpense;

import java.util.List;

public interface ExpenseFragmentContract {
    interface View extends BaseView {

        void defineClickListener();

        void onExpenseAdded();

        void setSourceAccount(AccountModel account);

        void showDefaultCategory(CategoryModel categoryModel);
    }

    interface ExpenseView {

        void loadFilterExpense(List<CategoryExpense> listOffilterExpense);

        void loadDate(List<DateModel> listOfDate);
    }


}
