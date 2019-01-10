package com.remotearthsolutions.expensetracker.contracts;

public interface LoginContract {
    interface View {
        void initializeView();
        void facebookInitialize();

        void onTokenGenerated(String token);
    }

    interface Interactor {
    }

}
