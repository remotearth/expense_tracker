package com.remotearthsolutions.expensetracker.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.activities.MainActivity
import com.remotearthsolutions.expensetracker.adapters.ExpenseListAdapter
import com.remotearthsolutions.expensetracker.contracts.ExpenseFragmentContract.ExpenseView
import com.remotearthsolutions.expensetracker.databaseutils.DatabaseClient
import com.remotearthsolutions.expensetracker.databaseutils.models.DateModel
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.CategoryExpense
import com.remotearthsolutions.expensetracker.utils.Utils.getCurrency
import com.remotearthsolutions.expensetracker.viewmodels.ExpenseViewModel
import com.remotearthsolutions.expensetracker.viewmodels.viewmodel_factory.BaseViewModelFactory

class AllExpenseFragment : BaseFragment(), ExpenseView {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ExpenseListAdapter
    private var viewModel: ExpenseViewModel? = null
    private var currencySymbol: String? = null
    private var layout: LinearLayout? = null
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
        val view =
            inflater.inflate(R.layout.fragment_all_expense, container, false)
        layout = view.findViewById(R.id.nodata)
        recyclerView = view.findViewById(R.id.expenserecyclearView)
        recyclerView.setHasFixedSize(true)
        val llm = LinearLayoutManager(mContext)
        recyclerView.layoutManager = llm
        currencySymbol = "$"
        if (mContext != null) {
            currencySymbol = getCurrency(mContext!!)
        }
        val db = DatabaseClient.getInstance(mContext!!)?.appDatabase

        viewModel =
            ViewModelProviders.of(this, BaseViewModelFactory {
                ExpenseViewModel(this, db?.expenseDao()!!, db.categoryExpenseDao())
            }).get(ExpenseViewModel::class.java)

        return view
    }

    override fun loadFilterExpense(listOffilterExpense: List<CategoryExpense>?) {
        adapter = ExpenseListAdapter(listOffilterExpense, currencySymbol!!)
        adapter.setOnItemClickListener(object : ExpenseListAdapter.OnItemClickListener {
            override fun onItemClick(categoryExpense: CategoryExpense?) {
                (context as MainActivity?)!!.openAddExpenseScreen(categoryExpense!!)
            }
        })

        if (listOffilterExpense == null || listOffilterExpense.isEmpty()) {
            recyclerView.visibility = View.GONE
            layout!!.visibility = View.VISIBLE
        } else {
            layout!!.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            recyclerView.adapter = adapter
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