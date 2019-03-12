package com.remotearth.expensetracker;

import android.app.Activity;
import android.content.Intent;
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
}
