package com.remotearthsolutions.expensetracker.services

import android.app.Activity
import android.net.Uri
import com.remotearthsolutions.expensetracker.databaseutils.models.AccountModel
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel
import com.remotearthsolutions.expensetracker.databaseutils.models.ExpenseModel
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.CategoryExpense
import java.net.URI

interface FileProcessingService {
    fun writeOnCsvFile(
        activity: Activity,
        content: String?,
        onSuccessRunnable: Runnable?,
        onFailureRunnable: Runnable?
    )

    fun readFromCsvFile(activity: Activity): List<CategoryExpense>?
    fun loadTableData(
        fileURI: Uri?,
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

        fun onFailure()
    }
}