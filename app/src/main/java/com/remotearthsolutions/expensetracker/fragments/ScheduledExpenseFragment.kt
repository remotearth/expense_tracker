package com.remotearthsolutions.expensetracker.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.adapters.ScheduledExpenseListAdapter
import com.remotearthsolutions.expensetracker.contracts.BaseView
import com.remotearthsolutions.expensetracker.databaseutils.models.ScheduledExpenseModel
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.ScheduledExpenseDto
import com.remotearthsolutions.expensetracker.databinding.FragmentRecyclerviewBinding
import com.remotearthsolutions.expensetracker.utils.AlertDialogUtils
import com.remotearthsolutions.expensetracker.utils.Constants
import com.remotearthsolutions.expensetracker.utils.SharedPreferenceUtils
import com.remotearthsolutions.expensetracker.utils.Utils
import com.remotearthsolutions.expensetracker.viewmodels.ScheduledExpenseViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ScheduledExpenseFragment : BaseFragment() {

    private lateinit var binding: FragmentRecyclerviewBinding
    private val viewModel: ScheduledExpenseViewModel by viewModel { parametersOf(requireContext()) }
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
    ): View {
        registerBackButton()
        binding = FragmentRecyclerviewBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currencySymbol = Utils.getCurrency(requireActivity())
        format = SharedPreferenceUtils.getInstance(requireActivity())!!.getString(
            Constants.PREF_TIME_FORMAT,
            Constants.KEY_DATE_MONTH_YEAR_DEFAULT
        )

        binding.recyclerView.setHasFixedSize(true)
        val llm = LinearLayoutManager(mContext)
        binding.recyclerView.layoutManager = llm

        viewModel.scheduledExpensesLiveData.observe(viewLifecycleOwner) {
            if (it == null || it.isEmpty()) {
                binding.nodatacontainer.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.INVISIBLE
                binding.noEntryMessage.text = getResourceString(R.string.no_scheduled_expenses)
            } else {
                binding.nodatacontainer.visibility = View.INVISIBLE
                binding.recyclerView.visibility = View.VISIBLE
                adapter = ScheduledExpenseListAdapter(it, currencySymbol, format, onItemClick)
                binding.recyclerView.adapter = adapter
            }
        }

        viewModel.loadScheduledExpenses()
    }

    private val onItemClick = object : ScheduledExpenseListAdapter.OnItemClickListener {
        override fun onItemDeleteButtonClick(scheduledExpense: ScheduledExpenseDto) {
            AlertDialogUtils.show(mContext,
                null,
                getResourceString(R.string.confirm_delete_scheduled_entry),
                getResourceString(R.string.ok),
                getResourceString(R.string.cancel), null,
                object : BaseView.Callback {
                    override fun onOkBtnPressed() {
                        val scheduleExpenseModel = ScheduledExpenseModel().fromDto(scheduledExpense)
                        viewModel.removeScheduledExpense(scheduleExpenseModel)

                    }
                })
        }
    }
}
