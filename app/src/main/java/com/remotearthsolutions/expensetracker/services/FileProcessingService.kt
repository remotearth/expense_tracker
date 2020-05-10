package com.remotearthsolutions.expensetracker.services

import android.app.Activity
import com.remotearthsolutions.expensetracker.databaseutils.models.AccountModel
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel
import com.remotearthsolutions.expensetracker.databaseutils.models.ExpenseModel
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.CategoryExpense

interface FileProcessingService {
    fun writeOnCsvFile(
        activity: Activity,
        content: String?,
        onSuccessRunnable: Runnable?,
        onFailureRunnable: Runnable?
    )

    fun readFromCsvFile(activity: Activity): List<CategoryExpense>?
    fun loadTableData(
        filepath: String?,
        callback: Callback?
    )

    val nameOfAllCsvFile: List<String>?
    fun shareFile(activity: Activity)
    interface Callback {
        fun onComplete(
            categories: List<CategoryModel>?,
            expenseModels: List<ExpenseModel>?,
            accountModels: List<AccountModel>?
        )
    }
}