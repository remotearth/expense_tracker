package com.remotearthsolutions.expensetracker.adapters.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.adapters.ExpenseListAdapter
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryExpense
import com.remotearthsolutions.expensetracker.utils.CategoryIcons.getIconId
import com.remotearthsolutions.expensetracker.utils.Utils.formatDecimalValues

class ExpenseListViewHolder(
    itemView: View,
    listener: ExpenseListAdapter.OnItemClickListener
) : RecyclerView.ViewHolder(itemView) {
    private val categoryTextView: TextView = itemView.findViewById(R.id.categoryNameTv)
    private val amountTextView: TextView = itemView.findViewById(R.id.amountTv)
    private val accountNameTv: TextView = itemView.findViewById(R.id.accountNameTv)
    private val categoryExpenseIcon: ImageView = itemView.findViewById(R.id.categoryIMG)
    private val accountImageIv: ImageView = itemView.findViewById(R.id.accountImageIv)

    private var expense: CategoryExpense? = null

    fun bind(expense: CategoryExpense, currencySymbol: String) {
        this.expense = expense
        categoryTextView.text = expense.categoryName.toString()
        amountTextView.text = "$currencySymbol ${formatDecimalValues(expense.totalAmount)}"
        categoryExpenseIcon.setImageResource(getIconId(expense.categoryIcon!!))
        accountNameTv.text = expense.accountName
        accountImageIv.setImageResource(getIconId(expense.accountIcon!!))
    }

    init {
        itemView.setOnClickListener {
            listener.onItemClick(
                expense
            )
        }
    }
}