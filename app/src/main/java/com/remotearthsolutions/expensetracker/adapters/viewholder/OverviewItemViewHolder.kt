package com.remotearthsolutions.expensetracker.adapters.viewholder

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.CategoryOverviewItemDto
import com.remotearthsolutions.expensetracker.utils.CategoryIcons
import com.remotearthsolutions.expensetracker.utils.Utils


class OverviewItemViewHolder(view: View, val currencySymbol: String) : RecyclerView.ViewHolder(view) {

    private val categoryIconIv: ImageView = view.findViewById(R.id.categoryIconIv)
    private val categoryNameTv: TextView = view.findViewById(R.id.categoryNameTv)
    private val totalExpenseTv: TextView = view.findViewById(R.id.totalExpenseTv)
    private val expensePercentTv: TextView = view.findViewById(R.id.expensePercentTv)
    private val barView: LinearLayout = view.findViewById(R.id.barView)
    private val guideBarView: LinearLayout = view.findViewById(R.id.guideBarView)

    @SuppressLint("SetTextI18n")
    fun bind(item: CategoryOverviewItemDto, totalExpense: Double, maxWidthOfBar: Int) {
        categoryIconIv.setImageResource(CategoryIcons.getIconId(item.categoryIcon!!))
        categoryNameTv.text = item.categoryName
        totalExpenseTv.text = "${Utils.formatDecimalValues(item.totalExpenseOfCateogry)} $currencySymbol"

        val percentage = if (totalExpense == 0.0) {
            0.0
        } else {
            Utils.formatDecimalValues((item.totalExpenseOfCateogry * 100) / totalExpense).toDouble()
        }
        expensePercentTv.text = "$percentage%"
        val newWidth = ((maxWidthOfBar * percentage) / 100.0).toInt()
        barView.layoutParams.width = newWidth
        guideBarView.layoutParams.width = newWidth + (if (newWidth < 50) 50 else 0)
    }
}