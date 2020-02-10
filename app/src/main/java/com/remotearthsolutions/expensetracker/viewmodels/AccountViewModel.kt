package com.remotearthsolutions.expensetracker.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.contracts.AccountContract
import com.remotearthsolutions.expensetracker.databaseutils.daos.AccountDao
import com.remotearthsolutions.expensetracker.databaseutils.daos.ExpenseDao
import com.remotearthsolutions.expensetracker.databaseutils.models.AccountModel
import com.remotearthsolutions.expensetracker.utils.Response
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class AccountViewModel(
    private val context: Context,
    private val view: AccountContract.View,
    private val accountDao: AccountDao,
    private val expenseDao: ExpenseDao
) : ViewModel() {
    var listOfAccountLiveData = MutableLiveData<List<AccountModel>>()

    private val mDisposable = CompositeDisposable()
    fun loadAccounts() {
        mDisposable.add(
            accountDao.allAccounts
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { listOfAccount: List<AccountModel>? ->
                    listOfAccountLiveData.value = listOfAccount
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
            .subscribe { }
        )
    }

    fun deleteAccount(selectAccountIncome: AccountModel?) {
        if (selectAccountIncome == null) {
            return
        }
        mDisposable.add(Completable.fromAction {
            accountDao.deleteAccount(selectAccountIncome)
            expenseDao.deleteExpensesOfAccounts(selectAccountIncome.id)
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                view.onSuccess(context.getString(R.string.operation_successful))
                view.onDeleteAccount()
            }
        )
    }

    fun transferAmount(
        amount: Double,
        fromAccountSelection: Int,
        toAccountSelection: Int
    ): Response {
        if (amount == 0.0) {
            return Response(
                Response.FAILURE,
                context.getString(R.string.enter_amount_greater_than_zero)
            )
        }

        if (fromAccountSelection == toAccountSelection) {
            return Response(Response.FAILURE, context.getString(R.string.select_different_accounts))
        }

        val listOfAccount = listOfAccountLiveData.value
        if (amount > listOfAccount!![fromAccountSelection].amount) {
            return Response(
                Response.FAILURE,
                context.getString(R.string.source_acc_not_have_amount)
            )
        }

        val fromAccount = listOfAccount[fromAccountSelection]
        fromAccount.amount -= amount
        val toAccount = listOfAccount[toAccountSelection]
        toAccount.amount += amount

        addOrUpdateAccount(fromAccount)
        addOrUpdateAccount(toAccount)
        return Response(Response.SUCCESS, null)
    }

    val numberOfItem: LiveData<Int>
        get() = accountDao.countAccount()

}