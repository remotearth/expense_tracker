package com.remotearthsolutions.expensetracker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.adapters.viewholder.CategoryViewHolder
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel
import com.remotearthsolutions.expensetracker.utils.Utils.ScreenSize

class CategoryListAdapter : RecyclerView.Adapter<CategoryViewHolder> {
    private var categorylist: List<CategoryModel>?
    private var listener: OnItemClickListener? = null
    private var selectedCategoryId = 0
    private var screenSize: ScreenSize? = null

    constructor(categorylist: List<CategoryModel>?, selectedCategoryId: Int) {
        this.categorylist = categorylist
        this.selectedCategoryId = selectedCategoryId
    }

    constructor(categorylist: List<CategoryModel>?) {
        this.categorylist = categorylist
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    fun setScreenSize(screenSize: ScreenSize?) {
        this.screenSize = screenSize
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_catagory, parent, false)
        return CategoryViewHolder(view, listener!!, screenSize)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categorylist!![position]
        holder.bind(category, category.id == selectedCategoryId)
    }

    override fun getItemCount(): Int {
        return if (categorylist == null) {
            0
        } else categorylist!!.size
    }

    interface OnItemClickListener {
        fun onItemClick(category: CategoryModel?)
    }
}