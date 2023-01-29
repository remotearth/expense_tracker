package com.remotearthsolutions.expensetracker.fragments.accounts

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.adapters.IconListAdapter
import com.remotearthsolutions.expensetracker.databaseutils.models.AccountModel
import com.remotearthsolutions.expensetracker.databinding.FragmentAddUpdateCategoryAccountBinding
import com.remotearthsolutions.expensetracker.utils.CategoryIcons.allIcons
import com.remotearthsolutions.expensetracker.utils.Utils.getDeviceScreenSize
import com.remotearthsolutions.expensetracker.viewmodels.AccountViewModel

class AddUpdateAccountDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentAddUpdateCategoryAccountBinding
    private var viewModel: AccountViewModel? = null
    private var accountModel: AccountModel? = null
    private var selectedIcon: String? = null
    private lateinit var iconListAdapter: IconListAdapter
    private lateinit var mContext: Context
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    fun initialize(accountModel: AccountModel?, viewModel: AccountViewModel?) {
        this.accountModel = accountModel
        this.viewModel = viewModel
        if (accountModel != null) {
            selectedIcon = accountModel.icon
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddUpdateCategoryAccountBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        if (accountModel != null) {
            binding.header.text = getString(R.string.update_account)
            binding.okBtn.text = getString(R.string.update)
            binding.nameEdtxt.setText(accountModel!!.name)
            binding.nameEdtxt.setSelection(binding.nameEdtxt.text.length)
        } else {
            binding.header.text = getString(R.string.add_account)
            binding.okBtn.text = getString(R.string.add)
        }
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            getDeviceScreenSize(mContext)!!.height / 2
        )
        binding.accountrecyclearView.layoutParams = params
        binding.accountrecyclearView.setHasFixedSize(true)
        val gridLayoutManager = GridLayoutManager(mContext, 4)
        binding.accountrecyclearView.layoutManager = gridLayoutManager
        val allIconList = allIcons
        iconListAdapter = IconListAdapter(allIconList, gridLayoutManager)
        iconListAdapter.setSelectedIcon(if (selectedIcon != null) selectedIcon else "")
        iconListAdapter.setOnItemClickListener(object : IconListAdapter.OnItemClickListener {
            override fun onItemClick(icon: String?) {
                selectedIcon = icon
                iconListAdapter.setSelectedIcon(if (selectedIcon != null) selectedIcon else "")
                iconListAdapter.notifyDataSetChanged()
            }
        })
        binding.accountrecyclearView.adapter = iconListAdapter
        binding.okBtn.setOnClickListener { saveAccount() }
    }

    private fun saveAccount() {
        val accountName = binding.nameEdtxt.text.toString().trim { it <= ' ' }
        if (accountName.isEmpty()) {
            binding.nameEdtxt.error = getString(R.string.enter_a_name_for_account)
            binding.nameEdtxt.requestFocus()
            return
        }
        if (accountName.length > 20) {
            binding.nameEdtxt.error = getString(R.string.name_should_be_less_than_20_char)
            binding.nameEdtxt.requestFocus()
            return
        }
        if (selectedIcon == null || selectedIcon!!.isEmpty()) {
            Toast.makeText(
                activity,
                getString(R.string.select_an_icon_for_the_account),
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (accountModel == null) {
            accountModel = AccountModel()
        }
        accountModel!!.name = accountName
        accountModel!!.icon = selectedIcon
        viewModel?.addOrUpdateAccount(accountModel)
        dismiss()
    }
}
