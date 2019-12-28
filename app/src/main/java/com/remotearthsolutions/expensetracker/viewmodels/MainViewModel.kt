package com.remotearthsolutions.expensetracker.viewmodels

import android.R
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.remotearthsolutions.expensetracker.contracts.MainContract
import com.remotearthsolutions.expensetracker.databaseutils.daos.AccountDao
import com.remotearthsolutions.expensetracker.databaseutils.daos.ExpenseDao
import com.remotearthsolutions.expensetracker.entities.User
import com.remotearthsolutions.expensetracker.services.FirebaseService
import com.remotearthsolutions.expensetracker.utils.Utils.formatDecimalValues
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainViewModel(
    private val view: MainContract.View,
    private val firebaseService: FirebaseService,
    private val accountDao: AccountDao,
    private val expenseDao: ExpenseDao
) : ViewModel() {
    var disposable = CompositeDisposable()
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

}