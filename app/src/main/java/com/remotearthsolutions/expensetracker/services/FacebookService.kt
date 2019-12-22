package com.remotearthsolutions.expensetracker.services

import com.facebook.CallbackManager

interface FacebookService {
    fun facebookCallbackInitialize()
    fun startFacebookLogin(callBack: CallBack?)
    val facebookCallbackManager: CallbackManager?

    interface CallBack : SocialLoginCallback {
        fun onFacebookLoginCancel()
    }
}