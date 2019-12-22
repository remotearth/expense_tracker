package com.remotearthsolutions.expensetracker.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.entities.DashboardModel

class DashboardAdapter(
    mContext: Context,
    private val dashboardItem: List<DashboardModel>
) : ArrayAdapter<DashboardModel?>(mContext, R.layout.custom_dashboard, dashboardItem) {
    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.custom_dashboard, parent, false)
        }
        val model = dashboardItem[position]
        val imageView =
            view!!.findViewById<ImageView>(R.id.dashboardimageView)
        val textView = view.findViewById<TextView>(R.id.dashboardtextView)
        imageView.setImageResource(model.dashboardImage)
        textView.text = model.dashboardName
        return view
    }

}