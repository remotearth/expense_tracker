package com.remotearthsolutions.expensetracker.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.adapters.AccountsAdapter
import com.remotearthsolutions.expensetracker.utils.Response
import com.remotearthsolutions.expensetracker.utils.Utils
import com.remotearthsolutions.expensetracker.viewmodels.AccountViewModel
import kotlinx.android.synthetic.main.fragment_transfer_balance.view.*


class TransferBalanceDialogFragment : DialogFragment() {
    private lateinit var mView: View
    private var viewModel: AccountViewModel? = null
    private var mContext: Context? = null

    fun setViewModel(viewModel: AccountViewModel) {
        this.viewModel = viewModel
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context as FragmentActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_transfer_balance, container, false)
        mView.amountEdtxt.width = Utils.getDeviceScreenSize(mContext)?.width!!
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (viewModel == null) {
            throw Exception("viewmodel must be initialized")
        }
        initialize()

        mView.okBtn.setOnClickListener {
            if (!mView.amountEdtxt.text.isEmpty()) {
                var amount = mView.amountEdtxt.text.toString().toDouble()
                val pos1 = mView.fromAccountSpnr.selectedItemPosition
                val pos2 = mView.toAccountSpnr.selectedItemPosition
                val response = viewModel?.transferAmount(amount, pos1, pos2)

                if (response?.code == Response.FAILURE) {
                    Toast.makeText(mContext, response?.message, Toast.LENGTH_SHORT).show()
                } else {
                    dismiss()
                }
            } else {
                Toast.makeText(
                    mContext,
                    getString(R.string.enter_amount_to_transfer),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }

    }

    private fun initialize() {
        var currencySymbol = "$"
        if (mContext != null) {
            currencySymbol = Utils.getCurrency(mContext!!)
        }
        val sprnAdapter =
            AccountsAdapter(mContext!!, viewModel?.listOfAccountLiveData?.value!!, currencySymbol)

        with(mView) {
            fromAccountSpnr.adapter = sprnAdapter
            toAccountSpnr.adapter = sprnAdapter
            fromAccountSpnr.setSelection(0)
            toAccountSpnr.setSelection(1)

            cancelBtn.setOnClickListener {
                dismiss()
            }
            switchAccountBtn.setOnClickListener {
                val p1 = mView.fromAccountSpnr.selectedItemPosition
                val p2 = mView.toAccountSpnr.selectedItemPosition
                mView.fromAccountSpnr.setSelection(p2, true)
                mView.toAccountSpnr.setSelection(p1, true)
            }
        }
    }
}