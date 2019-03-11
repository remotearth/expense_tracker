package com.remotearth.expensetracker;

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
    public void test_googleLoginWithIntent_withIntentData_will_startGoogleLogin() {
        loginViewModel.googleLoginWithIntent(null);
        Mockito.verify(googleService, Mockito.times(1)).startGoogleLogin(Mockito.any(), Mockito.any());
    }
}
