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
import kotlinx.android.synthetic.main.custom_account.view.*

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

        view!!.acimage.setImageResource(getIconId(model.icon!!))
        view.actitle.text = model.name
        view.acammount.text = "$currencySymbol ${formatDecimalValues(model.amount)}"
        if (model.amount < 0) {
            view.acammount.setTextColor(context.resources.getColor(android.R.color.holo_red_dark))
        } else {
            view.acammount.setTextColor(context.resources.getColor(android.R.color.holo_green_light))
        }
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position,convertView,parent)
    }

}