package com.remotearthsolutions.expensetracker.fragments.accounts

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.activities.ApplicationObject
import com.remotearthsolutions.expensetracker.activities.main.MainActivity
import com.remotearthsolutions.expensetracker.adapters.AccountsAdapter
import com.remotearthsolutions.expensetracker.contracts.AccountContract
import com.remotearthsolutions.expensetracker.contracts.BaseView
import com.remotearthsolutions.expensetracker.databaseutils.DatabaseClient
import com.remotearthsolutions.expensetracker.databaseutils.models.AccountModel
import com.remotearthsolutions.expensetracker.fragments.BaseFragment
import com.remotearthsolutions.expensetracker.fragments.OptionBottomSheetFragment
import com.remotearthsolutions.expensetracker.fragments.OptionBottomSheetFragment.OptionsFor
import com.remotearthsolutions.expensetracker.fragments.TransferBalanceDialogFragment
import com.remotearthsolutions.expensetracker.fragments.salary.SalaryFragment
import com.remotearthsolutions.expensetracker.utils.AlertDialogUtils.show
import com.remotearthsolutions.expensetracker.utils.Constants
import com.remotearthsolutions.expensetracker.utils.SharedPreferenceUtils
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
        currencySymbol = "$"
        if (mContext != null) {
            currencySymbol = getCurrency(mContext!!)
        }

        viewModel =
            ViewModelProvider(this, BaseViewModelFactory {
                AccountViewModel(
                    mContext!!, this,
                    DatabaseClient.getInstance(mContext!!).appDatabase.accountDao(),
                    DatabaseClient.getInstance(mContext!!).appDatabase.expenseDao()
                )
            }).get(AccountViewModel::class.java)

        viewModel!!.loadAccounts()
        viewModel!!.numberOfItem.observe(viewLifecycleOwner,
            Observer { count: Int -> limitOfAccount = count }
        )
        view.addAccountBtn.setOnClickListener {
            view.fabMenu.close(true)
            if (limitOfAccount < 5 ||
                ((mContext as Activity?)!!.application as ApplicationObject).isPremium
            ) {
                selectAccountModel = null
                onClickEditBtn()
            } else {
                showAlert(
                    requireContext().getString(R.string.attention),
                    requireContext().getString(R.string.you_need_to_be_premium_user_to_add_more_categories),
                    requireContext().getString(R.string.ok),
                    null,
                    null, null
                )
            }
        }

        view.transferAmountBtn.setOnClickListener {
            view.fabMenu.close(true)
            val transferBalanceDialogFragment =
                TransferBalanceDialogFragment()
            transferBalanceDialogFragment.setViewModel(viewModel!!)
            transferBalanceDialogFragment.show(
                childFragmentManager,
                TransferBalanceDialogFragment::class.java.name
            )
        }

        view.setSalaryBtn.setOnClickListener {
            view.fabMenu.close(true)
            val salaryFragment = SalaryFragment()
            salaryFragment.setViewModel(viewModel!!)
            salaryFragment.show(
                childFragmentManager,
                SalaryFragment::class.java.name
            )
        }

        view.container.setOnClickListener {
            view.fabMenu.close(true)
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun observe() {

        viewModel!!.listOfAccountLiveData.observe(viewLifecycleOwner, Observer {
            if (isAdded) {
                adapter = AccountsAdapter(mContext!!, it, currencySymbol!!)
                mView.accountList.adapter = adapter
                mView.accountList.onItemClickListener =
                    AdapterView.OnItemClickListener { _: AdapterView<*>?, _: View?, position: Int, _: Long ->
                        mView.fabMenu.close(true)
                        selectAccountModel = it[position]
                        val optionBottomSheetFragment =
                            OptionBottomSheetFragment()
                        optionBottomSheetFragment.setCallback(
                            this@AccountsFragment,
                            OptionsFor.ACCOUNT
                        )
                        optionBottomSheetFragment.show(
                            childFragmentManager,
                            OptionBottomSheetFragment::class.java.name
                        )
                    }
            }
        })
    }

    override fun onSuccess(message: String?) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
    }

    override fun onUpdateAccount() {
        viewModel!!.loadAccounts()
    }

    override fun onDeleteAccount() {
        viewModel?.loadAccounts()
        (activity as MainActivity).updateSummary()
        if (selectAccountModel?.id!! > 3) {
            SharedPreferenceUtils.getInstance(requireActivity())
                ?.putInt(Constants.KEY_SELECTED_ACCOUNT_ID, 1)
        }
    }

    override fun onClickAddAmountBtn() {
        val addAccountAmountDialogFragment =
            AddAccountAmountDialogFragment()
        addAccountAmountDialogFragment.setAccountIncome(
            selectAccountModel,
            AddAccountAmountDialogFragment.Purpose.AddAmount
        )
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

    override fun onClickUpdateAmountBtn() {
        val addAccountAmountDialogFragment =
            AddAccountAmountDialogFragment()
        addAccountAmountDialogFragment.setAccountIncome(
            selectAccountModel,
            AddAccountAmountDialogFragment.Purpose.UpdateAmount
        )
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
        val dialogFragment =
            AddUpdateAccountDialogFragment()
        dialogFragment.initialize(selectAccountModel, viewModel)
        dialogFragment.show(
            childFragmentManager,
            AddUpdateAccountDialogFragment::class.java.name
        )
    }

    override fun onClickDeleteBtn() {
        if (selectAccountModel!!.notremovable == 1) {
            Toast.makeText(
                activity,
                requireContext().getString(R.string.you_cannot_delete_this_account),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        show(activity,
            requireContext().getString(R.string.warning),
            requireContext().getString(R.string.deleting_this_account_will_remove_expenses_related_to_this_also_are_you_sure_you_want_to_delete),
            requireContext().getString(R.string.yes),
            requireContext().getString(R.string.not_now), null,
            object : BaseView.Callback {
                override fun onOkBtnPressed() {
                    viewModel!!.deleteAccount(selectAccountModel)
                }

                override fun onCancelBtnPressed() {}
            })
    }
}