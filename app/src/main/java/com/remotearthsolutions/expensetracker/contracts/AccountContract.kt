package com.remotearthsolutions.expensetracker.contracts

import com.remotearthsolutions.expensetracker.databaseutils.models.AccountModel

interface AccountContract {
    interface View {
        fun onAccountFetch(accounts: List<AccountModel>?)
        fun onSuccess(message: String?)
        fun onDeleteAccount()

    }
}