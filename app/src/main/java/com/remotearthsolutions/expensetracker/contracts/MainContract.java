package com.remotearthsolutions.expensetracker.contracts;

public interface MainContract {

    interface View {
        void initializeView();

        void goBackToLoginScreen();

        void onLogoutSuccess();

        void startLoadingApp();

        void showTotalExpense(String amount);

        void showTotalBalance(String amount);

        void stayOnCurrencyScreen();

        void setBalanceTextColor(int colorId);
    }
}
