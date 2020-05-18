package com.remotearthsolutions.expensetracker.contracts

interface AccountContract {
    interface View {
        fun onSuccess(message: String?)
        fun onUpdateAccount()
        fun onDeleteAccount()
    }
}
