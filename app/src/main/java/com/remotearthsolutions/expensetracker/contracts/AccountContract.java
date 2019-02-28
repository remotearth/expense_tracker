package com.remotearthsolutions.expensetracker.contracts;

import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.AccountIncome;

import java.util.ArrayList;

public interface AccountContract {

    interface View {

        void onAccountFetch(ArrayList<AccountIncome> accounts);
    }
}
