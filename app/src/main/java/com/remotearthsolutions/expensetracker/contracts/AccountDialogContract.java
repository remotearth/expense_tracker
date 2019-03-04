package com.remotearthsolutions.expensetracker.contracts;

import com.remotearthsolutions.expensetracker.databaseutils.models.AccountModel;

import java.util.List;

public interface AccountDialogContract {
    interface View {
        void onAccountFetchSuccess(List<AccountModel> accounts);

        void onAccountFetchFailure();
    }
}
