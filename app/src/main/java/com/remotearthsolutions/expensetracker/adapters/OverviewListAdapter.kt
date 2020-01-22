package com.remotearthsolutions.expensetracker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.adapters.viewholder.OverviewItemViewHolder
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.CategoryOverviewItemDto


class OverviewListAdapter(
    private val listOfItems: List<CategoryOverviewItemDto>,
    private val totalExpense: Double,
    private val maxWidthOfBar: Int
) :
    RecyclerView.Adapter<OverviewItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OverviewItemViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_overview_expense_item, parent, false)
        return OverviewItemViewHolder(v)
    }

    override fun getItemCount(): Int {
        return listOfItems.size
    }

    override fun onBindViewHolder(holder: OverviewItemViewHolder, position: Int) {
        val item = listOfItems[position]
        holder.bind(item, totalExpense, maxWidthOfBar)
    }
}