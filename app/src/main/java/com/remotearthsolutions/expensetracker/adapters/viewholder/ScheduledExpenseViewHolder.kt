package com.remotearthsolutions.expensetracker.adapters.viewholder

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.adapters.ScheduledExpenseListAdapter
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.ScheduledExpenseDto
import com.remotearthsolutions.expensetracker.databinding.ListitemScheduledexpenseBinding
import com.remotearthsolutions.expensetracker.utils.CategoryIcons.getIconId
import com.remotearthsolutions.expensetracker.utils.DateTimeUtils
import com.remotearthsolutions.expensetracker.utils.Utils

class ScheduledExpenseViewHolder(
    private val context: Context,
    private val binding: ListitemScheduledexpenseBinding,
    private val currencySymbol: String,
    private val dateFormat: String,
    private val listener: ScheduledExpenseListAdapter.OnItemClickListener
) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SetTextI18n")
    fun bind(scheduledExpense: ScheduledExpenseDto) {
        with(scheduledExpense) {
            binding.categoryIconIv.setImageResource(getIconId(categoryIcon!!))
            binding.amountTv.text = "$currencySymbol ${Utils.formatDecimalValues(amount)}"
            binding.categoryNameTv.text = categoryName
            binding.accountImageIv.setImageResource(getIconId(accountIcon!!))
            binding.accountNameTv.text = accountName
            binding.scheduledOnTv.text = DateTimeUtils.getDate(nextoccurrencedate, dateFormat)
            binding.repeatTv.text = "$occurrence"

            binding.fromTitleTv.text = "${context.getString(R.string.from)}:  "
            binding.scheduledOnTitle.text = "${context.getString(R.string.scheduled_on)}:  "
            binding.repeatTitleTv.text = "${context.getString(R.string.occurrence_left)}:  "
        }

        binding.deleteBtn.setOnClickListener {
            listener.onItemDeleteButtonClick(scheduledExpense)
        }
    }
}
