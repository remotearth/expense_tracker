package com.remotearthsolutions.expensetracker.adapters.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.adapters.CategoryListViewAdapter
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel
import com.remotearthsolutions.expensetracker.utils.CategoryIcons.getIconId

class CategoryListViewHolder(
    view: View,
    listener: CategoryListViewAdapter.OnItemClickListener
) : RecyclerView.ViewHolder(view) {
    private val categoryName: TextView = view.findViewById(R.id.cat_nameview)
    private val categoryImage: ImageView = view.findViewById(R.id.cat_iconview)
    //private val container: LinearLayout = view.findViewById(R.id.container)

    private var categoryModel: CategoryModel? = null
    fun bind(categoryModel: CategoryModel) {
        this.categoryModel = categoryModel
        categoryName.text = categoryModel.name
        categoryImage.setImageResource(getIconId(categoryModel.icon!!))
        //container.setBackgroundResource(R.color.catAccItem)
    }

    init {
        view.setOnClickListener {
            listener.onItemClick(
                categoryModel
            )
        }
    }
}