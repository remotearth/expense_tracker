package com.remotearthsolutions.expensetracker.viewmodels

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.remotearthsolutions.expensetracker.contracts.ExpenseFragmentContract
import com.remotearthsolutions.expensetracker.databaseutils.daos.*
import com.remotearthsolutions.expensetracker.databaseutils.models.*
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.CategoryExpense
import io.reactivex.Completable
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlin.math.abs

class ExpenseFragmentViewModel(
    private val view: ExpenseFragmentContract.View,
    private val expenseDao: ExpenseDao,
    private val accountDao: AccountDao,
    private val categoryDao: CategoryDao,
    private val scheduleExpenseDao: ScheduledExpenseDao,
    private val workerIdDao: WorkerIdsDao
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    fun init() {
        view.defineClickListener()
    }

    fun setDefaultSourceAccount(accountId: Int) {
        compositeDisposable.add(
            accountDao.getAccountById(accountId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { accountIncome: AccountModel? ->
                    if (accountIncome != null) {
                        view.setSourceAccount(accountIncome)
                    }
                }
        )
    }

    fun addExpense(expense: ExpenseModel) {
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
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { categoryModels: List<CategoryModel?>? ->
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
    fun requestToReviewApp(numberOfEntryNeeded: Int, callback: () -> Unit) {
        expenseDao.getNumberOfExpenseEntry()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : SingleObserver<Int> {
                override fun onSuccess(count: Int) {

                    if (count > numberOfEntryNeeded) {
                        callback.invoke()
                    }
                }

                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                    FirebaseCrashlytics.getInstance().recordException(e)
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

        var rowId: Long = -100
        compositeDisposable.add(Completable.fromAction {
            rowId = scheduleExpenseDao.add(scheduledExpenseModel).blockingGet()
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe {
                scheduledExpenseModel.id = rowId
                view.onScheduleExpense(scheduledExpenseModel)
            }
        )
    }

    fun saveWorkerId(workerIdModel: WorkerIdModel) {
        compositeDisposable.add(Completable.fromAction {
            workerIdDao.add(workerIdModel)
        }.subscribeOn(Schedulers.io()).subscribe())

    }
}
