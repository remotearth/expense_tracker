package com.remotearthsolutions.expensetracker.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.recyclerview.widget.LinearLayoutManager
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.activities.main.MainActivity
import com.remotearthsolutions.expensetracker.adapters.ExpenseListAdapter
import com.remotearthsolutions.expensetracker.compose.FabButton
import com.remotearthsolutions.expensetracker.contracts.BaseView
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.CategoryExpense
import com.remotearthsolutions.expensetracker.databinding.FragmentAllExpenseBinding
import com.remotearthsolutions.expensetracker.fragments.addexpensescreen.Purpose
import com.remotearthsolutions.expensetracker.utils.AlertDialogUtils
import com.remotearthsolutions.expensetracker.utils.Utils
import com.remotearthsolutions.expensetracker.viewmodels.AllTransactionsViewModel
import org.koin.android.ext.android.inject

class AllExpenseFragment : BaseFragment() {
    private lateinit var binding: FragmentAllExpenseBinding
    private var addExpenseButtonWidth = 0
    private val viewModel: AllTransactionsViewModel by inject()
    private var adapter: ExpenseListAdapter? = null
    private var mContext: Context? = null

    private var isDeleteModeOn = false
    private var expenseToDelete: ArrayList<CategoryExpense> = ArrayList()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllExpenseBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.expenserecyclearView.setHasFixedSize(true)
        val llm = LinearLayoutManager(mContext)
        binding.expenserecyclearView.layoutManager = llm
        addExpenseButtonWidth =  binding.composeView.layoutParams.width

        viewModel.expenseListLiveData.observe(viewLifecycleOwner) {
            endDeleteModeIfOn()
            val listOfFilteredExpense = it
            if (listOfFilteredExpense == null || listOfFilteredExpense.isEmpty()) {
                binding.expenserecyclearView.visibility = View.GONE
                binding.nodata.visibility = View.VISIBLE
            } else {

                val currencySymbol = Utils.getCurrency(mContext!!)
                adapter = ExpenseListAdapter(listOfFilteredExpense, currencySymbol)
                adapter?.setOnItemClickListener(object : ExpenseListAdapter.OnItemClickListener {
                    override fun onItemClick(categoryExpense: CategoryExpense?) {
                        val copyOfCategoryExpense = categoryExpense!!.copy()
                        (context as MainActivity).openAddExpenseScreen(
                            copyOfCategoryExpense,
                            getString(R.string.update_expense),
                            Purpose.UPDATE
                        )
                    }

                    override fun onSelectToDelete(categoryExpense: CategoryExpense?) {
                        expenseToDelete.add(categoryExpense!!)
                    }

                    override fun onCancelItemDelete(categoryExpense: CategoryExpense?) {
                        expenseToDelete.remove(categoryExpense!!)
                    }
                })

                binding.nodata.visibility = View.GONE
                binding.expenserecyclearView.visibility = View.VISIBLE
                binding.expenserecyclearView.adapter = adapter
            }
        }

        binding.composeView.setContent {
            Column(
                modifier = Modifier
                    .fillMaxHeight()

                    .padding(0.dp, 0.dp, 10.dp, 10.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                }
                FabButton(
                    onClick = {
                        (mContext as MainActivity?)!!.openAddExpenseScreen(null)
                    }, modifier = Modifier
                        .size(55.dp, 55.dp)
                        .align(alignment = Alignment.End)
                )
            }
        }

        binding.deleteBtn.setOnClickListener {
            isDeleteModeOn = !isDeleteModeOn
            adapter?.setDeleteMode(isDeleteModeOn)
            if (isDeleteModeOn) {
                binding.deleteConfirmBtn.visibility = View.VISIBLE
                binding.deleteBtn.setImageResource(R.drawable.ic_cancel_delete)
                binding.composeView.layoutParams.width = 0
            } else {
                binding.deleteBtn.setImageResource(R.drawable.ic_bulk_delete)
                binding.deleteConfirmBtn.visibility = View.GONE
                binding.composeView.layoutParams.width = addExpenseButtonWidth

                expenseToDelete.forEach {
                    it.isCheckedForDelete = false
                }
                expenseToDelete.clear()
            }
        }

        binding.deleteConfirmBtn.setOnClickListener {
            if (expenseToDelete.size > 0) {
                AlertDialogUtils.show(requireContext(),
                    getResourceString(R.string.wait),
                    getResourceString(R.string.you_sure_to_delete_expenses),
                    getResourceString(R.string.yes), getResourceString(R.string.cancel), null,
                    object : BaseView.Callback {
                        override fun onOkBtnPressed() {
                            showProgress(getResourceString(R.string.please_wait))
                            viewModel.deleteSelectedExpense(expenseToDelete, callback = {
                                hideProgress()
                                showToast(getResourceString(R.string.operation_successful))
                                (mContext as MainActivity?)?.updateSummary()
                            }, onError = {
                                hideProgress()
                                showAlert(
                                    null,
                                    getResourceString(R.string.something_went_wrong),
                                    getResourceString(R.string.ok),
                                    null, null, null
                                )
                            })
                        }
                    })
                binding.composeView.layoutParams.width = addExpenseButtonWidth
            } else {
                AlertDialogUtils.show(
                    requireContext(), null,
                    getResourceString(R.string.did_not_select), getResourceString(R.string.ok),
                    null, null, null
                )
            }
        }

    }

    private fun endDeleteModeIfOn() {
        isDeleteModeOn = false
        expenseToDelete.forEach {
            it.isCheckedForDelete = false
        }
        expenseToDelete.clear()
        binding.deleteBtn.setImageResource(R.drawable.ic_bulk_delete)
        binding.deleteConfirmBtn.visibility = View.GONE
    }

    fun updateFilterListWithDate(
        startTime: Long,
        endTime: Long,
        btnId: Int
    ) {
        viewModel.loadFilterExpense(startTime, endTime, btnId)
    }

    fun updateDateFormat(updatedDateFormat: String) {
        viewModel.updateDateFormat(updatedDateFormat)
    }
}