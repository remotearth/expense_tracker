package com.remotearthsolutions.expensetracker.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.remotearthsolutions.expensetracker.contracts.HomeFragmentContract
import com.remotearthsolutions.expensetracker.databaseutils.daos.AccountDao
import com.remotearthsolutions.expensetracker.databaseutils.daos.CategoryDao
import com.remotearthsolutions.expensetracker.databaseutils.daos.CategoryExpenseDao
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryExpense
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel
import com.remotearthsolutions.expensetracker.entities.ExpenseChartData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class HomeFragmentViewModel(
    private val view: HomeFragmentContract.View,
    private val categoryExpenseDao: CategoryExpenseDao,
    private val categoryDao: CategoryDao,
    private val accountDao: AccountDao
) : ViewModel() {
    private val disposable = CompositeDisposable()
    fun init() {
        disposable.add(
            categoryDao.allCategories
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { categories: List<CategoryModel>? ->
                    view.showCategories(categories)
                }
        )
    }

    fun loadExpenseChart(startTime: Long, endTime: Long) {
        disposable.add(
            categoryExpenseDao.getAllCategoriesWithExpense(startTime, endTime)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { listOfCategoryWithAmount: List<CategoryExpense> ->
                    var sum = 0.0
                    for (expense in listOfCategoryWithAmount) {
                        sum += expense.totalAmount
                    }
                    val chartDataList: MutableList<ExpenseChartData> =
                        ArrayList()
                    for (expense in listOfCategoryWithAmount) {
                        val expensePercentVal = expense.totalAmount / sum * 100
                        if (expensePercentVal > 0) {
                            val data = ExpenseChartData(
                                expense.totalAmount,
                                expensePercentVal,
                                expense.categoryName!!
                            )
                            chartDataList.add(data)
                        }
                    }
                    view.loadExpenseChart(chartDataList)
                }
        )
    }

    val numberOfItem: LiveData<Int>
        get() = categoryDao.dataCount

}