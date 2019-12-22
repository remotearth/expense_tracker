package com.remotearthsolutions.expensetracker.viewmodels

import androidx.lifecycle.ViewModel
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.contracts.ExpenseFragmentContract.ExpenseView
import com.remotearthsolutions.expensetracker.databaseutils.daos.CategoryExpenseDao
import com.remotearthsolutions.expensetracker.databaseutils.daos.ExpenseDao
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.CategoryExpense
import com.remotearthsolutions.expensetracker.utils.DateTimeUtils
import com.remotearthsolutions.expensetracker.utils.DateTimeUtils.getDate
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class ExpenseViewModel(
    private val view: ExpenseView,
    private val expenseDao: ExpenseDao,
    private val categoryExpenseDao: CategoryExpenseDao
) : ViewModel() {
    private val disposable = CompositeDisposable()
    private var dateRangeBtnId = 0
    fun loadFilterExpense(startTime: Long, endTime: Long, btnId: Int) {
        disposable.add(
            categoryExpenseDao.getExpenseWithinRange(startTime, endTime)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { listOfFilterExpense: List<CategoryExpense> ->
                    val expenseList: MutableList<CategoryExpense> =
                        ArrayList()
                    if (listOfFilterExpense.isNotEmpty()) {
                        var previousDate = listOfFilterExpense[0].datetime
                        var previousMonth =
                            getDate(previousDate, DateTimeUtils.mmmm)
                        if (btnId != R.id.nextDateBtn && btnId != R.id.previousDateBtn) {
                            dateRangeBtnId = btnId
                        }
                        if (dateRangeBtnId == R.id.yearlyRangeBtn) {
                            val monthHeader = CategoryExpense()
                            monthHeader.isHeader = true
                            monthHeader.categoryName = previousMonth
                            expenseList.add(monthHeader)
                        }
                        if (dateRangeBtnId != R.id.dailyRangeBtn) {
                            val header = CategoryExpense()
                            header.isHeader = true
                            header.categoryName =
                                getDate(previousDate, DateTimeUtils.dd_MM_yyyy)
                            expenseList.add(header)
                        }
                        for (i in listOfFilterExpense.indices) {
                            val expense = listOfFilterExpense[i]
                            if (dateRangeBtnId == R.id.yearlyRangeBtn) {
                                val monthName =
                                    getDate(expense.datetime, DateTimeUtils.mmmm)
                                if (monthName != previousMonth) {
                                    val monthHeader = CategoryExpense()
                                    monthHeader.isHeader = true
                                    monthHeader.categoryName = monthName
                                    expenseList.add(monthHeader)
                                    previousMonth = monthName
                                }
                            }
                            if (dateRangeBtnId != R.id.dailyRangeBtn) {
                                if (getDate(
                                        expense.datetime,
                                        DateTimeUtils.dd_MM_yyyy
                                    ) != getDate(
                                        previousDate,
                                        DateTimeUtils.dd_MM_yyyy
                                    )
                                ) {
                                    val dummy = CategoryExpense()
                                    dummy.isHeader = true
                                    dummy.categoryName = getDate(
                                        expense.datetime,
                                        DateTimeUtils.dd_MM_yyyy
                                    )
                                    previousDate = expense.datetime
                                    expenseList.add(dummy)
                                }
                            }
                            expenseList.add(expense)
                        }
                    }
                    view.loadFilterExpense(expenseList)
                }
        )
    }

}