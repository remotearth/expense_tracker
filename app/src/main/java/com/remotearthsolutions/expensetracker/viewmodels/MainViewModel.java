package com.remotearthsolutions.expensetracker.viewmodels;


import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;
import com.google.firebase.auth.FirebaseUser;
import com.remotearthsolutions.expensetracker.contracts.MainContract;
import com.remotearthsolutions.expensetracker.databaseutils.daos.AccountDao;
import com.remotearthsolutions.expensetracker.databaseutils.daos.ExpenseDao;
import com.remotearthsolutions.expensetracker.entities.User;
import com.remotearthsolutions.expensetracker.services.FirebaseService;
import com.remotearthsolutions.expensetracker.utils.Utils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


public class MainViewModel extends ViewModel {

    private MainContract.View view;
    private FirebaseService firebaseService;
    private AccountDao accountDao;
    private ExpenseDao expenseDao;
    CompositeDisposable disposable = new CompositeDisposable();
    private long startTime;
    private long endTime;

    public MainViewModel(MainContract.View view, FirebaseService firebaseService, AccountDao accountDao, ExpenseDao expenseDao) {
        this.view = view;
        this.firebaseService = firebaseService;
        this.accountDao = accountDao;
        this.expenseDao = expenseDao;
    }

    public void init(LifecycleOwner lifecycleOwner) {
        view.initializeView();
        accountDao.getTotalAmount().observe(lifecycleOwner, amount -> {
            if (amount != null) {
                view.showTotalBalance(Utils.formatDecimalValues(amount));
            } else {
                view.showTotalBalance(Utils.formatDecimalValues(0.0));
            }

            if (amount != null && amount < 0) {
                view.setBalanceTextColor(android.R.color.holo_red_dark);
            } else {
                view.setBalanceTextColor(android.R.color.holo_green_light);
            }
        });
    }

    public void updateSummary(long startTime, long endTime) {
        this.startTime = startTime;
        this.endTime = endTime;

        disposable.add(expenseDao.getTotalAmountInDateRange(startTime, endTime)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((amount, throwable) -> {
                    if (throwable == null) {
                        view.showTotalExpense(Utils.formatDecimalValues(amount));
                    } else {
                        throwable.printStackTrace();
                        view.showTotalExpense(Utils.formatDecimalValues(0.0));
                    }
                }));
    }

    public void updateSummary() {
        updateSummary(startTime, endTime);
    }

    public void checkAuthectication(User guestUser) {

        FirebaseUser user = firebaseService.getUser();

        if (user == null && guestUser == null) {
            view.goBackToLoginScreen();
        } else {
            view.startLoadingApp();
        }
    }

    public void performLogout() {
        firebaseService.logout();
        view.onLogoutSuccess();
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }
}
