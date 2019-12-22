package com.remotearthsolutions.expensetracker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.adapters.viewholder.CategoryListViewHolder
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel

class CategoryListViewAdapter(private val categorylist: List<CategoryModel>?) :
    RecyclerView.Adapter<CategoryListViewHolder>() {
    private var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryListViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_category_view, parent, false)
        return CategoryListViewHolder(v, listener!!)
    }

    override fun onBindViewHolder(
        holder: CategoryListViewHolder,
        position: Int
    ) {
        val categoryModel = categorylist!![position]
        holder.bind(categoryModel)
    }

    override fun getItemCount(): Int {
        return categorylist?.size ?: 0
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(categoryModel: CategoryModel?)
    }

}