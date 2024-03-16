package com.remotearthsolutions.expensetracker.viewmodels.mainview

import android.app.Activity
import android.content.Context
import android.net.Uri
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.google.firebase.crashlytics.BuildConfig
import com.google.firebase.crashlytics.FirebaseCrashlytics
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
import com.remotearthsolutions.expensetracker.services.FileProcessingService
import com.remotearthsolutions.expensetracker.services.FirebaseService
import com.remotearthsolutions.expensetracker.utils.AnalyticsManager
import com.remotearthsolutions.expensetracker.utils.Constants
import com.remotearthsolutions.expensetracker.utils.Utils.formatDecimalValues
import com.remotearthsolutions.expensetracker.utils.cloudbackup.CloudBackupHelper.getContentFromMetaString
import com.remotearthsolutions.expensetracker.utils.cloudbackup.CloudBackupHelper.getMetaString
import com.remotearthsolutions.expensetracker.utils.cloudbackup.CloudBackupManager
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

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
    var endTime: Long = 0

    fun init(lifecycleOwner: LifecycleOwner?) {
        view.initializeView()
        accountDao.totalAmount.observe(
            lifecycleOwner!!
        ) { amount: Double? ->
            if (amount != null) {
                view.showTotalBalance(formatDecimalValues(amount))
            } else {
                view.showTotalBalance(formatDecimalValues(0.0))
            }
            if (amount != null && amount < 0) {
                view.setBalanceTextColor(android.R.color.holo_red_light)
            } else {
                view.setBalanceTextColor(android.R.color.holo_green_light)
            }
        }
    }

    fun updateAllTimeExpense() {
        disposable.add(
            expenseDao.getAllTimeTotalAmount()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { amount: Double?, throwable: Throwable? ->
                    if (throwable == null) {
                        view.showAllTimeTotalExpense(formatDecimalValues(amount!!))
                    } else {
                        if (BuildConfig.DEBUG) {
                            throwable.printStackTrace()
                        }
                        FirebaseCrashlytics.getInstance().recordException(throwable)
                        view.showAllTimeTotalExpense(
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
        // more than this expense cannot be saved encrypted in the cell of csv file.
        // that's why it needs to be split into multiple row
        val sublist = expenses.chunked(100)
        for( li in sublist ){
            stringBuilder.append(Constants.KEY_EX_META_REPLACE).append(getMetaString(li)).append("\n")
        }

        stringBuilder.append(Constants.KEY_META2_REPLACE).append(getMetaString(categories))
            .append("\n")
        stringBuilder.append(Constants.KEY_META3_REPLACE).append(getMetaString(accounts))
            .append("\n")

        fileProcessingService.writeOnCsvFile(
            activity,
            stringBuilder.toString(),
            {
                shareCSVFileToMail(activity)
                disposable.clear()
            },
            { disposable.clear() }
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
            accountDao.allAccounts
        ) { listOfFilterExpense, allExpenses, allCategories, allAccounts ->
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
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { throwable ->
                if (BuildConfig.DEBUG) {
                    throwable.printStackTrace()
                }
                FirebaseCrashlytics.getInstance().recordException(throwable)
                view.hideProgress()
            }
            .subscribe { data, throwable: Throwable? ->
                view.hideProgress()
                if (throwable != null) {
                    if (BuildConfig.DEBUG) {
                        throwable.printStackTrace()
                    }
                    FirebaseCrashlytics.getInstance().recordException(throwable)
                    view.showAlert(
                        "",
                        activity.getString(R.string.something_went_wrong),
                        activity.getString(R.string.ok),
                        null,
                        null, null
                    )
                } else {
                    if (data.stringBuilder == null) {
                        view.showAlert(
                            "",
                            activity.getString(R.string.expense_data_not_available_to_export),
                            activity.getString(R.string.ok),
                            null,
                            null, null
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

    private fun shareCSVFileToMail(activity: Activity) {
        fileProcessingService.shareFile(activity)
    }

    fun importDataFromFile(fileUri: Uri) {
        view.showProgress((view as Context).getString(R.string.please_wait))
        fileProcessingService.loadTableData(
            fileUri,
            object : FileProcessingService.Callback {
                override fun onComplete(
                    categories: List<CategoryModel>?,
                    expenseModels: List<ExpenseModel>?,
                    accountModels: List<AccountModel>?
                ) {
                    saveAllData(categories, expenseModels, accountModels)
                }

                override fun onFailure() {
                    view.hideProgress()
                    view.showAlert(
                        null,
                        "The selected file is corrupted or not supported in Expense Tracker",
                        (view as Context).getString(R.string.ok),
                        null,
                        null,
                        null
                    )
                }
            }
        )
    }

    private fun saveAllData(
        categories: List<CategoryModel>?,
        expenseModels: List<ExpenseModel>?,
        accountModels: List<AccountModel>?
    ) {
        disposable.add(
            Completable.fromAction {
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
            }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ //onSuccess
                    view.hideProgress()
                    view.onDataUpdated()
                }, {
                    view.hideProgress()
                    if (BuildConfig.DEBUG) {
                        it.printStackTrace()
                    }
                    FirebaseCrashlytics.getInstance().recordException(it)
                })
        )
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
                context.getString(R.string.ok), null, null, null
            )
            return
        }

        if (!isLoggedIn) {
            view.showAlert(
                "",
                context.getString(R.string.login_to_sync),
                context.getString(R.string.ok), null, null, null
            )
            return
        }

        if (isDeviceOnline) {
            callback.invoke()
        } else {
            view.showAlert(
                "",
                context.getString(R.string.internet_connection_needed),
                context.getString(R.string.ok), null, null, null
            )
        }
    }

    fun backupToCloud(context: Context, user: String) {
        CloudBackupManager.getDataToUpload(context) {
            if (it.isNullOrEmpty()) {
                view.showAlert(
                    "",
                    context.getString(R.string.expense_data_not_available_to_upload),
                    context.getString(R.string.ok),
                    null,
                    null, null
                )
                return@getDataToUpload
            }

            view.showAlert(context.getString(R.string.warning),
                context.getString(R.string.will_overwrite_in_cloud),
                context.getString(R.string.yes), context.getString(R.string.no), null,
                object : BaseView.Callback {
                    override fun onOkBtnPressed() {
                        view.showProgress(context.getString(R.string.please_wait))
                        firebaseService.uploadToFirebaseStorage(user, it, "exporteddata/", {
                            view.hideProgress()
                            view.showAlert(
                                "", context.getString(R.string.successfully_uploaded),
                                context.getString(R.string.ok), null, null, null
                            )
                            with(AnalyticsManager) { logEvent(CLOUD_BACKUP) }
                        }, {
                            view.hideProgress()
                            view.showAlert(
                                "", context.getString(R.string.something_went_wrong),
                                context.getString(R.string.ok), null, null, null
                            )
                        })
                    }
                })
        }
    }

    fun downloadFromCloud(context: Context, user: String) {
        view.showAlert(context.getString(R.string.warning),
            context.getString(R.string.overwrite_device_data),
            context.getString(R.string.yes), context.getString(R.string.no), null,
            object : BaseView.Callback {
                override fun onOkBtnPressed() {
                    view.showProgress(context.getString(R.string.please_wait))
                    firebaseService.downloadFromFirebaseStorage(user, {
                        if (it.isEmpty()) {
                            view.hideProgress()
                            view.showAlert(
                                "", context.getString(R.string.data_not_available_to_download),
                                context.getString(R.string.ok), null, null, null
                            )
                            return@downloadFromFirebaseStorage
                        }

                        val categories = getContentFromMetaString(
                            it.getValue(FirebaseService.KEY_CATEGORIES),
                            Array<CategoryModel>::class.java
                        )
                        val expenseModels = getContentFromMetaString(
                            it.getValue(FirebaseService.KEY_EXPENSES),
                            Array<ExpenseModel>::class.java
                        )
                        val accountModels = getContentFromMetaString(
                            it.getValue(FirebaseService.KEY_ACCOUNTS),
                            Array<AccountModel>::class.java
                        )
                        saveAllData(categories, expenseModels, accountModels)
                        with(AnalyticsManager) { logEvent(CLOUD_DOWNLOAD) }
                    }, {
                        view.hideProgress()
                        view.showAlert("", it, context.getString(R.string.ok), null, null, null)
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
