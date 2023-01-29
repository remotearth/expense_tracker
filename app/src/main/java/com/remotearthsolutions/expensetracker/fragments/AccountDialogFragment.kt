package com.remotearthsolutions.expensetracker.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.remotearthsolutions.expensetracker.adapters.AccountListAdapter
import com.remotearthsolutions.expensetracker.contracts.AccountDialogContract
import com.remotearthsolutions.expensetracker.databaseutils.models.AccountModel
import com.remotearthsolutions.expensetracker.databinding.FragmentAddAccountBinding
import com.remotearthsolutions.expensetracker.utils.Constants
import com.remotearthsolutions.expensetracker.viewmodels.AccountDialogViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class AccountDialogFragment : DialogFragment(),
    AccountDialogContract.View {
    private lateinit var binding: FragmentAddAccountBinding
    private val viewModel: AccountDialogViewModel by viewModel { parametersOf(this) }
    private lateinit var accountListAdapter: AccountListAdapter
    private var callback: Callback? =
        null
    private lateinit var mContext: Context
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddAccountBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.accountrecyclearView.setHasFixedSize(true)
        val llm = LinearLayoutManager(mContext)
        binding.accountrecyclearView.layoutManager = llm

        viewModel.init()
        viewModel.loadAccounts()
    }

    override fun onAccountFetchSuccess(accounts: List<AccountModel>?) {
        accountListAdapter = AccountListAdapter(accounts)
        accountListAdapter.setOnItemClickListener(object : AccountListAdapter.OnItemClickListener {
            override fun onItemClick(account: AccountModel) {
                callback!!.onSelectAccount(account)
            }

        })
        binding.accountrecyclearView.adapter = accountListAdapter
    }

    override fun onAccountFetchFailure() {}
    interface Callback {
        fun onSelectAccount(accountIncome: AccountModel)
    }

    companion object {
        fun newInstance(title: String?): AccountDialogFragment {
            val frag = AccountDialogFragment()
            val args = Bundle()
            args.putString(Constants.KEY_TITLE, title)
            frag.arguments = args
            return frag
        }
    }
}