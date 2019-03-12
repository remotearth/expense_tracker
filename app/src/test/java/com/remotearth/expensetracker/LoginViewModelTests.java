package com.remotearth.expensetracker;

import android.app.Activity;
import android.content.Intent;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseUser;
import com.remotearthsolutions.expensetracker.contracts.LoginContract;
import com.remotearthsolutions.expensetracker.services.FacebookService;
import com.remotearthsolutions.expensetracker.services.FirebaseService;
import com.remotearthsolutions.expensetracker.services.GoogleService;
import com.remotearthsolutions.expensetracker.viewmodels.LoginViewModel;
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

    @Test
    public void test_googleLoginWithIntent_withIntentData_willStartGoogleLogin() {
        Intent intent = mock(Intent.class);
        loginViewModel.googleLoginWithIntent(intent);
        verify(googleService, times(1)).startGoogleLogin(eq(intent), any());
    }

    @Test
    public void test_startFacebookLogin_whenDeviceIsOnline_willStartFacebookLogin() {
        when(view.isDeviceOnline()).thenReturn(true);
        loginViewModel.startFacebookLogin();
        verify(facebookService, times(1)).startFacebookLogin(any());
    }

    @Test
    public void test_startFacebookLogin_whenDeviceIsOffline_willShowNoInternetAlert() {
        when(view.isDeviceOnline()).thenReturn(false);
        loginViewModel.startFacebookLogin();
        String title = "Warning";
        String message = "No Internet Connection";
        verify(view, times(1)).showAlert(title,
                message, "OK", null, null);
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
    public void test_onFirebaseSigninSuccess_with_FirebaseUser_call_hideProgress_and_onLoginSuccess() {
        FirebaseUser user = mock(FirebaseUser.class);
        loginViewModel.onFirebaseSigninSuccess(user);
        verify(view, times(1)).hideProgress();
        verify(view, times(1)).onLoginSuccess(eq(user));
    }

    @Test
    public void test_onFirebaseSigninFailure_with_StringData_call_hideProgress_onLoginFailure_and_showAlert() {
        loginViewModel.onFirebaseSigninFailure("Failed");
        verify(view, times(1)).hideProgress();
        verify(view, times(1)).onLoginFailure();
        verify(view, times(1)).showAlert(null, "Failed", "Ok", null, null);
    }



    @Test
    public void test_onSocialLoginSuccess_willshowProgressbar_with_signinWithCredential()
    {
        AuthCredential authCredential = mock(AuthCredential.class);
        loginViewModel.onSocialLoginSuccess(authCredential);
        verify(view, only()).showProgress("Please wait");
        verify(firebaseService, only()).signinWithCredential(eq(authCredential), any());
    }

    @Test
    public void test_onSocialLoginFailure_willshow_loginFailedAlert()
    {
        loginViewModel.onSocialLoginFailure("Login Failed");
        verify(view, only()).showAlert(null, "Login Failed", "Ok", null, null);

    }

    @Test
    public void test_onFacebookLoginCancel_whenLoginFailed_willstartonLoginFailure()
    {
        loginViewModel.onFacebookLoginCancel();
        verify(view, only()).onLoginFailure();
    }


}
