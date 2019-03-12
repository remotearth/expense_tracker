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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MainViewModelTests {

    @Mock
    MainContract.View view;

    @Mock
    FirebaseUser firebaseUser;

    @Mock
    FirebaseService firebaseService;

    @Mock
    User user;

    @InjectMocks
    private MainViewModel viewModel;

    @Test
    public void test_checkauthentication_with_user_ifnull()
    {
        firebaseUser = firebaseService.getUser();
        viewModel.checkAuthectication(user);
        when(firebaseUser == null && user == null).thenReturn();
        view.goBackToLoginScreen();
        verify(view,times(1)).goBackToLoginScreen();
    }

    @Test
    public void test_checkauthentication_with_user_ifnotnull()
    {
        view.startLoadingApp();
        verify(view,times(1)).startLoadingApp();
    }

    @Test
    public void test_perform_logout()
    {
        view.onLogoutSuccess();
        verify(view,times(1)).onLogoutSuccess();
        firebaseService.logout();
        verify(firebaseService,times(1)).logout();
    }

}
