package com.remotearthsolutions.expensetracker.contracts

import com.remotearthsolutions.expensetracker.databaseutils.models.AccountModel
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryExpense
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel

interface ExpenseFragmentContract {
    interface View : BaseView {
        fun defineClickListener()
        fun onExpenseAdded(amount: Double)
        fun onExpenseDeleted(categoryExpense: CategoryExpense?)
        fun setSourceAccount(account: AccountModel?)
        fun showDefaultCategory(categoryModel: CategoryModel?)
    }
}