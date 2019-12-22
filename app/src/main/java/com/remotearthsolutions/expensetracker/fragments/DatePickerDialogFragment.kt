package com.remotearthsolutions.expensetracker.fragments

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.utils.Constants
import com.remotearthsolutions.expensetracker.utils.DateTimeUtils
import com.remotearthsolutions.expensetracker.utils.DateTimeUtils.getCurrentDate
import com.remotearthsolutions.expensetracker.utils.DateTimeUtils.getDate
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DatePickerDialogFragment : DialogFragment(),
    View.OnClickListener {
    private lateinit var previousDate: LinearLayout
    private lateinit var currentDate: LinearLayout
    private lateinit var selectDate: LinearLayout
    private lateinit var todayDateTv: TextView
    private lateinit var yesterdayDateTv: TextView
    private var callback: Callback? = null
    private var cDay = 0
    private var cMonth = 0
    private var cYear = 0
    private lateinit var mContext: Context
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
    ): View? {
        return inflater.inflate(R.layout.add_date, container, false)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        previousDate = view.findViewById(R.id.previousdate)
        currentDate = view.findViewById(R.id.currentdate)
        selectDate = view.findViewById(R.id.selectdate)
        yesterdayDateTv = view.findViewById(R.id.showdyesterday)
        todayDateTv = view.findViewById(R.id.showdtoday)
        yesterdayDateTv.text = getDate(DateTimeUtils.dd_MM_yyyy, -1)
        todayDateTv.text = getCurrentDate(DateTimeUtils.dd_MM_yyyy)
        previousDate.setOnClickListener(this)
        currentDate.setOnClickListener(this)
        selectDate.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.currentdate -> {
                val date = getCurrentDate(DateTimeUtils.dd_MM_yyyy)
                callback!!.onSelectDate(date)
            }
            R.id.previousdate -> {
                val date = getDate(DateTimeUtils.dd_MM_yyyy, -1)
                callback!!.onSelectDate(date)
            }
            R.id.selectdate -> {
                val datePickerDialog = DatePickerDialog(
                    mContext,
                    OnDateSetListener { _: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                        cYear = year
                        cMonth = month
                        cDay = dayOfMonth
                        val calendar = Calendar.getInstance()
                        calendar[Calendar.DAY_OF_MONTH] = cDay
                        calendar[Calendar.MONTH] = cMonth
                        calendar[Calendar.YEAR] = cYear
                        val dateFormat: DateFormat = SimpleDateFormat(
                            DateTimeUtils.dd_MM_yyyy,
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