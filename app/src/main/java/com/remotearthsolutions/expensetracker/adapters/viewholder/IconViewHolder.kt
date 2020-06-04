package com.remotearthsolutions.expensetracker.adapters.viewholder

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.adapters.IconListAdapter
import com.remotearthsolutions.expensetracker.utils.CategoryIcons.getIconId

class IconViewHolder(
    view: View,
    listener: IconListAdapter.OnItemClickListener
) : RecyclerView.ViewHolder(view) {
    private val container: LinearLayout = view.findViewById(R.id.container)
    private val categoryIconIv: ImageView = view.findViewById(R.id.iconIv)
    private var icon: String? = null
    fun bind(icon: String?, isSelected: Boolean) {
        this.icon = icon
        categoryIconIv.setImageResource(getIconId(icon!!))
        if (isSelected) {
            container.setBackgroundColor(Color.parseColor("#C0C0C0"))
        } else {
            container.setBackgroundResource(R.color.background)
        }
    }

    init {
        view.setOnClickListener {
            listener.onItemClick(icon)
        }
    }
}