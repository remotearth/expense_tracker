package com.remotearthsolutions.expensetracker.viewmodels

import android.app.Activity
import android.util.Base64
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.contracts.DashboardContract
import com.remotearthsolutions.expensetracker.databaseutils.daos.AccountDao
import com.remotearthsolutions.expensetracker.databaseutils.daos.CategoryDao
import com.remotearthsolutions.expensetracker.databaseutils.daos.CategoryExpenseDao
import com.remotearthsolutions.expensetracker.databaseutils.daos.ExpenseDao
import com.remotearthsolutions.expensetracker.databaseutils.models.AccountModel
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel
import com.remotearthsolutions.expensetracker.databaseutils.models.ExpenseModel
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.CategoryExpense
import com.remotearthsolutions.expensetracker.services.FileProcessingService
import com.remotearthsolutions.expensetracker.utils.Constants
import com.remotearthsolutions.expensetracker.utils.Constants.Companion.KEY_UTF_VERSION
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executors

class DashboardViewModel(
    private val view: DashboardContract.View,
    private val categoryExpenseDao: CategoryExpenseDao,
    private val expenseDao: ExpenseDao,
    private val categoryDao: CategoryDao,
    private val accountDao: AccountDao,
    private val fileProcessingService: FileProcessingService
) : ViewModel() {
    private var disposable = CompositeDisposable()
    fun saveExpenseToCSV(activity: Activity) {
        val stringBuilder = StringBuilder()
        stringBuilder.append("\n\n")
        stringBuilder.append(activity.getString(R.string.date_category_amount_from_note))
        stringBuilder.append("\n")
        if (disposable.isDisposed) {
            disposable = CompositeDisposable()
        }
        disposable.add(
            categoryExpenseDao.allFilterExpense
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { listOfFilterExpense: List<CategoryExpense>? ->
                    if (listOfFilterExpense != null && listOfFilterExpense.isNotEmpty()) {
                        for (i in listOfFilterExpense.indices) {
                            if (listOfFilterExpense[i].totalAmount > 0) {
                                stringBuilder.append(listOfFilterExpense[i])
                            }
                        }
                        stringBuilder.append("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n")
                        stringBuilder.append("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n")
                        stringBuilder.append(activity.getString(R.string.dont_edit_this_meta_data))
                        stringBuilder.append("\n\n")
                        disposable.add(
                            expenseDao.allExpenseEntry.subscribeOn(Schedulers.io()).observeOn(
                                Schedulers.io()
                            ).subscribe { entries: List<ExpenseModel?>? ->
                                val json = Gson().toJson(entries)
                                val encryptedStr =
                                    Base64.encodeToString(
                                        json.toByteArray(charset(KEY_UTF_VERSION)),
                                        Base64.NO_WRAP
                                    )
                                stringBuilder.append(Constants.KEY_META1_REPLACE + encryptedStr + "\n")
                                disposable.add(
                                    categoryDao.allCategories.subscribeOn(
                                        Schedulers.io()
                                    ).observeOn(Schedulers.io()).subscribe { entries1: List<CategoryModel?>? ->
                                        Gson().toJson(entries1)
                                        val encryptedStr1 =
                                            Base64.encodeToString(
                                                json.toByteArray(charset(KEY_UTF_VERSION)),
                                                Base64.NO_WRAP
                                            )
                                        stringBuilder.append(Constants.KEY_META2_REPLACE + encryptedStr1 + "\n")
                                        disposable.add(
                                            accountDao.allAccounts.subscribeOn(
                                                Schedulers.io()
                                            ).observeOn(Schedulers.io()).subscribe { entries2: List<AccountModel?>? ->
                                                val json2 = Gson().toJson(entries2)
                                                val encryptedStr2 =
                                                    Base64.encodeToString(
                                                        json2.toByteArray(charset(KEY_UTF_VERSION)),
                                                        Base64.NO_WRAP
                                                    )
                                                stringBuilder.append(Constants.KEY_META3_REPLACE + encryptedStr2 + "\n")
                                                fileProcessingService.writeOnCsvFile(
                                                    activity,
                                                    stringBuilder.toString(),
                                                    Runnable {
                                                        shareCSVFileToMail(
                                                            activity
                                                        )
                                                        disposable.dispose()
                                                    },
                                                    Runnable { disposable.dispose() }
                                                )
                                            }
                                        )
                                    }
                                )
                            }
                        )
                    } else {
                        view.showAlert(
                            "",
                            activity.getString(R.string.expense_data_not_available_to_export),
                            activity.getString(R.string.ok),
                            null,
                            null
                        )
                        disposable.dispose()
                    }
                }
        )
    }

//    fun readExpenseFromCsv(activity: Activity?): List<CategoryExpense?>? {
//        return fileProcessingService.readFromCsvFile(activity)
//    }

    val allCsvFile: List<String>?
        get() = fileProcessingService.nameOfAllCsvFile

    private fun shareCSVFileToMail(activity: Activity) {
        fileProcessingService.shareFile(activity)
    }

    fun importDataFromFile(filepath: String?) {
        fileProcessingService.loadTableData(
            filepath,
            object : FileProcessingService.Callback {
                override fun onComplete(
                    categories: List<CategoryModel>?,
                    expenseModels: List<ExpenseModel>?,
                    accountModels: List<AccountModel>?
                ) {
                    Executors.newSingleThreadExecutor().execute {
                        categoryDao.deleteAll()
                        for (categoryModel in categories!!) {
                            categoryDao.addCategory(categoryModel)
                        }
                        expenseDao.deleteAll()
                        for (expenseModel in expenseModels!!) {
                            expenseDao.add(expenseModel)
                        }
                        accountDao.deleteAll()
                        for (accountModel in accountModels!!) {
                            accountDao.addAccount(accountModel)
                        }
                    }
                }
            }
        )
    }

}