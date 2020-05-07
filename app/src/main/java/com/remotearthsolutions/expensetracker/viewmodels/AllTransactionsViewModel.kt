package com.remotearthsolutions.expensetracker.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.databaseutils.daos.CategoryDao
import com.remotearthsolutions.expensetracker.databaseutils.daos.CategoryExpenseDao
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryExpense
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel
import com.remotearthsolutions.expensetracker.utils.DateTimeUtils
import com.remotearthsolutions.expensetracker.utils.DateTimeUtils.getDate
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiConsumer
import io.reactivex.schedulers.Schedulers
import java.util.*

class AllTransactionsViewModel(
    private val categoryExpenseDao: CategoryExpenseDao,
    private val categoryDao: CategoryDao,
    private var dateFormat: String
) : ViewModel() {
    private val disposable = CompositeDisposable()
    private var dateRangeBtnId = 0
    var chartDataRequirementLiveData = MutableLiveData<ChartDataRequirement>()
    var listOfCateogoryLiveData = MutableLiveData<List<CategoryModel>>()
    val expenseListLiveData = MutableLiveData<List<CategoryExpense>>()

    fun loadFilterExpense(startTime: Long, endTime: Long, btnId: Int) {
        disposable.add(
            categoryExpenseDao.getExpenseWithinRange(startTime, endTime)
                .subscribeOn(Schedulers.io())
                .subscribe { listOfFilterExpense: List<CategoryExpense> ->

                    val expenseList: MutableList<CategoryExpense> = ArrayList()
                    if (listOfFilterExpense.isNotEmpty()) {
                        var previousDate = listOfFilterExpense[0].datetime
                        var previousMonth = getDate(previousDate, DateTimeUtils.mmmm)
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
                            header.isDateSection = true
                            header.categoryName = getDate(previousDate, dateFormat)
                            expenseList.add(header)
                        }
                        for (i in listOfFilterExpense.indices) {
                            val expense = listOfFilterExpense[i]
                            if (dateRangeBtnId == R.id.yearlyRangeBtn) {
                                val monthName = getDate(expense.datetime, DateTimeUtils.mmmm)
                                if (monthName != previousMonth) {
                                    val monthHeader = CategoryExpense()
                                    monthHeader.isHeader = true
                                    monthHeader.categoryName = monthName
                                    expenseList.add(monthHeader)
                                    previousMonth = monthName
                                }
                            }
                            if (dateRangeBtnId != R.id.dailyRangeBtn) {
                                if (getDate(expense.datetime, dateFormat) !=
                                    getDate(previousDate, dateFormat)
                                ) {
                                    val dummy = CategoryExpense()
                                    dummy.isHeader = true
                                    dummy.isDateSection = true
                                    dummy.categoryName = getDate(expense.datetime, dateFormat)
                                    previousDate = expense.datetime
                                    expenseList.add(dummy)
                                }
                            }
                            expenseList.add(expense)
                        }
                    }
                    expenseListLiveData.postValue(expenseList)
                    this.chartDataRequirementLiveData.postValue(
                        ChartDataRequirement(
                            startTime,
                            endTime,
                            dateRangeBtnId,
                            listOfFilterExpense
                        )
                    )
                }
        )
    }

    fun getAllCategory() {
        disposable.add(
            categoryDao.allCategories
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { result, error ->
                    if (error == null) {
                        listOfCateogoryLiveData.value = result
                    }
                }
        )
    }

    fun updateDateFormat(updatedDateFormat: String) {
        this.dateFormat = updatedDateFormat
    }

    data class ChartDataRequirement(
        val startTime: Long,
        val endTime: Long,
        val selectedFilterBtnId: Int,
        val filteredList: List<CategoryExpense>
    )
}