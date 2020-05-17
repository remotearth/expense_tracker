package com.remotearthsolutions.expensetracker.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.contracts.ExpenseFragmentContract
import com.remotearthsolutions.expensetracker.databaseutils.daos.AccountDao
import com.remotearthsolutions.expensetracker.databaseutils.daos.CategoryDao
import com.remotearthsolutions.expensetracker.databaseutils.daos.ExpenseDao
import com.remotearthsolutions.expensetracker.databaseutils.daos.ScheduledExpenseDao
import com.remotearthsolutions.expensetracker.databaseutils.models.*
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.CategoryExpense
import com.remotearthsolutions.expensetracker.utils.Constants
import io.reactivex.Completable
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlin.math.abs

class ExpenseFragmentViewModel(
    private val context: Context,
    private val view: ExpenseFragmentContract.View,
    private val expenseDao: ExpenseDao,
    private val accountDao: AccountDao,
    private val categoryDao: CategoryDao,
    private val scheduleExpenseDao: ScheduledExpenseDao
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

    @SuppressLint("CheckResult")
    fun requestToReviewApp(callback: () -> Unit) {
        expenseDao.getNumberOfExpenseEntry()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : SingleObserver<Int> {
                override fun onSuccess(count: Int) {
                    if (count > Constants.NUMBER_OF_ENTRY_NEEDED_BEFORE_ASKING_TO_REVIEW) {
                        callback.invoke()
                    }
                }

                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }

            })
    }

    fun scheduleExpense(
        expenseModel: ExpenseModel,
        period: Int,
        repeatType: Int,
        repeatCount: Int,
        nextOccurrenceDate: Long
    ) {
        val scheduledExpenseModel = ScheduledExpenseModel(
            period,
            repeatType,
            repeatCount,
            nextOccurrenceDate,
            expenseModel.categoryId,
            expenseModel.source,
            expenseModel.amount,
            expenseModel.note
        )
        if (abs(expenseModel.amount) == 0.0) {
            return
        }

        var rowId:Long = -100
        compositeDisposable.add(Completable.fromAction {
            rowId = scheduleExpenseDao.add(scheduledExpenseModel).blockingGet()
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe {
                scheduledExpenseModel.id = rowId
                view.onScheduleExpense(scheduledExpenseModel)
            }
        )
    }
}
