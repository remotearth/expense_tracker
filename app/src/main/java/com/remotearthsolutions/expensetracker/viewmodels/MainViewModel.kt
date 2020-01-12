package com.remotearthsolutions.expensetracker.viewmodels

import android.R
import android.app.Activity
import android.util.Base64
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.remotearthsolutions.expensetracker.contracts.MainContract
import com.remotearthsolutions.expensetracker.databaseutils.daos.AccountDao
import com.remotearthsolutions.expensetracker.databaseutils.daos.CategoryDao
import com.remotearthsolutions.expensetracker.databaseutils.daos.CategoryExpenseDao
import com.remotearthsolutions.expensetracker.databaseutils.daos.ExpenseDao
import com.remotearthsolutions.expensetracker.databaseutils.models.AccountModel
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel
import com.remotearthsolutions.expensetracker.databaseutils.models.ExpenseModel
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.CategoryExpense
import com.remotearthsolutions.expensetracker.entities.User
import com.remotearthsolutions.expensetracker.services.FileProcessingService
import com.remotearthsolutions.expensetracker.services.FirebaseService
import com.remotearthsolutions.expensetracker.utils.Constants
import com.remotearthsolutions.expensetracker.utils.Utils.formatDecimalValues
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executors

class MainViewModel(
    private val view: MainContract.View,
    private val firebaseService: FirebaseService,
    private val accountDao: AccountDao,
    private val expenseDao: ExpenseDao,
    private val categoryDao: CategoryDao,
    private val categoryExpenseDao: CategoryExpenseDao,
    private val fileProcessingService: FileProcessingService
) : ViewModel() {
    private var disposable = CompositeDisposable()
    var startTime: Long = 0
        private set
    var endTime: Long = 0
        private set

    fun init(lifecycleOwner: LifecycleOwner?) {
        view.initializeView()
        accountDao.totalAmount.observe(
            lifecycleOwner!!,
            Observer { amount: Double? ->
                if (amount != null) {
                    view.showTotalBalance(
                        formatDecimalValues(
                            amount
                        )
                    )
                } else {
                    view.showTotalBalance(
                        formatDecimalValues(
                            0.0
                        )
                    )
                }
                if (amount != null && amount < 0) {
                    view.setBalanceTextColor(R.color.holo_red_dark)
                } else {
                    view.setBalanceTextColor(R.color.holo_green_light)
                }
            }
        )
    }

    @JvmOverloads
    fun updateSummary(
        startTime: Long = this.startTime, endTime: Long = this.endTime
    ) {
        this.startTime = startTime
        this.endTime = endTime
        disposable.add(
            expenseDao.getTotalAmountInDateRange(startTime, endTime)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { amount: Double?, throwable: Throwable? ->
                    if (throwable == null) {
                        view.showTotalExpense(formatDecimalValues(amount!!))
                    } else {
                        throwable.printStackTrace()
                        view.showTotalExpense(
                            formatDecimalValues(
                                0.0
                            )
                        )
                    }
                }
        )
    }

    fun checkAuthectication(guestUser: User?) {
        val user = firebaseService.user
        if (user == null && guestUser == null) {
            view.goBackToLoginScreen()
        } else {
            view.startLoadingApp()
        }
    }

    fun performLogout() {
        firebaseService.logout()
        view.onLogoutSuccess()
    }

    fun saveExpenseToCSV(activity: Activity) {
        val stringBuilder = StringBuilder()
        stringBuilder.append("\n\n")
        stringBuilder.append(activity.getString(com.remotearthsolutions.expensetracker.R.string.date_category_amount_from_note))
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
                        stringBuilder.append(activity.getString(com.remotearthsolutions.expensetracker.R.string.dont_edit_this_meta_data))
                        stringBuilder.append("\n\n")
                        disposable.add(
                            expenseDao.allExpenseEntry.subscribeOn(Schedulers.io()).observeOn(
                                Schedulers.io()
                            ).subscribe { entries: List<ExpenseModel?>? ->
                                val expenseJson = Gson().toJson(entries)
                                val encryptedStr =
                                    Base64.encodeToString(
                                        expenseJson.toByteArray(charset(Constants.KEY_UTF_VERSION)),
                                        Base64.NO_WRAP
                                    )
                                stringBuilder.append(Constants.KEY_META1_REPLACE + encryptedStr + "\n")
                                disposable.add(
                                    categoryDao.allCategories.subscribeOn(
                                        Schedulers.io()
                                    ).observeOn(Schedulers.io()).subscribe { entries1: List<CategoryModel?>? ->
                                        val categoryJson = Gson().toJson(entries1)
                                        val encryptedStr1 =
                                            Base64.encodeToString(
                                                categoryJson.toByteArray(charset(Constants.KEY_UTF_VERSION)),
                                                Base64.NO_WRAP
                                            )
                                        stringBuilder.append(Constants.KEY_META2_REPLACE + encryptedStr1 + "\n")
                                        disposable.add(
                                            accountDao.allAccounts.subscribeOn(
                                                Schedulers.io()
                                            ).observeOn(Schedulers.io()).subscribe { entries2: List<AccountModel?>? ->
                                                val accountJson = Gson().toJson(entries2)
                                                val encryptedStr2 =
                                                    Base64.encodeToString(
                                                        accountJson.toByteArray(charset(Constants.KEY_UTF_VERSION)),
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
                                                        disposable.clear()
                                                    },
                                                    Runnable { disposable.clear() }
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
                            activity.getString(com.remotearthsolutions.expensetracker.R.string.expense_data_not_available_to_export),
                            activity.getString(com.remotearthsolutions.expensetracker.R.string.ok),
                            null,
                            null
                        )
                        disposable.clear()
                    }
                }
        )
    }

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
                        accountDao.deleteAll()
                        expenseDao.deleteAll()

                        for (categoryModel in categories!!) {
                            categoryDao.addCategory(categoryModel)
                        }
                        for (accountModel in accountModels!!) {
                            accountDao.addAccount(accountModel)
                        }
                        for (expenseModel in expenseModels!!) {
                            expenseDao.add(expenseModel)
                        }
                    }
                }
            }
        )
    }

}