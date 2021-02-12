package com.remotearthsolutions.expensetracker.adapters.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.databaseutils.models.DateModel
import com.remotearthsolutions.expensetracker.utils.Utils

class DateSectionedViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    private val dateTextView: TextView = itemView.findViewById(R.id.date_section)
    private val underLine: View = itemView.findViewById(R.id.underLine)
    fun bind(dateModel: DateModel, currencySymbol: String) {
        if (dateModel.isDateSection) {
            underLine.visibility = View.VISIBLE
            dateTextView.text = dateModel.date
        } else {
            val amountInMonth =
                "${dateModel.date} ($currencySymbol ${Utils.formatDecimalValues(dateModel.totalAmount)})"
            dateTextView.text = amountInMonth
        }
    }
}