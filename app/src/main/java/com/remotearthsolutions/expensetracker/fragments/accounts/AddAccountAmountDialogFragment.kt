package com.remotearthsolutions.expensetracker.fragments.accounts

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.databaseutils.models.AccountModel
import com.remotearthsolutions.expensetracker.utils.CategoryIcons.getIconId
import kotlinx.android.synthetic.main.fragment_addaccountamount.view.*

class AddAccountAmountDialogFragment : DialogFragment() {
    private lateinit var mView: View
    private lateinit var callback: Callback
    private var accountIncome: AccountModel? = null
    private lateinit var mContext: Context
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    fun setAccountIncome(accountIncome: AccountModel?) {
        this.accountIncome = accountIncome
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_addaccountamount, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (accountIncome != null) {
            mView.accountNameTv.text = accountIncome!!.name
            mView.accountImageIv.setImageResource(getIconId(accountIncome!!.icon!!))
        }
        mView.okBtn.setOnClickListener {
            val amount = mView.amountEdtxt.text.toString()
            if (amount.isEmpty()) {
                Toast.makeText(
                    activity,
                    getString(R.string.enter_amount),
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            val accountAmount = amount.toDouble()
            accountIncome!!.amount += accountAmount
            callback.onAmountAdded(accountIncome)
        }
    }

    interface Callback {
        fun onAmountAdded(accountIncome: AccountModel?)
    }
}