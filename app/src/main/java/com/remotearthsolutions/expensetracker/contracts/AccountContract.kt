package com.remotearthsolutions.expensetracker.contracts

interface AccountContract {
    interface View {
        fun onSuccess(message: String?)
        fun onUpdateAccount(isNewAccount: Boolean?)
        fun onDeleteAccount()
    }
}
