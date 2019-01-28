package com.remotearthsolutions.expensetracker.contracts;

import com.google.firebase.auth.FirebaseUser;

public interface LoginContract {

    interface View extends BaseView {

        void initializeView();
        void onLoginSuccess(FirebaseUser user);
        void onLoginFailure();
    }

    interface Interactor {
    }

}
