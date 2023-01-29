package com.remotearthsolutions.expensetracker.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.databinding.AddDateBinding
import com.remotearthsolutions.expensetracker.utils.Constants
import com.remotearthsolutions.expensetracker.utils.DateTimeUtils.getCurrentDate
import com.remotearthsolutions.expensetracker.utils.DateTimeUtils.getDate
import com.remotearthsolutions.expensetracker.utils.SharedPreferenceUtils
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DatePickerDialogFragment : DialogFragment(),
    View.OnClickListener {
    private lateinit var binding: AddDateBinding
    private var callback: Callback? = null
    private var cDay = 0
    private var cMonth = 0
    private var cYear = 0
    private lateinit var mContext: Context

    private lateinit var format: String
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    fun setCallback(callback: Callback?) {
        this.callback = callback
    }

    fun setInitialDate(cDay: Int, cMonth: Int, cYear: Int) {
        this.cDay = cDay
        this.cMonth = cMonth
        this.cYear = cYear
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AddDateBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        format = SharedPreferenceUtils.getInstance(requireActivity())?.getString(
            Constants.PREF_TIME_FORMAT,
            Constants.KEY_DATE_MONTH_YEAR_DEFAULT
        )!!
        binding.showdyesterday.text = getDate(format, -1)
        binding.showdtoday.text = getCurrentDate(format)
        binding.previousdate.setOnClickListener(this)
        binding.currentdate.setOnClickListener(this)
        binding.selectdate.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.currentdate -> {
                val date = getCurrentDate(format)
                callback!!.onSelectDate(date)
            }
            R.id.previousdate -> {
                val date = getDate(format, -1)
                callback!!.onSelectDate(date)
            }
            R.id.selectdate -> {
                val datePickerDialog = DatePickerDialog(
                    mContext,
                    { _: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                        cYear = year
                        cMonth = month
                        cDay = dayOfMonth
                        val calendar = Calendar.getInstance()
                        calendar[Calendar.DAY_OF_MONTH] = cDay
                        calendar[Calendar.MONTH] = cMonth
                        calendar[Calendar.YEAR] = cYear
                        val dateFormat: DateFormat = SimpleDateFormat(
                            format,
                            Locale.getDefault()
                        )
                        callback!!.onSelectDate(dateFormat.format(calendar.time))
                        dismiss()
                    },
                    cYear,
                    cMonth,
                    cDay
                )
                datePickerDialog.show()
            }
        }
    }

    interface Callback {
        fun onSelectDate(date: String?)
    }

    companion object {
        fun newInstance(title: String?): DatePickerDialogFragment {
            val frag = DatePickerDialogFragment()
            val args = Bundle()
            args.putString(Constants.KEY_TITLE, title)
            frag.arguments = args
            return frag
        }
    }
}