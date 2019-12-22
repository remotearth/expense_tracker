package com.remotearthsolutions.expensetracker.contracts

interface MainContract {
    interface View {
        fun initializeView()
        fun goBackToLoginScreen()
        fun onLogoutSuccess()
        fun startLoadingApp()
        fun showTotalExpense(amount: String?)
        fun showTotalBalance(amount: String?)
        fun stayOnCurrencyScreen()
        fun setBalanceTextColor(colorId: Int)
    }
}