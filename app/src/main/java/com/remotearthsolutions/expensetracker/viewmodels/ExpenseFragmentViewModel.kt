package com.remotearthsolutions.expensetracker.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.contracts.ExpenseFragmentContract
import com.remotearthsolutions.expensetracker.databaseutils.daos.AccountDao
import com.remotearthsolutions.expensetracker.databaseutils.daos.CategoryDao
import com.remotearthsolutions.expensetracker.databaseutils.daos.ExpenseDao
import com.remotearthsolutions.expensetracker.databaseutils.models.AccountModel
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel
import com.remotearthsolutions.expensetracker.databaseutils.models.ExpenseModel
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryExpense
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlin.math.abs

class ExpenseFragmentViewModel(
    private val context: Context,
    private val view: ExpenseFragmentContract.View,
    private val expenseDao: ExpenseDao,
    private val accountDao: AccountDao,
    private val categoryDao: CategoryDao
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    fun init() {
        view.defineClickListener()
    }

    fun setDefaultSourceAccount(accountId: Int) {
        compositeDisposable.add(
            accountDao.getAccountById(accountId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe { accountIncome: AccountModel? ->
                    if (accountIncome != null) {
                        view.setSourceAccount(accountIncome)
                    }
                }
        )
    }

    fun addExpense(expense: ExpenseModel) {
        if (abs(expense.amount) != 0.0) {
            compositeDisposable.add(Completable.fromAction {
                if (expense.id > 0) {
                    expenseDao.updateExpenseAmount(expense)
                } else {
                    expenseDao.add(expense)
                }
            }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { view.onExpenseAdded(expense.amount) }
            )
        } else {
            view.showToast(context.getString(R.string.please_enter_an_amount))
        }
    }

    fun updateAccountAmount(accountId: Int, amount: Double) {
        compositeDisposable.add(
            accountDao.getAccountById(accountId)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe { accountModel: AccountModel ->
                    var previousAmount = accountModel.amount
                    previousAmount -= amount
                    accountModel.amount = previousAmount
                    Completable.fromAction {
                        accountDao.updateAccount(
                            accountModel
                        )
                    }.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {}
                }
        )
    }

    fun setDefaultCategory() {
        compositeDisposable.add(
            categoryDao.allCategories
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe { categoryModels: List<CategoryModel?>? ->
                    if (categoryModels != null && categoryModels.isNotEmpty()) {
                        val categoryModel = categoryModels[0]
                        view.showDefaultCategory(categoryModel)
                    }
                }
        )
    }

    fun deleteExpense(categoryExpense: CategoryExpense?) {
        if (categoryExpense == null) {
            return
        }
        compositeDisposable.add(Completable.fromAction {
            expenseDao.delete(categoryExpense.expenseId)
        }.subscribeOn(
            Schedulers.io()
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { view.onExpenseDeleted(categoryExpense) }
        )
    }
}