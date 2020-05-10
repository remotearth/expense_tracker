package com.remotearthsolutions.expensetracker.adapters.viewholder

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.adapters.ScheduledExpenseListAdapter
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.ScheduledExpenseDto
import com.remotearthsolutions.expensetracker.utils.CategoryIcons.getIconId
import com.remotearthsolutions.expensetracker.utils.DateTimeUtils
import com.remotearthsolutions.expensetracker.utils.Utils
import kotlinx.android.synthetic.main.listitem_scheduledexpense.view.*

class ScheduledExpenseViewHolder(
    private val context: Context,
    private val view: View,
    private val currencySymbol: String,
    private val dateFormat: String,
    private val listener: ScheduledExpenseListAdapter.OnItemClickListener
) : RecyclerView.ViewHolder(view) {

    @SuppressLint("SetTextI18n")
    fun bind(scheduledExpense: ScheduledExpenseDto) {
        with(scheduledExpense) {
            view.categoryIconIv.setImageResource(getIconId(categoryIcon!!))
            view.amountTv.text = "$currencySymbol ${Utils.formatDecimalValues(amount)}"
            view.categoryNameTv.text = categoryName
            view.accountImageIv.setImageResource(getIconId(accountIcon!!))
            view.accountNameTv.text = accountName
            view.scheduledOnTv.text = DateTimeUtils.getDate(nextoccurrencedate, dateFormat)
            view.fromTitleTv.text = "${context.getString(R.string.from)}:  "
            view.scheduledOnTitle.text = "${context.getString(R.string.scheduled_on)}:  "
        }

        view.deleteBtn.setOnClickListener {
            listener.onItemDeleteButtonClick(scheduledExpense)
        }
    }
}
