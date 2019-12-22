package com.remotearthsolutions.expensetracker.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.databaseutils.models.AccountModel
import com.remotearthsolutions.expensetracker.utils.CategoryIcons.getIconId

class AddAccountAmountDialogFragment : DialogFragment() {
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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_addaccountamount, container, false)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        val accountNameTv = view.findViewById<TextView>(R.id.accountNameTv)
        val accountImageIv =
            view.findViewById<ImageView>(R.id.accountImageIv)
        val amountEdtxt = view.findViewById<EditText>(R.id.amountEdtxt)
        if (accountIncome != null) {
            accountNameTv.text = accountIncome!!.name
            accountImageIv.setImageResource(getIconId(accountIncome!!.icon!!))
            amountEdtxt.setText(accountIncome!!.amount.toString())
            amountEdtxt.setSelection(amountEdtxt.text.toString().length)
        }
        view.findViewById<View>(R.id.okBtn)
            .setOnClickListener {
                val amount = amountEdtxt.text.toString()
                if (amount.isEmpty()) {
                    Toast.makeText(
                        activity,
                        getString(R.string.you_have_to_enter_an_amount),
                        Toast.LENGTH_LONG
                    ).show()
                    return@setOnClickListener
                }
                val accountAmount = amount.toDouble()
                accountIncome!!.amount = accountAmount
                callback.onAmountAdded(accountIncome)
            }
    }

    interface Callback {
        fun onAmountAdded(accountIncome: AccountModel?)
    }
}