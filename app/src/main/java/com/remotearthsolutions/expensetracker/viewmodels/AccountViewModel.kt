package com.remotearthsolutions.expensetracker.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.contracts.AccountContract
import com.remotearthsolutions.expensetracker.databaseutils.daos.AccountDao
import com.remotearthsolutions.expensetracker.databaseutils.models.AccountModel
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class AccountViewModel(
    private val context: Context,
    private val view: AccountContract.View,
    private val accountDao: AccountDao
) : ViewModel() {
    private val mDisposable = CompositeDisposable()
    fun loadAccounts() {
        mDisposable.add(
            accountDao.allAccounts
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { listOfAccount: List<AccountModel>? ->
                    view.onAccountFetch(listOfAccount)
                }
        )
    }

    fun addOrUpdateAccount(accountIncome: AccountModel?) {
        if (accountIncome == null) {
            return
        }
        mDisposable.add(Completable.fromAction {
            if (accountIncome.id > 0) {
                accountDao.updateAccount(accountIncome)
            } else {
                accountDao.addAccount(accountIncome)
            }
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { view.onSuccess(context.getString(R.string.operation_successful)) }
        )
    }

    fun deleteAccount(selectAccountIncome: AccountModel?) {
        if (selectAccountIncome == null) {
            return
        }
        mDisposable.add(Completable.fromAction {
            accountDao.deleteAccount(
                selectAccountIncome
            )
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { view.onSuccess(context.getString(R.string.operation_successful)) }
        )
    }

    val numberOfItem: LiveData<Int>
        get() = accountDao.countAccount()

}