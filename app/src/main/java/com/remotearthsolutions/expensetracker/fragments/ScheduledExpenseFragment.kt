package com.remotearthsolutions.expensetracker.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.adapters.ScheduledExpenseListAdapter
import com.remotearthsolutions.expensetracker.contracts.BaseView
import com.remotearthsolutions.expensetracker.databaseutils.DatabaseClient
import com.remotearthsolutions.expensetracker.databaseutils.models.ScheduledExpenseModel
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.ScheduledExpenseDto
import com.remotearthsolutions.expensetracker.utils.AlertDialogUtils
import com.remotearthsolutions.expensetracker.utils.Constants
import com.remotearthsolutions.expensetracker.utils.SharedPreferenceUtils
import com.remotearthsolutions.expensetracker.utils.Utils
import com.remotearthsolutions.expensetracker.viewmodels.ScheduledExpenseViewModel
import com.remotearthsolutions.expensetracker.viewmodels.viewmodel_factory.BaseViewModelFactory
import kotlinx.android.synthetic.main.fragment_recyclerview.view.*
import kotlinx.android.synthetic.main.listitem_scheduledexpense.view.*

class ScheduledExpenseFragment : BaseFragment() {

    private lateinit var mView: View
    private lateinit var viewModel: ScheduledExpenseViewModel
    private lateinit var currencySymbol: String
    private lateinit var format: String
    private lateinit var adapter: ScheduledExpenseListAdapter

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
        registerBackButton()
        mView = inflater.inflate(R.layout.fragment_recyclerview, container, false)
        return mView
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currencySymbol = Utils.getCurrency(activity!!)
        format = SharedPreferenceUtils.getInstance(activity!!)!!.getString(
            Constants.PREF_TIME_FORMAT,
            Constants.KEY_DATE_MONTH_YEAR_DEFAULT
        )

        val db = DatabaseClient.getInstance(mContext!!)?.appDatabase

        viewModel = ViewModelProviders.of(requireActivity(), BaseViewModelFactory {
            ScheduledExpenseViewModel(
                db?.scheduleExpenseDao()!!
            )
        }).get(ScheduledExpenseViewModel::class.java)

        mView.recyclerView.setHasFixedSize(true)
        val llm = LinearLayoutManager(mContext)
        mView.recyclerView.layoutManager = llm

        viewModel.scheduledExpensesLiveData.observe(this, Observer {
            if (it == null || it.isEmpty()) {
                mView.nodatacontainer.visibility = View.VISIBLE
                mView.recyclerView.visibility = View.GONE
                mView.noEntryMessage.text = getString(R.string.no_scheduled_expenses)
            } else {
                mView.nodatacontainer.visibility = View.GONE
                mView.recyclerView.visibility = View.VISIBLE
                adapter = ScheduledExpenseListAdapter(it, currencySymbol, format, onItemClick)
                mView.recyclerView.adapter = adapter
            }
        })

        viewModel.loadScheduledExpenses()
    }

    private val onItemClick = object : ScheduledExpenseListAdapter.OnItemClickListener {
        override fun onItemDeleteButtonClick(scheduledExpense: ScheduledExpenseDto) {
            AlertDialogUtils.show(mContext,
                null,
                getString(R.string.confirm_delete_scheduled_entry),
                getString(R.string.ok)
                ,
                getString(R.string.cancel),
                object : BaseView.Callback {
                    override fun onOkBtnPressed() {
                        val scheduleExpenseModel = ScheduledExpenseModel().fromDto(scheduledExpense)
                        viewModel.removeScheduledExpense(scheduleExpenseModel)
                    }
                })
        }
    }
}
