package com.remotearthsolutions.expensetracker.contracts

import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel
import com.remotearthsolutions.expensetracker.entities.ExpeneChartData

interface HomeFragmentContract {
    interface View {
        fun showCategories(categories: List<CategoryModel>?)
        fun loadExpenseChart(listOfCategoryWithAmount: List<ExpeneChartData>?)
    }
}