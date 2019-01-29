package com.remotearthsolutions.expensetracker.contracts;

import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.AccountIncome;

import java.util.List;

public interface AccountDialogContract {
    interface View {
        void onAccountFetchSuccess(List<AccountIncome> accounts);

        void onAccountFetchFailure();
    }
}
