package com.remotearthsolutions.expensetracker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.adapters.viewholder.ScheduledExpenseViewHolder
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.ScheduledExpenseDto

class ScheduledExpenseListAdapter(
    private val scheduledExpenseList: List<ScheduledExpenseDto>?,
    private val currencySymbol: String,
    private val format: String,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<ScheduledExpenseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduledExpenseViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.listitem_scheduledexpense, parent, false)
        return ScheduledExpenseViewHolder(v, currencySymbol, format, listener)
    }

    override fun getItemCount(): Int {
        if (scheduledExpenseList == null) {
            return 0
        }
        return scheduledExpenseList.size
    }

    override fun onBindViewHolder(holder: ScheduledExpenseViewHolder, position: Int) {
        val scheduledExpense = scheduledExpenseList?.get(position)
        scheduledExpense?.let { holder.bind(it) }
    }

    interface OnItemClickListener {
        fun onItemDeleteButtonClick(scheduledExpense: ScheduledExpenseDto)
    }
}
