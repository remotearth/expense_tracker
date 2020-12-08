package com.remotearthsolutions.expensetracker.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.activities.main.MainActivity
import com.remotearthsolutions.expensetracker.adapters.ExpenseListAdapter
import com.remotearthsolutions.expensetracker.contracts.BaseView
import com.remotearthsolutions.expensetracker.databaseutils.DatabaseClient
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.CategoryExpense
import com.remotearthsolutions.expensetracker.fragments.addexpensescreen.Purpose
import com.remotearthsolutions.expensetracker.utils.AlertDialogUtils
import com.remotearthsolutions.expensetracker.utils.Constants
import com.remotearthsolutions.expensetracker.utils.SharedPreferenceUtils
import com.remotearthsolutions.expensetracker.utils.Utils
import com.remotearthsolutions.expensetracker.viewmodels.AllTransactionsViewModel
import com.remotearthsolutions.expensetracker.viewmodels.viewmodel_factory.BaseViewModelFactory
import kotlinx.android.synthetic.main.fragment_all_expense.*
import kotlinx.android.synthetic.main.fragment_all_expense.view.*

class AllExpenseFragment : BaseFragment() {
    private lateinit var mView: View
    private var viewModel: AllTransactionsViewModel? = null
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
    ): View? {
        mView = inflater.inflate(R.layout.fragment_all_expense, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView.expenserecyclearView.setHasFixedSize(true)
        val llm = LinearLayoutManager(mContext)
        mView.expenserecyclearView.layoutManager = llm
        val db = DatabaseClient.getInstance(mContext!!).appDatabase

        viewModel =
            ViewModelProvider(requireActivity(), BaseViewModelFactory {
                AllTransactionsViewModel(
                    db.categoryExpenseDao(), db.expenseDao(), db.categoryDao(), db.accountDao(),
                    SharedPreferenceUtils.getInstance(mContext!!)!!
                        .getString(
                            Constants.PREF_TIME_FORMAT,
                            resources.getString(R.string.default_time_format)
                        )
                )
            }).get(AllTransactionsViewModel::class.java)

        viewModel?.expenseListLiveData?.observe(viewLifecycleOwner, Observer {
            endDeleteModeIfOn()
            val listOffilterExpense = it
            if (listOffilterExpense == null || listOffilterExpense.isEmpty()) {
                mView.expenserecyclearView.visibility = View.GONE
                mView.nodata.visibility = View.VISIBLE
            } else {

                val currencySymbol = Utils.getCurrency(mContext!!)
                adapter = ExpenseListAdapter(listOffilterExpense, currencySymbol)
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

                mView.nodata.visibility = View.GONE
                mView.expenserecyclearView.visibility = View.VISIBLE
                mView.expenserecyclearView.adapter = adapter
            }
        })

        deleteBtn.setOnClickListener {
            isDeleteModeOn = !isDeleteModeOn
            adapter?.setDeleteMode(isDeleteModeOn)
            if (isDeleteModeOn) {
                deleteConfirmBtn.visibility = View.VISIBLE
                deleteBtn.setImageResource(R.drawable.ic_cancel_delete)
            } else {
                deleteBtn.setImageResource(R.drawable.ic_bulk_delete)
                deleteConfirmBtn.visibility = View.GONE
                expenseToDelete.forEach {
                    it.isCheckedForDelete = false
                }
                expenseToDelete.clear()
            }
        }

        deleteConfirmBtn.setOnClickListener {
            if (expenseToDelete.size > 0) {
                AlertDialogUtils.show(requireContext(),
                    getResourceString(R.string.wait),
                    getResourceString(R.string.you_sure_to_delete_expenses),
                    getResourceString(R.string.yes), getResourceString(R.string.cancel), null,
                    object : BaseView.Callback {
                        override fun onOkBtnPressed() {
                            showProgress(getResourceString(R.string.please_wait))
                            viewModel?.deleteSelectedExpense(expenseToDelete, callback = {
                                hideProgress()
                                showToast(getResourceString(R.string.operation_successful))
                                val mainActivity = mContext as MainActivity?
                                mainActivity!!.updateSummary()
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
        deleteBtn.setImageResource(R.drawable.ic_bulk_delete)
        deleteConfirmBtn.visibility = View.GONE
    }

    fun updateFilterListWithDate(
        startTime: Long,
        endTime: Long,
        btnId: Int
    ) {
        viewModel!!.loadFilterExpense(startTime, endTime, btnId)
    }

    fun updateDateFormat(updatedDateFormat: String) {
        viewModel?.updateDateFormat(updatedDateFormat)
    }
}