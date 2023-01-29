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
import com.remotearthsolutions.expensetracker.databinding.FragmentTransferBalanceBinding
import com.remotearthsolutions.expensetracker.utils.AnalyticsManager
import com.remotearthsolutions.expensetracker.utils.Response
import com.remotearthsolutions.expensetracker.utils.Utils
import com.remotearthsolutions.expensetracker.viewmodels.AccountViewModel

class TransferBalanceDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentTransferBalanceBinding
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
    ): View {
        binding = FragmentTransferBalanceBinding.inflate(layoutInflater, container, false)
        binding.amountEdtxt.width = Utils.getDeviceScreenSize(mContext)?.width!!
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()

        binding.okBtn.setOnClickListener {
            if (binding.amountEdtxt.text.isNotEmpty()) {
                val amount = binding.amountEdtxt.text.toString().toDouble()
                val pos1 = binding.fromAccountSpnr.selectedItemPosition
                val pos2 = binding.toAccountSpnr.selectedItemPosition
                val response = viewModel?.transferAmount(amount, pos1, pos2)

                if (response?.code == Response.FAILURE) {
                    Toast.makeText(mContext, response.message, Toast.LENGTH_SHORT).show()
                } else {
                    dismiss()
                    with(AnalyticsManager) {
                        logEvent(ACCOUNT_AMOUNT_TRANSFERRED)
                    }
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

        binding.fromAccountSpnr.adapter = sprnAdapter
        binding.toAccountSpnr.adapter = sprnAdapter
        binding.fromAccountSpnr.setSelection(0)
        binding.toAccountSpnr.setSelection(1)

        binding.cancelBtn.setOnClickListener {
            dismiss()
        }
        binding.switchAccountBtn.setOnClickListener {
            val p1 = binding.fromAccountSpnr.selectedItemPosition
            val p2 = binding.toAccountSpnr.selectedItemPosition
            binding.fromAccountSpnr.setSelection(p2, true)
            binding.toAccountSpnr.setSelection(p1, true)
        }
    }
}