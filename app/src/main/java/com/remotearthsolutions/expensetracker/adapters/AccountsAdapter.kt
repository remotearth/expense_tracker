package com.remotearthsolutions.expensetracker.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.databaseutils.models.AccountModel
import com.remotearthsolutions.expensetracker.utils.CategoryIcons.getIconId
import com.remotearthsolutions.expensetracker.utils.Utils.formatDecimalValues

class AccountsAdapter(
    mContext: Context,
    private val accountList: List<AccountModel>,
    private val currencySymbol: String
) : ArrayAdapter<AccountModel?>(mContext, R.layout.custom_account, accountList) {
    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var view = convertView
        if (view == null) {
            view =
                LayoutInflater.from(parent.context).inflate(R.layout.custom_account, parent, false)
        }
        val model = accountList[position]
        val accountImageIv =
            view!!.findViewById<ImageView>(R.id.acimage)
        val accountNameTv = view.findViewById<TextView>(R.id.actitle)
        val ammountTv = view.findViewById<TextView>(R.id.acammount)
        accountImageIv.setImageResource(getIconId(model.icon!!))
        accountNameTv.text = model.name
        ammountTv.text = "$currencySymbol ${formatDecimalValues(model.amount)}"
        if (model.amount < 0) {
            ammountTv.setTextColor(context.resources.getColor(android.R.color.holo_red_dark))
        } else {
            ammountTv.setTextColor(context.resources.getColor(android.R.color.holo_green_light))
        }
        return view
    }

}