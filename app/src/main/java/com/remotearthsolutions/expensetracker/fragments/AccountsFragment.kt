package com.remotearthsolutions.expensetracker.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.activities.ApplicationObject
import com.remotearthsolutions.expensetracker.adapters.AccountsAdapter
import com.remotearthsolutions.expensetracker.contracts.AccountContract
import com.remotearthsolutions.expensetracker.contracts.BaseView
import com.remotearthsolutions.expensetracker.databaseutils.DatabaseClient
import com.remotearthsolutions.expensetracker.databaseutils.models.AccountModel
import com.remotearthsolutions.expensetracker.fragments.OptionBottomSheetFragment.OptionsFor
import com.remotearthsolutions.expensetracker.utils.AlertDialogUtils.show
import com.remotearthsolutions.expensetracker.utils.Utils.getCurrency
import com.remotearthsolutions.expensetracker.viewmodels.AccountViewModel
import com.remotearthsolutions.expensetracker.viewmodels.viewmodel_factory.BaseViewModelFactory
import kotlinx.android.synthetic.main.fragment_accounts.view.*

class AccountsFragment : BaseFragment(),
    AccountContract.View,
    OptionBottomSheetFragment.Callback {
    private var viewModel: AccountViewModel? = null
    private lateinit var mView: View
    private var adapter: AccountsAdapter? = null
    private var selectAccountModel: AccountModel? = null
    private var limitOfAccount = 0
    private var currencySymbol: String? = null
    private var mContext: Context? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_accounts, container, false)
        return mView
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        currencySymbol = "$"
        if (mContext != null) {
            currencySymbol = getCurrency(mContext!!)
        }
        val accountDao =
            DatabaseClient.getInstance(mContext!!)?.appDatabase?.accountDao()

        viewModel =
            ViewModelProviders.of(this, BaseViewModelFactory {
                AccountViewModel(mContext!!, this, accountDao!!)
            }).get(AccountViewModel::class.java)

        viewModel!!.loadAccounts()
        viewModel!!.numberOfItem.observe(this,
            Observer { count: Int -> limitOfAccount = count }
        )
        view.findViewById<View>(R.id.addAccountBtn)
            .setOnClickListener {
                if (limitOfAccount < 5 ||
                    ((mContext as Activity?)!!.application as ApplicationObject).isPremium
                ) {
                    selectAccountModel = null
                    onClickEditBtn()
                } else {
                    showAlert(
                        getString(R.string.attention),
                        getString(R.string.you_need_to_be_premium_user_to_add_more_categories),
                        getString(R.string.ok),
                        null,
                        null
                    )
                }
            }
    }

    override fun onAccountFetch(accounts: List<AccountModel>?) {
        if (isAdded) {
            adapter = AccountsAdapter(mContext!!, accounts!!, currencySymbol!!)
            mView.accountList.adapter = adapter
            mView.accountList.onItemClickListener =
                AdapterView.OnItemClickListener { _: AdapterView<*>?, _: View?, position: Int, _: Long ->
                    selectAccountModel = accounts[position]
                    val optionBottomSheetFragment = OptionBottomSheetFragment()
                    optionBottomSheetFragment.setCallback(this@AccountsFragment, OptionsFor.ACCOUNT)
                    optionBottomSheetFragment.show(
                        childFragmentManager,
                        OptionBottomSheetFragment::class.java.name
                    )
                }
        }
    }

    override fun onSuccess(message: String?) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
    }

    override fun onClickAddAmountBtn() {
        val addAccountAmountDialogFragment = AddAccountAmountDialogFragment()
        addAccountAmountDialogFragment.setAccountIncome(selectAccountModel)
        addAccountAmountDialogFragment.setCallback(object :
            AddAccountAmountDialogFragment.Callback {
            override fun onAmountAdded(accountIncome: AccountModel?) {
                viewModel!!.addOrUpdateAccount(accountIncome)
                addAccountAmountDialogFragment.dismiss()
            }
        })
        addAccountAmountDialogFragment.show(
            childFragmentManager,
            AddAccountAmountDialogFragment::class.java.name
        )
    }

    override fun onClickEditBtn() {
        val dialogFragment = AddUpdateAccountDialogFragment()
        dialogFragment.setAccountModel(selectAccountModel)
        dialogFragment.show(
            childFragmentManager,
            AddUpdateAccountDialogFragment::class.java.name
        )
    }

    override fun onClickDeleteBtn() {
        if (selectAccountModel!!.notremovable == 1) {
            Toast.makeText(
                activity,
                getString(R.string.you_cannot_delete_this_account),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        show(activity,
            getString(R.string.warning),
            getString(R.string.deleting_this_account_will_remove_expenses_related_to_this_also_are_you_sure_you_want_to_delete),
            getString(R.string.yes),
            getString(R.string.not_now),
            object : BaseView.Callback {
                override fun onOkBtnPressed() {
                    viewModel!!.deleteAccount(selectAccountModel)
                }

                override fun onCancelBtnPressed() {}
            })
    }
}