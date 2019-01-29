package com.remotearthsolutions.expensetracker.contracts;

public interface ExpenseFragmentContract {
    interface View{

        void defineClickListener();

        void onExpenseAdded();
    }
}
