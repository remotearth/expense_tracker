package com.remotearthsolutions.expensetracker.fragments.main

import android.util.Log
import android.view.View
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.utils.Constants
import com.remotearthsolutions.expensetracker.utils.DateTimeUtils
import com.remotearthsolutions.expensetracker.utils.DateTimeUtils.getCalendarFromDateString
import com.remotearthsolutions.expensetracker.utils.DateTimeUtils.getDate
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateFilterButtonClickListener(private val callback: Callback) :
    View.OnClickListener {
    private var selectedDate: String? = null
    private var startDate: Date? = null
    private var endDate: Date? = null
    private var simpleDateFormat: SimpleDateFormat? = null
    private lateinit var calendar: Calendar
    private var day = 0
    private var month = 0
    private var year = 0
    private var startingOfWeek = -6
    private var endingOfWeek = 0
    override fun onClick(v: View) {
        var date: String? = null
        var startTime: Long = 0
        var endTime: Long = 0
        when (v.id) {
            R.id.nextDateBtn -> {
                when (selectedDate) {
                    Constants.KEY_DAILY -> {
                        if (day < 0) {
                            day += 1
                        }
                        date = getDate(DateTimeUtils.dd_MM_yyyy, day)
                        startTime = getDateTimeInLong(DateTimeUtils.dd_MM_yyyy, date, 0, 0, 0)
                        endTime = getDateTimeInLong(DateTimeUtils.dd_MM_yyyy, date, 23, 59, 59)
                    }
                    Constants.KEY_WEEKLY -> {
                        simpleDateFormat = SimpleDateFormat(
                            DateTimeUtils.dd_MM_yyyy,
                            Locale.getDefault()
                        )
                        try {
                            if (endingOfWeek < 0) {
                                endingOfWeek += 7
                                startingOfWeek += 7
                            }
                            startDate = simpleDateFormat!!.parse(
                                getDate(
                                    DateTimeUtils.dd_MM_yyyy,
                                    startingOfWeek
                                )
                            )
                            endDate = simpleDateFormat!!.parse(
                                getDate(
                                    DateTimeUtils.dd_MM_yyyy,
                                    endingOfWeek
                                )
                            )
                        } catch (e: ParseException) {
                            Log.d("Exception", "" + e.message)
                        }
                        val weekstartdate = simpleDateFormat!!.format(startDate)
                        val weeklastdate = simpleDateFormat!!.format(endDate)
                        date = "$weekstartdate - $weeklastdate"
                        startTime =
                            getDateTimeInLong(DateTimeUtils.dd_MM_yyyy, weekstartdate, 0, 0, 0)
                        endTime =
                            getDateTimeInLong(DateTimeUtils.dd_MM_yyyy, weeklastdate, 29, 59, 59)
                    }
                    Constants.KEY_MONTHLY -> {
                        simpleDateFormat =
                            SimpleDateFormat(DateTimeUtils.MM_yy, Locale.getDefault())
                        calendar = Calendar.getInstance()
                        if (month < 0) {
                            month += 1
                        }
                        calendar.add(Calendar.MONTH, month)
                        date = simpleDateFormat!!.format(calendar.time)
                        val minimumDate = calendar.getActualMinimum(Calendar.DAY_OF_MONTH)
                        val maximumDate = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
                        startTime =
                            getDateTimeInLong(DateTimeUtils.MM_yy, date, minimumDate, 0, 0, 0)
                        endTime =
                            getDateTimeInLong(DateTimeUtils.MM_yy, date, maximumDate, 23, 59, 59)
                    }
                    Constants.KEY_YEARLY -> {
                        selectedDate = Constants.KEY_YEARLY
                        simpleDateFormat =
                            SimpleDateFormat(DateTimeUtils.yyyy, Locale.getDefault())
                        calendar = Calendar.getInstance()
                        if (year < 0) {
                            year += 1
                        }
                        calendar.add(Calendar.YEAR, year)
                        date = simpleDateFormat!!.format(calendar.time)
                        val minimumDate = calendar.getActualMinimum(Calendar.DAY_OF_YEAR)
                        val maximumDate = calendar.getActualMaximum(Calendar.DAY_OF_YEAR)
                        startTime =
                            getDateTimeInLong(DateTimeUtils.yyyy, date, minimumDate, 0, 0, 0)
                        endTime =
                            getDateTimeInLong(DateTimeUtils.yyyy, date, maximumDate, 23, 59, 59)
                    }
                }
            }
            R.id.previousDateBtn -> {
                when (selectedDate) {
                    Constants.KEY_DAILY -> {
                        day -= 1
                        date = getDate(DateTimeUtils.dd_MM_yyyy, day)
                        startTime = getDateTimeInLong(DateTimeUtils.dd_MM_yyyy, date, 0, 0, 0)
                        endTime = getDateTimeInLong(DateTimeUtils.dd_MM_yyyy, date, 23, 59, 59)
                    }
                    Constants.KEY_WEEKLY -> {
                        simpleDateFormat = SimpleDateFormat(
                            DateTimeUtils.dd_MM_yyyy,
                            Locale.getDefault()
                        )
                        try {
                            startingOfWeek -= 7
                            endingOfWeek -= 7
                            endDate = simpleDateFormat!!.parse(
                                getDate(
                                    DateTimeUtils.dd_MM_yyyy,
                                    endingOfWeek
                                )
                            )
                            startDate = simpleDateFormat!!.parse(
                                getDate(
                                    DateTimeUtils.dd_MM_yyyy,
                                    startingOfWeek
                                )
                            )
                        } catch (e: ParseException) {
                            Log.d("Exception", "" + e.message)
                        }
                        val weekstartdate = simpleDateFormat!!.format(startDate)
                        val weeklastdate = simpleDateFormat!!.format(endDate)
                        date = "$weekstartdate - $weeklastdate"
                        startTime =
                            getDateTimeInLong(DateTimeUtils.dd_MM_yyyy, weekstartdate, 0, 0, 0)
                        endTime =
                            getDateTimeInLong(DateTimeUtils.dd_MM_yyyy, weeklastdate, 29, 59, 59)
                    }
                    Constants.KEY_MONTHLY -> {
                        simpleDateFormat =
                            SimpleDateFormat(DateTimeUtils.MM_yy, Locale.getDefault())
                        calendar = Calendar.getInstance()
                        month -= 1
                        calendar.add(Calendar.MONTH, month)
                        date = simpleDateFormat!!.format(calendar.time)
                        val minimumDate = calendar.getActualMinimum(Calendar.DAY_OF_MONTH)
                        val maximumDate = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
                        startTime =
                            getDateTimeInLong(DateTimeUtils.MM_yy, date, minimumDate, 0, 0, 0)
                        endTime =
                            getDateTimeInLong(DateTimeUtils.MM_yy, date, maximumDate, 23, 59, 59)
                    }
                    Constants.KEY_YEARLY -> {
                        selectedDate = Constants.KEY_YEARLY
                        simpleDateFormat =
                            SimpleDateFormat(DateTimeUtils.yyyy, Locale.getDefault())
                        calendar = Calendar.getInstance()
                        year -= 1
                        calendar.add(Calendar.YEAR, year)
                        date = simpleDateFormat!!.format(calendar.time)
                        val minimumDate = calendar.getActualMinimum(Calendar.DAY_OF_YEAR)
                        val maximumDate = calendar.getActualMaximum(Calendar.DAY_OF_YEAR)
                        startTime =
                            getDateTimeInLong(DateTimeUtils.yyyy, date, minimumDate, 0, 0, 0)
                        endTime =
                            getDateTimeInLong(DateTimeUtils.yyyy, date, maximumDate, 23, 59, 59)
                    }
                }
            }
            R.id.dailyRangeBtn -> {
                resetDate()
                selectedDate = Constants.KEY_DAILY
                date = getDate(DateTimeUtils.dd_MM_yyyy, day)
                startTime = getDateTimeInLong(DateTimeUtils.dd_MM_yyyy, date, 0, 0, 0)
                endTime = getDateTimeInLong(DateTimeUtils.dd_MM_yyyy, date, 23, 59, 59)
            }
            R.id.weeklyRangeBtn -> {
                resetDate()
                selectedDate = Constants.KEY_WEEKLY
                simpleDateFormat =
                    SimpleDateFormat(DateTimeUtils.dd_MM_yyyy, Locale.getDefault())
                try {
                    startDate = simpleDateFormat!!.parse(
                        getDate(
                            DateTimeUtils.dd_MM_yyyy,
                            startingOfWeek
                        )
                    )
                    endDate = simpleDateFormat!!.parse(
                        getDate(
                            DateTimeUtils.dd_MM_yyyy,
                            endingOfWeek
                        )
                    )
                } catch (e: ParseException) {
                    Log.d("Exception", "" + e.message)
                }
                val weekstartdate = simpleDateFormat!!.format(startDate)
                val weeklastdate = simpleDateFormat!!.format(endDate)
                date = "$weekstartdate - $weeklastdate"
                startTime = getDateTimeInLong(DateTimeUtils.dd_MM_yyyy, weekstartdate, 0, 0, 0)
                endTime = getDateTimeInLong(DateTimeUtils.dd_MM_yyyy, weeklastdate, 29, 59, 59)
            }
            R.id.monthlyRangeBtn -> {
                resetDate()
                selectedDate = Constants.KEY_MONTHLY
                simpleDateFormat =
                    SimpleDateFormat(DateTimeUtils.MM_yy, Locale.getDefault())
                calendar = Calendar.getInstance()
                calendar.add(Calendar.MONTH, month)
                date = simpleDateFormat!!.format(calendar.time)
                val minimumDate = calendar.getActualMinimum(Calendar.DAY_OF_MONTH)
                val maximumDate = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
                startTime = getDateTimeInLong(DateTimeUtils.MM_yy, date, minimumDate, 0, 0, 0)
                endTime = getDateTimeInLong(DateTimeUtils.MM_yy, date, maximumDate, 23, 59, 59)
            }
            R.id.yearlyRangeBtn -> {
                resetDate()
                selectedDate = Constants.KEY_YEARLY
                val sdf =
                    SimpleDateFormat(DateTimeUtils.yyyy, Locale.getDefault())
                calendar = Calendar.getInstance()
                calendar.add(Calendar.YEAR, year)
                date = sdf.format(calendar.time)
                val minimumDate = calendar.getActualMinimum(Calendar.DAY_OF_YEAR)
                val maximumDate = calendar.getActualMaximum(Calendar.DAY_OF_YEAR)
                startTime = getDateTimeInLong(DateTimeUtils.yyyy, date, minimumDate, 0, 0, 0)
                endTime = getDateTimeInLong(DateTimeUtils.yyyy, date, maximumDate, 23, 59, 59)
            }
        }
        callback.onDateChanged(v.id, date, startTime, endTime)
    }

    private fun getDateTimeInLong(
        format: String,
        dateStr: String,
        hour: Int,
        min: Int,
        sec: Int
    ): Long {
        val calendarForWeekLastDay =
            getCalendarFromDateString(format, dateStr)
        calendarForWeekLastDay[Calendar.HOUR_OF_DAY] = hour
        calendarForWeekLastDay[Calendar.MINUTE] = min
        calendarForWeekLastDay[Calendar.SECOND] = sec
        return calendarForWeekLastDay.timeInMillis
    }

    private fun getDateTimeInLong(
        format: String,
        dateStr: String?,
        dateType: Int,
        hour: Int,
        min: Int,
        sec: Int
    ): Long {
        val calendarForWeekLastDay =
            getCalendarFromDateString(format, dateStr)
        calendarForWeekLastDay[Calendar.DATE] = dateType
        calendarForWeekLastDay[Calendar.HOUR_OF_DAY] = hour
        calendarForWeekLastDay[Calendar.MINUTE] = min
        calendarForWeekLastDay[Calendar.SECOND] = sec
        return calendarForWeekLastDay.timeInMillis
    }

    private fun resetDate() {
        day = 0
        month = 0
        year = 0
        startingOfWeek = -6
        endingOfWeek = 0
    }

    interface Callback {
        fun onDateChanged(
            btnId: Int,
            date: String?,
            startTime: Long,
            endTime: Long
        )
    }

}