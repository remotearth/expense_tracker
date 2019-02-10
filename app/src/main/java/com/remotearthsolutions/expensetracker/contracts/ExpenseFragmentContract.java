package com.remotearthsolutions.expensetracker.contracts;

import com.remotearthsolutions.expensetracker.databaseutils.models.ExpenseModel;
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.AccountIncome;

import java.util.List;

public interface ExpenseFragmentContract {
    interface View extends BaseView {

        void defineClickListener();

        void onExpenseAdded();

        void setSourceAccount(AccountIncome account);

    }

    interface ExpenseView{

        void showExpense(List<ExpenseModel> expense);

    }


}
