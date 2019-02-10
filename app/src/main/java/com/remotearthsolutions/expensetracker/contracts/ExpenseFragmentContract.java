package com.remotearthsolutions.expensetracker.contracts;

import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel;
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.AccountIncome;

public interface ExpenseFragmentContract {
    interface View extends BaseView {

        void defineClickListener();

        void onExpenseAdded();

        void setSourceAccount(AccountIncome account);

        void showDefaultCategory(CategoryModel categoryModel);
    }
}
