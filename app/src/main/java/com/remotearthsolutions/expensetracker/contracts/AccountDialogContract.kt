package com.remotearthsolutions.expensetracker.contracts

import com.remotearthsolutions.expensetracker.databaseutils.models.AccountModel

interface AccountDialogContract {
    interface View {
        fun onAccountFetchSuccess(accounts: List<AccountModel>?)
        fun onAccountFetchFailure()
    }
}