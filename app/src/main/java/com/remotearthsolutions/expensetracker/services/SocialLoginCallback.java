package com.remotearthsolutions.expensetracker.services;

import com.google.firebase.auth.AuthCredential;

public interface SocialLoginCallback {
    void onSocialLoginSuccess(AuthCredential credential);
    void onSocialLoginFailure(String message);
}
