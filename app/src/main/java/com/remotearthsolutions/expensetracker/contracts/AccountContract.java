package com.remotearthsolutions.expensetracker.contracts;

import com.remotearthsolutions.expensetracker.databaseutils.models.AccountModel;

import java.util.List;

public interface AccountContract {

    interface View {

        void onAccountFetch(List<AccountModel> accounts);
        void onSuccess(String message);
    }
}
