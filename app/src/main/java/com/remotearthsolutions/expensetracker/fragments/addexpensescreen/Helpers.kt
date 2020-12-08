package com.remotearthsolutions.expensetracker.fragments.addexpensescreen

import android.app.Activity
import android.view.View
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.CategoryExpense
import com.remotearthsolutions.expensetracker.utils.*
import com.remotearthsolutions.expensetracker.viewmodels.ExpenseFragmentViewModel
import kotlinx.android.synthetic.main.fragment_add_expense.view.*

object Helpers {

    fun updateUI(mView: View, categoryExpense: CategoryExpense?, format: String) {
        mView.toCategoryBtn.update(categoryExpense?.categoryName!!, categoryExpense.categoryIcon!!)
        if (categoryExpense.totalAmount > 0) {
            mView.inputdigit.setText(Utils.formatDecimalValues(categoryExpense.totalAmount))
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
        val sharedPreferenceUtils = SharedPreferenceUtils.getInstance(activity)!!
        if (!sharedPreferenceUtils.getBoolean(Constants.USER_TOLD_NEVER_ASK_TO_REVIEW, false)) {
            val countNeeded = sharedPreferenceUtils.getInt(
                Constants.ENTRY_NEEDED,
                Constants.DEFAULT_NUMBER_OF_ENTRY_NEEDED
            )
            viewModel.requestToReviewApp(countNeeded) {
                activity.onBackPressed()
                RequestReviewUtils.request(activity)
            }
        }
    }
}
