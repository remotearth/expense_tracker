package com.remotearthsolutions.expensetracker.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.adapters.AccountListAdapter
import com.remotearthsolutions.expensetracker.contracts.AccountDialogContract
import com.remotearthsolutions.expensetracker.databaseutils.models.AccountModel
import com.remotearthsolutions.expensetracker.utils.Constants
import com.remotearthsolutions.expensetracker.viewmodels.AccountDialogViewModel
import kotlinx.android.synthetic.main.fragment_add_account.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class AccountDialogFragment : DialogFragment(),
    AccountDialogContract.View {
    private val viewModel: AccountDialogViewModel by viewModel { parametersOf(this) }
    private lateinit var accountListAdapter: AccountListAdapter
    private lateinit var mView: View
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
        mView = inflater.inflate(R.layout.fragment_add_account, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView.accountrecyclearView.setHasFixedSize(true)
        val llm = LinearLayoutManager(mContext)
        mView.accountrecyclearView.layoutManager = llm

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
        mView.accountrecyclearView.adapter = accountListAdapter
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