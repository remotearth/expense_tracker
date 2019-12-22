package com.remotearthsolutions.expensetracker.contracts

import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel

interface CategoryFragmentContract {
    interface View {
        fun showCategories(categories: List<CategoryModel>?)
    }
}