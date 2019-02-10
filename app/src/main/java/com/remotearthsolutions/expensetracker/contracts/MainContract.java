package com.remotearthsolutions.expensetracker.contracts;

public interface MainContract {

    interface View {
        void initializeView();

        void goBackToLoginScreen();

        void onLogoutSuccess();

    }
}
