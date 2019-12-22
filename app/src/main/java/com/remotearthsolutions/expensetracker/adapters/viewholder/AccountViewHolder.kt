package com.remotearthsolutions.expensetracker.adapters.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.adapters.AccountListAdapter
import com.remotearthsolutions.expensetracker.databaseutils.models.AccountModel
import com.remotearthsolutions.expensetracker.utils.CategoryIcons
import com.remotearthsolutions.expensetracker.utils.Utils.formatDecimalValues

class AccountViewHolder(
    view: View,
    listener: AccountListAdapter.OnItemClickListener
) : RecyclerView.ViewHolder(view) {
    private val accountImageIv: ImageView = view.findViewById(R.id.acimage)
    private val accountNameTv: TextView = view.findViewById(R.id.actitle)
    private val amountTv: TextView = view.findViewById(R.id.acammount)
    private lateinit var account: AccountModel
    fun bind(account: AccountModel) {
        this.account = account
        accountImageIv.setImageResource(CategoryIcons.getIconId(account.icon!!))
        accountNameTv.text = account.name
        amountTv.text = formatDecimalValues(
            account.amount
        )
    }

    init {
        view.setOnClickListener {
            listener.onItemClick(account)
        }
    }
}