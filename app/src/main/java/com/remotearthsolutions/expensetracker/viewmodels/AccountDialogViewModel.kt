package com.remotearthsolutions.expensetracker.viewmodels

import androidx.lifecycle.ViewModel
import com.remotearthsolutions.expensetracker.contracts.AccountDialogContract
import com.remotearthsolutions.expensetracker.databaseutils.daos.AccountDao
import com.remotearthsolutions.expensetracker.databaseutils.models.AccountModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class AccountDialogViewModel(
    var view: AccountDialogContract.View,
    var accountDao: AccountDao
) : ViewModel() {
    var mDisposable = CompositeDisposable()
    fun init() {}
    fun loadAccounts() {
        mDisposable.add(
            accountDao.allAccounts
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { listOfAccounts: List<AccountModel>? ->
                    view.onAccountFetchSuccess(
                        listOfAccounts
                    )
                }
        )
    }

}