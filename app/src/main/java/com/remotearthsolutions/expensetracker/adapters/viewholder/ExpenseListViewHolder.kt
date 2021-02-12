package com.remotearthsolutions.expensetracker.adapters.viewholder

import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.adapters.ExpenseListAdapter
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.CategoryExpense
import com.remotearthsolutions.expensetracker.utils.CategoryIcons.getIconId
import com.remotearthsolutions.expensetracker.utils.Utils.formatDecimalValues

class ExpenseListViewHolder(
    itemView: View,
    private val listener: ExpenseListAdapter.OnItemClickListener
) : RecyclerView.ViewHolder(itemView) {
    private val categoryTextView: TextView = itemView.findViewById(R.id.categoryNameTv)
    private val notesTextView: TextView = itemView.findViewById(R.id.notesTv)
    private val amountTextView: TextView = itemView.findViewById(R.id.amountTv)
    private val accountNameTv: TextView = itemView.findViewById(R.id.accountNameTv)
    private val categoryExpenseIcon: ImageView = itemView.findViewById(R.id.categoryIMG)
    private val accountImageIv: ImageView = itemView.findViewById(R.id.accountImageIv)
    private val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)

    private var expense: CategoryExpense? = null

    fun bind(
        expense: CategoryExpense,
        currencySymbol: String,
        isDeleteModeOn: Boolean
    ) {
        notesTextView.visibility = View.GONE

        this.expense = expense
        categoryTextView.text = expense.categoryName.toString()

        expense.note?.let {
            if (it.isNotEmpty()) {
                notesTextView.text = it.trim()
                notesTextView.visibility = View.VISIBLE
            }
        }

        amountTextView.text = "$currencySymbol ${formatDecimalValues(expense.totalAmount)}"
        categoryExpenseIcon.setImageResource(getIconId(expense.categoryIcon!!))
        accountNameTv.text = expense.accountName
        accountImageIv.setImageResource(getIconId(expense.accountIcon!!))
        if (isDeleteModeOn) {
            checkBox.visibility = View.VISIBLE
        } else {
            checkBox.visibility = View.GONE
        }
        checkBox.isChecked = expense.isCheckedForDelete

        itemView.setOnClickListener {
            if (isDeleteModeOn) {
                onItemClickWithDeleteModeOn(expense)
            } else {
                listener.onItemClick(expense)
            }

        }

        checkBox.setOnClickListener {
            onItemClickWithDeleteModeOn(expense)
        }
    }

    private fun onItemClickWithDeleteModeOn(expense: CategoryExpense) {
        expense.isCheckedForDelete = !expense.isCheckedForDelete
        checkBox.isChecked = expense.isCheckedForDelete

        if (expense.isCheckedForDelete) {
            listener.onSelectToDelete(expense)
        } else {
            listener.onCancelItemDelete(expense)
        }
    }
}