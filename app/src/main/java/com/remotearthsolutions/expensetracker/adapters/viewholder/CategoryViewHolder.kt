package com.remotearthsolutions.expensetracker.adapters.viewholder

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.adapters.CategoryListAdapter
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel
import com.remotearthsolutions.expensetracker.utils.CategoryIcons.getIconId
import com.remotearthsolutions.expensetracker.utils.Utils.ScreenSize

class CategoryViewHolder(
    view: View,
    listener: CategoryListAdapter.OnItemClickListener,
    private val screenSize: ScreenSize?
) : RecyclerView.ViewHolder(view) {
    private val container: RelativeLayout = view.findViewById(R.id.container)
    private val categoryImageIv: ImageView = view.findViewById(R.id.eventtitle)
    private val categoryNameTv: TextView = view.findViewById(R.id.eventdate)
    private var category: CategoryModel? = null

    fun bind(category: CategoryModel, isSelected: Boolean) {
        this.category = category
        categoryImageIv.setImageResource(getIconId(category.icon!!))
        categoryNameTv.text = category.name
        if (isSelected) {
            container.setBackgroundColor(Color.parseColor("#C0C0C0"))
        } else {
            container.setBackgroundColor(Color.parseColor("#FFFFFF"))
        }
        if (screenSize != null) {
            categoryImageIv.layoutParams.height = screenSize.width / 7
            categoryImageIv.layoutParams.width = screenSize.width / 7
            container.layoutParams.height = screenSize.height / 7
        }
    }

    init {
        view.setOnClickListener {
            listener.onItemClick(category)
        }
    }
}