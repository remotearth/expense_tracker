package com.remotearthsolutions.expensetracker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.remotearthsolutions.expensetracker.adapters.viewholder.ScheduledExpenseViewHolder
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.ScheduledExpenseDto
import com.remotearthsolutions.expensetracker.databinding.ListitemScheduledexpenseBinding

class ScheduledExpenseListAdapter(
    private val scheduledExpenseList: List<ScheduledExpenseDto>?,
    private val currencySymbol: String,
    private val format: String,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<ScheduledExpenseViewHolder>() {

    private lateinit var binding: ListitemScheduledexpenseBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduledExpenseViewHolder {
        binding = ListitemScheduledexpenseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ScheduledExpenseViewHolder(parent.context, binding, currencySymbol, format, listener)
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
