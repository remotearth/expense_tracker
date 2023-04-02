package com.remotearthsolutions.expensetracker.viewmodels

import android.os.Build
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.crashlytics.BuildConfig
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.remotearthsolutions.expensetracker.databaseutils.daos.ExpenseDao
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel
import com.remotearthsolutions.expensetracker.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainFragmentViewModel(
    private val expenseDao: ExpenseDao,
) : ViewModel() {
    private val disposable = CompositeDisposable()
    val currentExpense = MutableLiveData<Double>();

    fun gerCurrentExpense(startDate: Long, endDate: Long) {

        disposable.add(expenseDao.getTotalAmountInDateRange(startDate, endDate)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { amount: Double?, throwable: Throwable? ->
                if (throwable == null) {
                    currentExpense.postValue(amount)
                } else {
                    if (BuildConfig.DEBUG){
                        throwable.printStackTrace()
                    }
                    FirebaseCrashlytics.getInstance().recordException(throwable)
                    currentExpense.postValue(0.0)
                }
            });
    }

    override fun onCleared() {
        super.onCleared()
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
    }
}