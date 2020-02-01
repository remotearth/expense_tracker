package com.remotearthsolutions.expensetracker.contracts

import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel
import com.remotearthsolutions.expensetracker.entities.ExpenseChartData

interface HomeFragmentContract {
    interface View {
        fun showCategories(categories: List<CategoryModel>?)
        fun loadExpenseChart(listOfCategoryWithAmount: List<ExpenseChartData>?)
    }
}