package com.remotearthsolutions.expensetracker.utils

import android.content.Context
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.remotearthsolutions.expensetracker.databaseutils.DatabaseClient
import com.remotearthsolutions.expensetracker.databaseutils.models.AccountModel
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel
import com.remotearthsolutions.expensetracker.databaseutils.models.ExpenseModel
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.CategoryExpense
import com.remotearthsolutions.expensetracker.services.FirebaseServiceImpl
import com.remotearthsolutions.expensetracker.services.InternetCheckerServiceImpl
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function4
import io.reactivex.schedulers.Schedulers


object CloudBackupManager {

    fun startBackupWithPrecondtion(context: Context) {
        val sharedPref = SharedPreferenceUtils.getInstance(context)!!
        val user = sharedPref.getString(Constants.KEY_USER, "")

        val isLoggedIn = user != "guest" && user.isNotEmpty()
        if (!isLoggedIn || !InternetCheckerServiceImpl(context).isConnected) {
            return
        }

        val delay = sharedPref.getInt(Constants.KEY_EXPENSE_COUNT_AUTO_BACKUP_DELAY, 60)
        val neededExpenseCount = sharedPref.getInt(Constants.KEY_EXPENSE_COUNT_AUTO_BACKUP, delay)
        val disposable = CompositeDisposable()
        val expenseDao = DatabaseClient.getInstance(context).appDatabase.expenseDao()
        disposable.add(expenseDao.getNumberOfExpenseEntry().subscribeOn(Schedulers.io()).subscribe({
            if (it > neededExpenseCount) {
                getDataToUpload(context) { data ->
                    FirebaseServiceImpl(context).uploadToFirebaseStorage(
                        user, data, "silentautobackup/", { // onSuccess
                            sharedPref.putInt(
                                Constants.KEY_EXPENSE_COUNT_AUTO_BACKUP,
                                neededExpenseCount + delay
                            )
                            println("Silent backup successful")
                        }, null
                    )
                }
            }
        }, {
            it.printStackTrace()
            FirebaseCrashlytics.getInstance().recordException(it)
        }))

    }

    fun getDataToUpload(context: Context, callback: (String) -> Unit) {
        val db = DatabaseClient.getInstance(context).appDatabase
        val disposable = CompositeDisposable()

        disposable.add(Single.zip(
            db.categoryExpenseDao().allFilterExpense,
            db.expenseDao().allExpenseEntry,
            db.categoryDao().allCategories,
            db.accountDao().allAccounts,
            Function4<List<CategoryExpense>?, List<ExpenseModel>?, List<CategoryModel>?, List<AccountModel>?, String>
            { listOfFilterExpense, allExpenses, allCategories, allAccounts ->
                var str = ""
                if (listOfFilterExpense.isNotEmpty()) {
                    str = "${allExpenses}|${allCategories}|${allAccounts}"
                }
                str
            }
        ).subscribe({
            callback(it)
        }, {
            it.printStackTrace()
            FirebaseCrashlytics.getInstance().recordException(it)
        })
        )
    }
}