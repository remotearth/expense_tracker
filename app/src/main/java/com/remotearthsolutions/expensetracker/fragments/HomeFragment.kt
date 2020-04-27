package com.remotearthsolutions.expensetracker.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.activities.ApplicationObject
import com.remotearthsolutions.expensetracker.activities.main.MainActivity
import com.remotearthsolutions.expensetracker.adapters.CategoryListAdapter
import com.remotearthsolutions.expensetracker.contracts.HomeFragmentContract
import com.remotearthsolutions.expensetracker.databaseutils.DatabaseClient
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryExpense
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel
import com.remotearthsolutions.expensetracker.databinding.FragmentHomeBinding
import com.remotearthsolutions.expensetracker.entities.ExpenseChartData
import com.remotearthsolutions.expensetracker.utils.MPPieChart
import com.remotearthsolutions.expensetracker.utils.Utils
import com.remotearthsolutions.expensetracker.utils.Utils.getDeviceScreenSize
import com.remotearthsolutions.expensetracker.viewmodels.HomeFragmentViewModel
import com.remotearthsolutions.expensetracker.viewmodels.viewmodel_factory.BaseViewModelFactory

class HomeFragment : BaseFragment(),
    HomeFragmentContract.View, View.OnClickListener {
    private lateinit var adapter: CategoryListAdapter
    private var viewModel: HomeFragmentViewModel? = null
    private lateinit var binding: FragmentHomeBinding
    private var limitOfCategory: Int? = null
    private var startTime: Long = 0
    private var endTime: Long = 0
    private var listOfCategoryWithAmount: List<ExpenseChartData>? = null
    private lateinit var mContext: Context
    private lateinit var chartManager: MPPieChart
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding.addCategoryBtn.setOnClickListener(this)
        binding.fab.setOnClickListener(this)
        binding.recyclerView.setHasFixedSize(true)
        val llm =
            LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.layoutManager = llm
        val db = DatabaseClient.getInstance(mContext)?.appDatabase

        chartManager = MPPieChart()
        chartManager.initPierChart(binding.chartView, getDeviceScreenSize(mContext))

        viewModel =
            ViewModelProviders.of(this, BaseViewModelFactory {
                HomeFragmentViewModel(
                    this,
                    db?.categoryExpenseDao()!!,
                    db.categoryDao(),
                    db.accountDao()
                )
            }).get(HomeFragmentViewModel::class.java).apply {
                this.init()
            }

        viewModel!!.init()
        viewModel!!.loadExpenseChart(startTime, endTime)
        viewModel!!.numberOfItem.observe(
            viewLifecycleOwner,
            Observer { integer: Int? -> limitOfCategory = integer }
        )
    }

    override fun showCategories(categories: List<CategoryModel>?) {
        adapter = CategoryListAdapter(categories)
        adapter.setScreenSize(
            getDeviceScreenSize(mContext)
        )
        adapter.setOnItemClickListener(object : CategoryListAdapter.OnItemClickListener {
            override fun onItemClick(category: CategoryModel?) {
                val categoryExpense =
                    CategoryExpense()
                categoryExpense.setCategory(category!!)
                (mContext as MainActivity).openAddExpenseScreen(categoryExpense)
            }

        })
        binding.recyclerView.adapter = adapter
    }

    override fun loadExpenseChart(listOfCategoryWithAmount: List<ExpenseChartData>?) {
        this.listOfCategoryWithAmount = listOfCategoryWithAmount
        if (listOfCategoryWithAmount == null || listOfCategoryWithAmount.isEmpty()) {
            binding.chartView.visibility = View.GONE
            binding.nodatacontainer.visibility = View.VISIBLE
        } else {
            binding.chartView.visibility = View.VISIBLE
            binding.nodatacontainer.visibility = View.GONE
            chartManager.loadExpensePieChart(
                mContext, binding.chartView, listOfCategoryWithAmount,
                Utils.getCurrency(mContext)
            )
        }
    }

    override fun onClick(v: View) {
        if (v.id == R.id.addCategoryBtn) {
            if (limitOfCategory!! < 20 ||
                ((mContext as Activity?)!!.application as ApplicationObject).isPremium
            ) {
                val ft =
                    childFragmentManager.beginTransaction()
                val categoryDialogFragment: AddCategoryDialogFragment =
                    AddCategoryDialogFragment.newInstance(getString(R.string.add_category))
                categoryDialogFragment.setCallback(object : AddCategoryDialogFragment.Callback {
                    override fun onCategoryAdded(categoryModel: CategoryModel?) {
                        categoryDialogFragment.dismiss()
                    }
                })
                categoryDialogFragment.show(ft, AddCategoryDialogFragment::class.java.name)
            } else {
                showAlert(
                    getString(R.string.attention),
                    getString(R.string.you_need_to_be_premium_user_to_add_more_categories),
                    getString(R.string.ok),
                    null,
                    null
                )
            }
        } else if (v.id == R.id.fab) {
            binding.fab.isClickable = false
            (mContext as MainActivity?)!!.openAddExpenseScreen(null)
            Handler().postDelayed({ binding.fab.isClickable = true }, 500)
        }
    }

    fun updateChartView(startTime: Long, endTime: Long) {
        this.startTime = startTime
        this.endTime = endTime
        viewModel!!.loadExpenseChart(startTime, endTime)
    }

    fun refresh() {
        loadExpenseChart(listOfCategoryWithAmount)
    }
}