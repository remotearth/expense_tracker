package com.remotearthsolutions.expensetracker.fragments.addexpensescreen

import android.app.Activity
import android.view.View
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.CategoryExpense
import com.remotearthsolutions.expensetracker.utils.Constants
import com.remotearthsolutions.expensetracker.utils.DateTimeUtils
import com.remotearthsolutions.expensetracker.utils.RequestReviewUtils
import com.remotearthsolutions.expensetracker.utils.SharedPreferenceUtils
import com.remotearthsolutions.expensetracker.viewmodels.ExpenseFragmentViewModel
import kotlinx.android.synthetic.main.fragment_add_expense.view.*

object Helpers {

    fun updateUI(mView: View, categoryExpense: CategoryExpense?, format: String) {
        mView.toCategoryBtn.update(categoryExpense?.categoryName!!, categoryExpense.categoryIcon!!)
        if (categoryExpense.totalAmount > 0) {
            mView.inputdigit.setText(categoryExpense.totalAmount.toString())
        }
        mView.expenseNoteEdtxt.setText(categoryExpense.note)
        if (categoryExpense.datetime > 0) {
            mView.dateTv.text = DateTimeUtils.getDate(
                categoryExpense.datetime,
                format
            )
        }
    }

    fun updateAccountBtn(mView: View, categoryExpense: CategoryExpense?) {
        mView.fromAccountBtn.update(categoryExpense?.accountName!!, categoryExpense.accountIcon!!)
    }

    fun requestToReviewApp(activity: Activity, viewModel: ExpenseFragmentViewModel) {
        if (!SharedPreferenceUtils.getInstance(activity)?.getBoolean(
                Constants.ASKED_TO_REVIEW,
                false
            )!!
        ) {
            viewModel.requestToReviewApp {
                activity.onBackPressed()
                SharedPreferenceUtils.getInstance(activity)
                    ?.putBoolean(Constants.ASKED_TO_REVIEW, true)
                RequestReviewUtils.request(activity)
            }
        }
    }
}
