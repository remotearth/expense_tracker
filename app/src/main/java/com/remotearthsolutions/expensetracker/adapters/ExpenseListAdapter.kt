package com.remotearthsolutions.expensetracker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.adapters.viewholder.DateSectionedViewHolder
import com.remotearthsolutions.expensetracker.adapters.viewholder.ExpenseListViewHolder
import com.remotearthsolutions.expensetracker.databaseutils.models.DateModel
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryExpense

class ExpenseListAdapter(
    private val categoryExpenseList: List<CategoryExpense>?,
    private val currencySymbol: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var listener: OnItemClickListener? =
        null

    override fun getItemViewType(position: Int): Int {
        val item = categoryExpenseList!![position]
        return if (item.isHeader) {
            HEADER_ENABLE
        } else {
            HEADER_DISABLE
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return if (viewType == HEADER_ENABLE) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_sectioned_date, parent, false)
            DateSectionedViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.custom_all_expense, parent, false)
            ExpenseListViewHolder(view, listener!!)
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val item = categoryExpenseList!![position]
        if (holder is DateSectionedViewHolder) {
            holder.bind(DateModel(item.categoryName!!))
        } else {
            val viewHolder = holder as ExpenseListViewHolder
            viewHolder.bind(item, currencySymbol)
        }
    }

    override fun getItemCount(): Int {
        return categoryExpenseList?.size ?: 0
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(categoryExpense: CategoryExpense?)
    }

    companion object {
        private const val HEADER_ENABLE = 1
        private const val HEADER_DISABLE = 2
    }

}