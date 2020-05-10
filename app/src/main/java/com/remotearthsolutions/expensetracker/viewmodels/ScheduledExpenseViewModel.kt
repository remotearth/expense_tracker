package com.remotearthsolutions.expensetracker.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.remotearthsolutions.expensetracker.databaseutils.daos.ScheduledExpenseDao
import com.remotearthsolutions.expensetracker.databaseutils.models.ScheduledExpenseModel
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.ScheduledExpenseDto
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ScheduledExpenseViewModel(
    private val scheduledExpenseDao: ScheduledExpenseDao
) : ViewModel() {
    var scheduledExpensesLiveData = MutableLiveData<List<ScheduledExpenseDto>>()
    private val mDisposable = CompositeDisposable()

    fun loadScheduledExpenses() {
        mDisposable.add(
            scheduledExpenseDao.getScheduledExpensesWithCategoryAndAccount()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { listOfExpense: List<ScheduledExpenseDto> ->
                    scheduledExpensesLiveData.value = listOfExpense
                }
        )
    }

    fun removeScheduledExpense(scheduledExpense: ScheduledExpenseModel) {
        mDisposable.add(
            Completable.fromAction {
                scheduledExpenseDao.delete(scheduledExpense)
            }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    val list = scheduledExpensesLiveData.value!!
                    scheduledExpensesLiveData.value =
                        list.filter { it.id == scheduledExpense.id }
                }
        )
    }
}
