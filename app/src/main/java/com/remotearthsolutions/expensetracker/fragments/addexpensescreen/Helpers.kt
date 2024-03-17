package com.remotearthsolutions.expensetracker.fragments.addexpensescreen

import android.app.Activity
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.CategoryExpense
import com.remotearthsolutions.expensetracker.databinding.FragmentAddExpenseBinding
import com.remotearthsolutions.expensetracker.utils.*
import com.remotearthsolutions.expensetracker.viewmodels.ExpenseFragmentViewModel

object Helpers {

    fun updateUI(
        binding: FragmentAddExpenseBinding,
        categoryExpense: CategoryExpense?,
        format: String
    ) {
        binding.toCategoryBtn.update(
            categoryExpense?.categoryName!!,
            categoryExpense.categoryIcon!!
        )
        if (categoryExpense.totalAmount > 0) {
            binding.inputdigit.setText(Utils.formatDecimalValues(categoryExpense.totalAmount))
        }

        val note =
            if (categoryExpense.note == null || categoryExpense.note == "null") "" else getTruncatedNote(
                categoryExpense.note.toString()
            )
        binding.expenseNoteEdtxt.setText(note)
        if (categoryExpense.datetime > 0) {
            binding.dateTv.text = DateTimeUtils.getDate(
                categoryExpense.datetime,
                format
            )
        }
    }

    fun updateAccountBtn(binding: FragmentAddExpenseBinding, categoryExpense: CategoryExpense?) {
        binding.fromAccountBtn.update(categoryExpense?.accountName!!, categoryExpense.accountIcon!!)
    }

    fun getTruncatedNote(note: String): String {
        if (note.length > 22) {
            return "${note.subSequence(0, 22)}..."
        }

        return note
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
