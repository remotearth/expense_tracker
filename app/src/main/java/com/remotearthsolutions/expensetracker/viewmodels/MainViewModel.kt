package com.remotearthsolutions.expensetracker.viewmodels

import android.app.Activity
import android.content.Context
import android.util.Base64
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.contracts.BaseView
import com.remotearthsolutions.expensetracker.contracts.MainContract
import com.remotearthsolutions.expensetracker.databaseutils.daos.AccountDao
import com.remotearthsolutions.expensetracker.databaseutils.daos.CategoryDao
import com.remotearthsolutions.expensetracker.databaseutils.daos.CategoryExpenseDao
import com.remotearthsolutions.expensetracker.databaseutils.daos.ExpenseDao
import com.remotearthsolutions.expensetracker.databaseutils.models.AccountModel
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryExpense
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel
import com.remotearthsolutions.expensetracker.databaseutils.models.ExpenseModel
import com.remotearthsolutions.expensetracker.services.FileProcessingService
import com.remotearthsolutions.expensetracker.services.FirebaseService
import com.remotearthsolutions.expensetracker.utils.Constants
import com.remotearthsolutions.expensetracker.utils.Utils.formatDecimalValues
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.nio.charset.Charset
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
    private var KEY_EXPENSES = "expenses"
    private var KEY_CATEGORIES = "categories"
    private var KEY_ACCOUNTS = "accounts"
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
                    view.setBalanceTextColor(android.R.color.holo_red_dark)
                } else {
                    view.setBalanceTextColor(android.R.color.holo_green_light)
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

    fun checkAuthectication(user: String) {
        if (user.isEmpty()) {
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
                                            accountDao.allAccounts.subscribeOn(Schedulers.io())
                                                .observeOn(Schedulers.io()).subscribe { entries2: List<AccountModel?>? ->
                                                    val accountJson = Gson().toJson(entries2)
                                                    val encryptedStr2 =
                                                        Base64.encodeToString(
                                                            accountJson.toByteArray(
                                                                charset(Constants.KEY_UTF_VERSION)
                                                            ),
                                                            Base64.NO_WRAP
                                                        )
                                                    stringBuilder.append(Constants.KEY_META3_REPLACE + encryptedStr2 + "\n")
                                                    fileProcessingService.writeOnCsvFile(
                                                        activity,
                                                        stringBuilder.toString(),
                                                        Runnable {
                                                            shareCSVFileToMail(activity)
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
                            activity.getString(R.string.expense_data_not_available_to_export),
                            activity.getString(R.string.ok),
                            null,
                            null
                        )
                        disposable.clear()
                    }
                }
        )
    }

    fun getDataMapToUpload(context: Context, callback: (Map<String, String>?) -> Unit) {
        val map = HashMap<String, String>()
        if (disposable.isDisposed) {
            disposable = CompositeDisposable()
        }
        disposable.add(
            categoryExpenseDao.allFilterExpense
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { listOfFilterExpense: List<CategoryExpense>? ->
                    if (listOfFilterExpense != null && listOfFilterExpense.isNotEmpty()) {
                        disposable.add(
                            expenseDao.allExpenseEntry.subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe { entries: List<ExpenseModel?>? ->
                                    val expenseJson = Gson().toJson(entries)
                                    val encryptedExpensesStr =
                                        Base64.encodeToString(
                                            expenseJson.toByteArray(charset(Constants.KEY_UTF_VERSION)),
                                            Base64.NO_WRAP
                                        )
                                    map[KEY_EXPENSES] = encryptedExpensesStr
                                    disposable.add(
                                        categoryDao.allCategories.subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe { entries1: List<CategoryModel?>? ->
                                                val categoryJson = Gson().toJson(entries1)
                                                val encryptedCategoriesStr =
                                                    Base64.encodeToString(
                                                        categoryJson.toByteArray(charset(Constants.KEY_UTF_VERSION)),
                                                        Base64.NO_WRAP
                                                    )
                                                map[KEY_CATEGORIES] = encryptedCategoriesStr
                                                disposable.add(
                                                    accountDao.allAccounts.subscribeOn(Schedulers.io())
                                                        .observeOn(AndroidSchedulers.mainThread())
                                                        .subscribe { entries2: List<AccountModel?>? ->
                                                            val accountJson =
                                                                Gson().toJson(entries2)
                                                            val encryptedAccountsStr =
                                                                Base64.encodeToString(
                                                                    accountJson.toByteArray(
                                                                        charset(Constants.KEY_UTF_VERSION)
                                                                    ),
                                                                    Base64.NO_WRAP
                                                                )
                                                            map[KEY_ACCOUNTS] = encryptedAccountsStr
                                                            callback(map)
                                                            disposable.clear()
                                                        }
                                                )
                                            }
                                    )
                                }
                        )
                    } else {
                        view.showAlert(
                            "",
                            context.getString(R.string.expense_data_not_available_to_upload),
                            context.getString(R.string.ok),
                            null,
                            null
                        )
                        callback(null)
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
                    saveAllData(categories, expenseModels, accountModels)
                }
            }
        )
    }

    private fun saveAllData(
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

    fun backupOrSync(
        context: Context,
        isPremium: Boolean,
        isDeviceOnline: Boolean,
        isLoggedIn: Boolean,
        callback: () -> Unit
    ) {
        if (!isPremium) {
            view.showAlert(
                "", "This is a premium feature. Please purchase the app first.",
                context.getString(R.string.ok), null, null
            )
            return
        }

        if (!isLoggedIn) {
            view.showAlert(
                "",
                "Please login using facebook or google to sync your data in the cloud securely",
                context.getString(R.string.ok), null, null
            )
            return
        }

        if (isDeviceOnline) {
            callback.invoke()
        } else {
            view.showAlert(
                "",
                context.getString(R.string.internet_connection_needed),
                context.getString(R.string.ok), null, null
            )
        }
    }

    fun backupToCloud(context: Context, user: String) {
        getDataMapToUpload(context) {
            view.showAlert(context.getString(R.string.warning),
                "This will overwrite the data in the cloud for this account, if you have any. Are you sure to proceed?",
                context.getString(R.string.yes), context.getString(R.string.no),
                object : BaseView.Callback {
                    override fun onOkBtnPressed() {
                        it?.let {
                            view.showProgress(context.getString(R.string.please_wait))

                            firebaseService.uploadToFireStore(user, it, {
                                view.hideProgress()
                                view.showAlert(
                                    "", "Successfully uploaded",
                                    context.getString(R.string.ok), null, null
                                )
                            }, {
                                view.hideProgress()
                                view.showAlert(
                                    "", "Something went wrong. Please try again later.",
                                    context.getString(R.string.ok), null, null
                                )
                            })
                        }
                    }
                })
        }
    }

    fun downloadFromCloud(context: Context, user: String) {
        view.showAlert(context.getString(R.string.warning),
            "This will overwrite all data in your device. Are you sure to proceed?",
            context.getString(R.string.yes), context.getString(R.string.no),
            object : BaseView.Callback {
                override fun onOkBtnPressed() {
                    view.showProgress(context.getString(R.string.please_wait))
                    firebaseService.downloadFromCloud(user, {
                        view.hideProgress()
                        if (it.isEmpty()) {
                            view.showAlert("","No data available to download for this account",
                                context.getString(R.string.ok),null,null)
                            return@downloadFromCloud
                        }

                        var jsonContent = String(
                            Base64.decode(it[KEY_CATEGORIES], Base64.NO_WRAP),
                            Charset.forName(Constants.KEY_UTF_VERSION)
                        )
                        val categories =
                            Gson().fromJson(jsonContent, Array<CategoryModel>::class.java).toList()

                        jsonContent = String(
                            Base64.decode(it[KEY_EXPENSES], Base64.NO_WRAP),
                            Charset.forName(Constants.KEY_UTF_VERSION)
                        )
                        val expenseModels =
                            Gson().fromJson(jsonContent, Array<ExpenseModel>::class.java).toList()

                        jsonContent = String(
                            Base64.decode(it[KEY_ACCOUNTS], Base64.NO_WRAP),
                            Charset.forName(Constants.KEY_UTF_VERSION)
                        )
                        val accountModels =
                            Gson().fromJson(jsonContent, Array<AccountModel>::class.java).toList()

                        saveAllData(categories, expenseModels, accountModels)
                    }, {
                        view.hideProgress()
                        view.showAlert("", it, context.getString(R.string.ok), null, null)
                    })
                }
            })
    }

}