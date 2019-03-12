package com.remotearth.expensetracker;

import com.google.firebase.auth.FirebaseUser;
import com.remotearthsolutions.expensetracker.contracts.MainContract;
import com.remotearthsolutions.expensetracker.entities.User;
import com.remotearthsolutions.expensetracker.services.FirebaseService;
import com.remotearthsolutions.expensetracker.viewmodels.MainViewModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MainViewModelTests {

    @Mock
    MainContract.View view;

    @Mock
    FirebaseUser firebaseUser;

    @Mock
    FirebaseService firebaseService;

    @InjectMocks
    private MainViewModel viewModel;

    @Test
    public void test_init_willInitializeView() {
        viewModel.init();
        verify(view, only()).initializeView();
    }

    @Test
    public void test_checkauthentication_when_guestUserAndFirebaseUserIsNull_willGoBackToLogin() {
        when(firebaseService.getUser()).thenReturn(null);
        viewModel.checkAuthectication(null);
        verify(view, times(1)).goBackToLoginScreen();
        verify(view, never()).startLoadingApp();
    }

    @Test
    public void test_checkauthentication_when_guestUserIsNull_AndFirebaseUserIsNotNull_willStartLoadingApp() {
        when(firebaseService.getUser()).thenReturn(firebaseUser);
        viewModel.checkAuthectication(null);
        verify(view, times(1)).startLoadingApp();
        verify(view, never()).goBackToLoginScreen();
    }

    @Test
    public void test_checkauthentication_when_guestUserIsNotNull_AndFirebaseUserIsNull_willStartLoadingApp() {
        when(firebaseService.getUser()).thenReturn(null);
        viewModel.checkAuthectication(new User());
        verify(view, times(1)).startLoadingApp();
        verify(view, never()).goBackToLoginScreen();
    }

    @Test
    public void test_checkauthentication_when_guestUserAndFirebaseUserNotNull_willStartLoadingApp() {
        when(firebaseService.getUser()).thenReturn(firebaseUser);
        viewModel.checkAuthectication(new User());
        verify(view, times(1)).startLoadingApp();
        verify(view, never()).goBackToLoginScreen();
    }

    @Test
    public void test_performlogout_willCallLogoutAndOnLogoutSuccess() {
        viewModel.performLogout();
        verify(firebaseService, times(1)).logout();
        verify(view, times(1)).onLogoutSuccess();
    }

}
