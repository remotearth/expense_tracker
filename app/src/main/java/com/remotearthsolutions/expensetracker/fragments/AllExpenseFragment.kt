package com.remotearthsolutions.expensetracker.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.activities.MainActivity
import com.remotearthsolutions.expensetracker.adapters.ExpenseListAdapter
import com.remotearthsolutions.expensetracker.contracts.ExpenseFragmentContract.ExpenseView
import com.remotearthsolutions.expensetracker.databaseutils.DatabaseClient
import com.remotearthsolutions.expensetracker.databaseutils.models.DateModel
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryExpense
import com.remotearthsolutions.expensetracker.utils.Constants
import com.remotearthsolutions.expensetracker.utils.SharedPreferenceUtils
import com.remotearthsolutions.expensetracker.utils.Utils.getCurrency
import com.remotearthsolutions.expensetracker.viewmodels.AllTransactionsViewModel
import com.remotearthsolutions.expensetracker.viewmodels.viewmodel_factory.BaseViewModelFactory
import kotlinx.android.synthetic.main.fragment_all_expense.view.*

class AllExpenseFragment : BaseFragment(), ExpenseView {
    private lateinit var mView: View
    private lateinit var adapter: ExpenseListAdapter
    private var viewModel: AllTransactionsViewModel? = null
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
        mView = inflater.inflate(R.layout.fragment_all_expense, container, false)
        mView.expenserecyclearView.setHasFixedSize(true)
        val llm = LinearLayoutManager(mContext)
        mView.expenserecyclearView.layoutManager = llm
        currencySymbol = "$"
        if (mContext != null) {
            currencySymbol = getCurrency(mContext!!)
        }
        val db = DatabaseClient.getInstance(mContext!!)?.appDatabase

        viewModel =
            ViewModelProviders.of(requireActivity(), BaseViewModelFactory {
                AllTransactionsViewModel(
                    this, db?.expenseDao()!!, db.categoryExpenseDao(),db.categoryDao(),
                    SharedPreferenceUtils.getInstance(mContext!!)!!
                        .getString(
                            Constants.PREF_TIME_FORMAT,
                            resources.getString(R.string.default_time_format)
                        )
                )
            }).get(AllTransactionsViewModel::class.java)

        return mView
    }

    override fun loadFilterExpense(listOffilterExpense: List<CategoryExpense>?) {
        adapter = ExpenseListAdapter(listOffilterExpense, currencySymbol!!)
        adapter.setOnItemClickListener(object : ExpenseListAdapter.OnItemClickListener {
            override fun onItemClick(categoryExpense: CategoryExpense?) {
                val copyOfCategoryExpense = categoryExpense!!.copy()
                (context as MainActivity).openAddExpenseScreen(
                    copyOfCategoryExpense,
                    getString(R.string.update_expense),
                    ExpenseFragment.Purpose.UPDATE
                )
            }
        })

        if (listOffilterExpense == null || listOffilterExpense.isEmpty()) {
            mView.expenserecyclearView.visibility = View.GONE
            mView.nodata.visibility = View.VISIBLE
        } else {
            mView.nodata.visibility = View.GONE
            mView.expenserecyclearView.visibility = View.VISIBLE
            mView.expenserecyclearView.adapter = adapter
        }
    }

    override fun loadDate(listOfDate: List<DateModel?>?) {}
    fun updateFilterListWithDate(
        startTime: Long,
        endTime: Long,
        btnId: Int
    ) {
        viewModel!!.loadFilterExpense(startTime, endTime, btnId)
    }
}