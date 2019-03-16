package com.remotearthsolutions.expensetracker.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.remotearthsolutions.expensetracker.contracts.AccountContract;
import com.remotearthsolutions.expensetracker.databaseutils.daos.AccountDao;
import com.remotearthsolutions.expensetracker.databaseutils.models.AccountModel;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class AccountViewModel extends ViewModel {

    private AccountContract.View view;
    private AccountDao accountDao;
    private CompositeDisposable mDisposable = new CompositeDisposable();

    public AccountViewModel(AccountContract.View view, AccountDao accountDao) {
        this.view = view;
        this.accountDao = accountDao;
    }

    public void loadAccounts() {
        mDisposable.add(accountDao.getAllAccounts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        listOfAccount -> view.onAccountFetch(listOfAccount)
                ));
    }

    public void addOrUpdateAccount(AccountModel accountIncome) {
        if (accountIncome == null) {
            return;
        }

        mDisposable.add(Completable.fromAction(() -> {
            if (accountIncome.getId() > 0) {
                accountDao.updateAccount(accountIncome);
            } else {
                accountDao.addAccount(accountIncome);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> view.onSuccess("Operation successful")));
    }

    public void deleteAccount(AccountModel selectAccountIncome) {
        if (selectAccountIncome == null) {
            return;
        }

        mDisposable.add(Completable.fromAction(() -> {
            accountDao.deleteAccount(selectAccountIncome);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> view.onSuccess("Operation successful")));
    }

    public LiveData<Integer> getNumberOfItem() {
        return accountDao.countAccount();
    }

}
