package com.remotearth.expensetracker;

import android.content.Intent;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseUser;
import com.remotearthsolutions.expensetracker.contracts.LoginContract;
import com.remotearthsolutions.expensetracker.services.FacebookService;
import com.remotearthsolutions.expensetracker.services.FirebaseService;
import com.remotearthsolutions.expensetracker.services.GoogleService;
import com.remotearthsolutions.expensetracker.viewmodels.LoginViewModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LoginViewModelTests {

    @Mock
    LoginContract.View view;
    @Mock
    GoogleService googleService;
    @Mock
    FacebookService facebookService;
    @Mock
    FirebaseService firebaseService;

    @InjectMocks
    private LoginViewModel loginViewModel;

    @Before
    public void init() {
        Mockito.reset(view, googleService, facebookService, firebaseService);
    }

    @Test
    public void test_init_will_initializeView() {
        loginViewModel.init();
        verify(view, only()).initializeView();
        verify(googleService, only()).initializeGoogleSigninClient();
        verify(facebookService, times(1)).facebookCallbackInitialize();
    }

    @Test
    public void test_getFacebookCallbackManager_will_return_CallbackManager_object() {
        loginViewModel.getFacebookCallbackManager();
        verify(facebookService, only()).getFacebookCallbackManager();
    }

    @Test
    public void test_startFacebookLogin_when_deviceIsOnline_will_startFacebookLogin() {
        when(view.isDeviceOnline()).thenReturn(true);
        loginViewModel.startFacebookLogin();
        verify(facebookService, times(1)).startFacebookLogin(any());
    }

    @Test
    public void test_startFacebookLogin_when_deviceIsOffline_will_showNoInternetAlert() {
        when(view.isDeviceOnline()).thenReturn(false);
        loginViewModel.startFacebookLogin();
        verify(view, times(1)).showAlert("Warning", "No Internet Connection", "OK", null, null);
    }

    @Test
    public void test_startGoogleLogin_when_deviceIsOnline_will_startFacebookLogin() {
        when(view.isDeviceOnline()).thenReturn(true);
        loginViewModel.startGoogleLogin();
        verify(view, times(1)).loadUserEmails();
    }

    @Test
    public void test_startGoogleLogin_when_deviceIsOffline_will_showNoInternetAlert() {
        when(view.isDeviceOnline()).thenReturn(false);
        loginViewModel.startGoogleLogin();
        verify(view, times(1)).showAlert("Warning", "No Internet Connection", "OK", null, null);
    }

    @Test
    public void test_googleLoginWithIntent_with_Intent_will_startGoogleLogin() {
        Intent intent = mock(Intent.class);
        loginViewModel.googleLoginWithIntent(intent);
        verify(googleService, times(1)).startGoogleLogin(eq(intent), any());
    }

    @Test
    public void test_getGoogleSignInClient_will_return_GoogleSignInClient_object() {
        loginViewModel.getGoogleSignInClient();
        verify(googleService, only()).getGoogleSignInClient();
    }

    @Test
    public void test_onFirebaseSigninSuccess_with_FirebaseUser_will_call_hideProgress_and_onLoginSuccess() {
        FirebaseUser user = mock(FirebaseUser.class);
        loginViewModel.onFirebaseSigninSuccess(user);
        verify(view, times(1)).hideProgress();
        verify(view, times(1)).onLoginSuccess(eq(user));
    }

    @Test
    public void test_onFirebaseSigninFailure_with_Data_will_call_hideProgress_onLoginFailure_and_showAlert() {
        String str = "Sign in Failed";
        loginViewModel.onFirebaseSigninFailure(str);
        verify(view, times(1)).hideProgress();
        verify(view, times(1)).onLoginFailure();
        verify(view, times(1)).showAlert(null, "Sign in Failed", "Ok", null, null);
    }

    @Test
    public void test_onSocialLoginSuccess_with_AuthCredential_will_call_showProgress_and_signinWithCredential() {
        AuthCredential authCredential = mock(AuthCredential.class);
        loginViewModel.onSocialLoginSuccess(authCredential);
        verify(view, only()).showProgress("Please wait...");
        verify(firebaseService, only()).signinWithCredential(eq(authCredential), any());
    }

    @Test
    public void test_onSocialLoginFailure_with_StringData_will_call_showAlert() {
        loginViewModel.onSocialLoginFailure("Sign in Failed");
        verify(view, only()).showAlert(null, "Sign in Failed", "Ok", null, null);
    }

    @Test
    public void test_onFacebookLoginCancel_will_call_onFacebookLoginCancel() {
        loginViewModel.onFacebookLoginCancel();
        verify(view, only()).onLoginFailure();
    }


}
