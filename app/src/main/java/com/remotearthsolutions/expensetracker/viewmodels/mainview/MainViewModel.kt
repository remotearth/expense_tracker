package com.remotearthsolutions.expensetracker.viewmodels.mainview

import android.app.Activity
import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.contracts.BaseView
import com.remotearthsolutions.expensetracker.contracts.MainContract
import com.remotearthsolutions.expensetracker.databaseutils.daos.AccountDao
import com.remotearthsolutions.expensetracker.databaseutils.daos.CategoryDao
import com.remotearthsolutions.expensetracker.databaseutils.daos.CategoryExpenseDao
import com.remotearthsolutions.expensetracker.databaseutils.daos.ExpenseDao
import com.remotearthsolutions.expensetracker.databaseutils.models.AccountModel
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel
import com.remotearthsolutions.expensetracker.databaseutils.models.ExpenseModel
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.CategoryExpense
import com.remotearthsolutions.expensetracker.services.FileProcessingService
import com.remotearthsolutions.expensetracker.services.FirebaseService
import com.remotearthsolutions.expensetracker.utils.AnalyticsManager
import com.remotearthsolutions.expensetracker.utils.Constants
import com.remotearthsolutions.expensetracker.utils.Utils.formatDecimalValues
import com.remotearthsolutions.expensetracker.viewmodels.mainview.MainViewModelHelper.getContentFromMetaString
import com.remotearthsolutions.expensetracker.viewmodels.mainview.MainViewModelHelper.getMetaString
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function4
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
    private val KEY_EXPENSES = "expenses"
    private val KEY_CATEGORIES = "categories"
    private val KEY_ACCOUNTS = "accounts"
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
                    view.showTotalBalance(formatDecimalValues(amount))
                } else {
                    view.showTotalBalance(formatDecimalValues(0.0))
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

    private fun saveToFile(
        activity: Activity,
        expenses: List<ExpenseModel>,
        categories: List<CategoryModel>,
        accounts: List<AccountModel>,
        stringBuilder: java.lang.StringBuilder
    ) {
        stringBuilder.append(Constants.KEY_META1_REPLACE).append(getMetaString(expenses))
            .append("\n")
        stringBuilder.append(Constants.KEY_META2_REPLACE).append(getMetaString(categories))
            .append("\n")
        stringBuilder.append(Constants.KEY_META3_REPLACE).append(getMetaString(accounts))
            .append("\n")

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

    fun saveExpenseToCSV(activity: Activity) {
        val stringBuilder = StringBuilder()
        stringBuilder.append("\n\n")
        stringBuilder.append(activity.getString(R.string.date_category_amount_from_note))
        stringBuilder.append("\n")
        if (disposable.isDisposed) {
            disposable = CompositeDisposable()
        }
        view.showProgress(activity.getString(R.string.please_wait))
        disposable.add(Single.zip(
            categoryExpenseDao.allFilterExpense,
            expenseDao.allExpenseEntry,
            categoryDao.allCategories,
            accountDao.allAccounts,
            Function4<List<CategoryExpense>?, List<ExpenseModel>?, List<CategoryModel>?, List<AccountModel>?, SaveExpenseToFileData>
            { listOfFilterExpense, allExpenses, allCategories, allAccounts ->
                if (listOfFilterExpense.isNotEmpty()) {
                    for (i in listOfFilterExpense.indices) {
                        if (listOfFilterExpense[i].totalAmount > 0) {
                            stringBuilder.append(listOfFilterExpense[i])
                        }
                    }
                    stringBuilder.append(Constants.DONOT_EDIT_META_DATA)
                    SaveExpenseToFileData(
                        allExpenses,
                        allCategories,
                        allAccounts,
                        stringBuilder
                    )
                } else {
                    SaveExpenseToFileData(allExpenses, allCategories, allAccounts, null)
                }
            }
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { throwable ->
                throwable.printStackTrace()
                view.hideProgress()
            }
            .subscribe { data, throwable: Throwable? ->
                view.hideProgress()
                if (throwable != null) {
                    throwable.printStackTrace()
                    view.showAlert(
                        "",
                        activity.getString(R.string.something_went_wrong),
                        activity.getString(R.string.ok),
                        null,
                        null
                    )
                } else {
                    if (data.stringBuilder == null) {
                        view.showAlert(
                            "",
                            activity.getString(R.string.expense_data_not_available_to_export),
                            activity.getString(R.string.ok),
                            null,
                            null
                        )
                    } else {
                        saveToFile(
                            activity,
                            data.expneses!!,
                            data.categories!!,
                            data.accounts!!,
                            data.stringBuilder!!
                        )
                    }
                }
                disposable.clear()
            }
        )
    }

    private fun getDataMapToUpload(context: Context, callback: (Map<String, String>) -> Unit) {
        val map = HashMap<String, String>()
        if (disposable.isDisposed) {
            disposable = CompositeDisposable()
        }

        view.showProgress(context.getString(R.string.please_wait))
        disposable.add(Single.zip(
            categoryExpenseDao.allFilterExpense,
            expenseDao.allExpenseEntry,
            categoryDao.allCategories,
            accountDao.allAccounts,
            Function4<List<CategoryExpense>?, List<ExpenseModel>?, List<CategoryModel>?, List<AccountModel>?, HashMap<String, String>>
            { listOfFilterExpense, allExpenses, allCategories, allAccounts ->
                if (listOfFilterExpense.isNotEmpty()) {
                    map[KEY_EXPENSES] = getMetaString(allExpenses)
                    map[KEY_CATEGORIES] = getMetaString(allCategories)
                    map[KEY_ACCOUNTS] = getMetaString(allAccounts)
                }
                map
            }
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { throwable ->
                throwable.printStackTrace()
                view.hideProgress()
            }
            .subscribe { result: HashMap<String, String>, throwable: Throwable? ->
                view.hideProgress()
                if (throwable != null) {
                    throwable.printStackTrace()
                    view.showAlert(
                        "",
                        context.getString(R.string.something_went_wrong),
                        context.getString(R.string.ok),
                        null,
                        null
                    )
                } else {
                    callback(result)
                }
                disposable.clear()
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
                "", context.getString(R.string.buy_message),
                context.getString(R.string.ok), null, null
            )
            return
        }

        if (!isLoggedIn) {
            view.showAlert(
                "",
                context.getString(R.string.login_to_sync),
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
            if (it.isEmpty()) {
                view.showAlert(
                    "",
                    context.getString(R.string.expense_data_not_available_to_upload),
                    context.getString(R.string.ok),
                    null,
                    null
                )
                return@getDataMapToUpload
            }

            view.showAlert(context.getString(R.string.warning),
                context.getString(R.string.will_overwrite_in_cloud),
                context.getString(R.string.yes), context.getString(R.string.no),
                object : BaseView.Callback {
                    override fun onOkBtnPressed() {
                        view.showProgress(context.getString(R.string.please_wait))
                        firebaseService.uploadToFireStore(user, it, {
                            view.hideProgress()
                            view.showAlert(
                                "", context.getString(R.string.successfully_uploaded),
                                context.getString(R.string.ok), null, null
                            )
                            AnalyticsManager.logEvent(AnalyticsManager.CLOUD_BACKUP)
                        }, {
                            view.hideProgress()
                            view.showAlert(
                                "", context.getString(R.string.something_went_wrong),
                                context.getString(R.string.ok), null, null
                            )
                        })
                    }
                })
        }
    }

    fun downloadFromCloud(context: Context, user: String) {
        view.showAlert(context.getString(R.string.warning),
            context.getString(R.string.overwrite_device_data),
            context.getString(R.string.yes), context.getString(R.string.no),
            object : BaseView.Callback {
                override fun onOkBtnPressed() {
                    view.showProgress(context.getString(R.string.please_wait))
                    firebaseService.downloadFromCloud(user, {
                        view.hideProgress()
                        if (it.isEmpty()) {
                            view.showAlert(
                                "", context.getString(R.string.data_not_available_to_download),
                                context.getString(R.string.ok), null, null
                            )
                            return@downloadFromCloud
                        }

                        val categories = getContentFromMetaString(
                            it.getValue(KEY_CATEGORIES),
                            Array<CategoryModel>::class.java
                        )
                        val expenseModels = getContentFromMetaString(
                            it.getValue(KEY_EXPENSES),
                            Array<ExpenseModel>::class.java
                        )
                        val accountModels = getContentFromMetaString(
                            it.getValue(KEY_ACCOUNTS),
                            Array<AccountModel>::class.java
                        )
                        saveAllData(categories, expenseModels, accountModels)
                        AnalyticsManager.logEvent(AnalyticsManager.CLOUD_DOWNLOAD)
                    }, {
                        view.hideProgress()
                        view.showAlert("", it, context.getString(R.string.ok), null, null)
                    })
                }
            })
    }

    data class SaveExpenseToFileData(
        var expneses: List<ExpenseModel>?,
        var categories: List<CategoryModel>?,
        var accounts: List<AccountModel>?,
        var stringBuilder: java.lang.StringBuilder?
    )
}
