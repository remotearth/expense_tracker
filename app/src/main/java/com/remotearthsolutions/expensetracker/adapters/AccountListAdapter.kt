package com.remotearthsolutions.expensetracker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.adapters.viewholder.AccountViewHolder
import com.remotearthsolutions.expensetracker.databaseutils.models.AccountModel

class AccountListAdapter(private val accountList: List<AccountModel>?) :
    RecyclerView.Adapter<AccountViewHolder>() {
    private var listener: OnItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.custom_account, parent, false)
        return AccountViewHolder(v, listener!!)
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        val account = accountList!![position]
        holder.bind(account)
    }

    override fun getItemCount(): Int {
        return accountList?.size ?: 0
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(account: AccountModel)
    }

}