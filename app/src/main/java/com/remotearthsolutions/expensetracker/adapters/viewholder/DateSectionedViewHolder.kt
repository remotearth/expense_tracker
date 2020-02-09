package com.remotearthsolutions.expensetracker.adapters.viewholder

import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.databaseutils.models.DateModel

class DateSectionedViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    private val dateTextView: TextView = itemView.findViewById(R.id.date_section)
    private val underLine: View = itemView.findViewById(R.id.underLine)
    fun bind(dateModel: DateModel) {
        if (dateModel.isDateSection) {
            dateTextView.gravity = Gravity.START
            dateTextView.textSize = TEXT_SIZE
            underLine.visibility = View.VISIBLE
        }
        dateTextView.text = dateModel.date
    }

    companion object {
        private const val TEXT_SIZE = 15f
    }

}