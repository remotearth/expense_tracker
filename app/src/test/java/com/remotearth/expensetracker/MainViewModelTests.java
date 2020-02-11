package com.remotearth.expensetracker;

import androidx.lifecycle.LifecycleOwner;

import com.google.firebase.auth.FirebaseUser;
import com.remotearthsolutions.expensetracker.contracts.MainContract;
import com.remotearthsolutions.expensetracker.databaseutils.daos.AccountDao;
import com.remotearthsolutions.expensetracker.databaseutils.daos.CategoryDao;
import com.remotearthsolutions.expensetracker.databaseutils.daos.CategoryExpenseDao;
import com.remotearthsolutions.expensetracker.databaseutils.daos.ExpenseDao;
import com.remotearthsolutions.expensetracker.entities.User;
import com.remotearthsolutions.expensetracker.services.FileProcessingService;
import com.remotearthsolutions.expensetracker.services.FirebaseService;
import com.remotearthsolutions.expensetracker.viewmodels.MainViewModel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
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
    AccountDao accountDao;

    @Mock
    ExpenseDao expenseDao;

    @Mock
    CategoryDao categoryDao;

    @Mock
    CategoryExpenseDao categoryExpenseDao;

    @Mock
    FileProcessingService fileProcessingService;

    @InjectMocks
    private MainViewModel viewModel;

    //@Test
    public void test_init_will_initializeView() {
//        LiveData<Double> mockTotalAmount = new MutableLiveData<>();
//        when(accountDao.getTotalAmount()).thenReturn(mockTotalAmount);
        viewModel.init(mock(LifecycleOwner.class));
        verify(view, only()).initializeView();
        verify(accountDao, only()).getTotalAmount();

    }

    @Test
    public void test_checkAuthentication_when_guestUserAndFireBaseUserIsNull_will_goBackToLogin() {
        viewModel.checkAuthectication("");

        verify(view, times(1)).goBackToLoginScreen();
        verify(view, never()).startLoadingApp();
    }

    @Test
    public void test_checkAuthentication_when_guestUserIsNull_AndFireBaseUserIsNotNull_will_startLoadingApp() {
        viewModel.checkAuthectication("validUserId");

        verify(view, times(1)).startLoadingApp();
        verify(view, never()).goBackToLoginScreen();
    }

    @Test
    public void test_checkAuthentication_when_guestUserIsNotNull_AndFireBaseUserIsNull_will_startLoadingApp() {
        viewModel.checkAuthectication("guest");

        verify(view, times(1)).startLoadingApp();
        verify(view, never()).goBackToLoginScreen();
    }

    @Test
    public void test_checkAuthentication_when_guestUserAndFireBaseUserNotNull_will_starLoadingApp() {
        viewModel.checkAuthectication("guest");

        verify(view, times(1)).startLoadingApp();
        verify(view, never()).goBackToLoginScreen();
    }

    @Test
    public void test_performLogout_will_CallLogoutAndOnLogoutSuccess() {
        viewModel.performLogout();

        verify(firebaseService, times(1)).logout();
        verify(view, times(1)).onLogoutSuccess();
    }

}
