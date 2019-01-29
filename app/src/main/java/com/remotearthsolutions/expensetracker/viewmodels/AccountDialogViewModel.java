package com.remotearthsolutions.expensetracker.viewmodels;

import androidx.lifecycle.ViewModel;
import com.remotearthsolutions.expensetracker.contracts.AccountDialogContract;
import com.remotearthsolutions.expensetracker.databaseutils.daos.AccountDao;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class AccountDialogViewModel extends ViewModel {

    AccountDialogContract.View view;
    AccountDao accountDao;
    CompositeDisposable mDisposable = new CompositeDisposable();

    public AccountDialogViewModel(AccountDialogContract.View view, AccountDao accountDao) {
        this.view = view;
        this.accountDao = accountDao;
    }

    public void init() {
    }

    public void loadAccounts() {
        mDisposable.add(accountDao.getAllAccountsWithAmount()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
                listOfAccounts -> view.onAccountFetchSuccess(listOfAccounts)
        ));
    }
}
