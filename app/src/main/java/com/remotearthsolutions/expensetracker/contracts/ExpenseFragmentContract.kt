package com.remotearthsolutions.expensetracker.contracts

import com.remotearthsolutions.expensetracker.databaseutils.models.AccountModel
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel
import com.remotearthsolutions.expensetracker.databaseutils.models.DateModel
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.CategoryExpense

interface ExpenseFragmentContract {
    interface View : BaseView {
        fun defineClickListener()
        fun onExpenseAdded(amount: Double)
        fun onExpenseDeleted(categoryExpense: CategoryExpense?)
        fun setSourceAccount(account: AccountModel?)
        fun showDefaultCategory(categoryModel: CategoryModel?)
    }

    interface ExpenseView : BaseView {
        fun loadFilterExpense(listOffilterExpense: List<CategoryExpense>?)
        fun loadDate(listOfDate: List<DateModel?>?)
    }
}