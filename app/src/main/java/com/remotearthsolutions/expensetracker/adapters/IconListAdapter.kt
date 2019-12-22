package com.remotearthsolutions.expensetracker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.adapters.viewholder.IconViewHolder

class IconListAdapter(
    private val iconlist: List<String>?,
    private val layoutManager: GridLayoutManager
) : RecyclerView.Adapter<IconViewHolder>() {
    private var listener: OnItemClickListener? =
        null
    private var selectedIcon: String? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.custom_icon, parent, false)
        return IconViewHolder(v, listener!!, layoutManager)
    }

    override fun onBindViewHolder(holder: IconViewHolder, position: Int) {
        val icon = iconlist!![position]
        holder.bind(icon, selectedIcon == icon)
    }

    override fun getItemCount(): Int {
        return iconlist?.size ?: 0
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    fun setSelectedIcon(selectedIcon: String?) {
        this.selectedIcon = selectedIcon
        val position = iconlist!!.indexOf(selectedIcon)
        if (position > 0) {
            layoutManager.scrollToPosition(position)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(icon: String?)
    }

}