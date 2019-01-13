package com.remotearthsolutions.expensetracker.services;

import com.facebook.CallbackManager;

public interface FacebookService {

    void facebookCallbackInitialize();
    void startFacebookLogin(CallBack callBack);
    CallbackManager getFacebookCallbackManager();


    interface CallBack extends SocialLoginCallback{
        void onFacebookLoginCancel();
    }
}
