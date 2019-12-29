package com.remotearthsolutions.expensetracker.utils

import android.util.Log
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtils {
    val dd_MM_yyyy: String = Constants.KEY_DATE_MONTH_YEAR_DEFAULT
    val dd_MM_yyyy_h_mm: String = Constants.KEY_DATE_MONTH_YEAR_HOUR_MINIT_AM_PM
    val MM_yy: String = Constants.KEY_MONTH_YEAR
    val yyyy: String = Constants.KEY_YEAR
    val mmmm: String = Constants.KEY_MONTH
    fun getCurrentDate(format: String?): String {
        val calendar = Calendar.getInstance()
        val dateFormat: DateFormat =
            SimpleDateFormat(format, Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    val currentTime: String
        get() {
            val calendar = Calendar.getInstance()
            val dateFormat: DateFormat = SimpleDateFormat(
                Constants.KEY_HOUR_MIN_SEC,
                Locale.getDefault()
            )
            return dateFormat.format(calendar.time)
        }

    fun getDate(format: String?, days: Int): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, days)
        val dateFormat: DateFormat =
            SimpleDateFormat(format, Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    fun getDate(datetime: Long, format: String?): String {
        val calendar =
            Calendar.getInstance(Locale.getDefault())
        calendar.timeInMillis = datetime
        val dateFormat: DateFormat =
            SimpleDateFormat(format, Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    fun getCalendarFromDateString(
        format: String?,
        date: String?
    ): Calendar {
        val dateFormat: DateFormat =
            SimpleDateFormat(format, Locale.getDefault())
        val calendar = Calendar.getInstance()
        try {
            val dateObj = dateFormat.parse(date)
            calendar.time = dateObj
        } catch (e: ParseException) {
            Log.d("Exception", "" + e.message)
        }
        return calendar
    }

    fun getTimeInMillisFromDateStr(dateStr: String?, format: String?): Long {
        val dateFormat: DateFormat =
            SimpleDateFormat(format, Locale.getDefault())
        val calendar = Calendar.getInstance()
        try {
            val dateObj = dateFormat.parse(dateStr)
            calendar.time = dateObj
        } catch (e: ParseException) {
            Log.d("Exception", "" + e.message)
        }
        return calendar.timeInMillis
    }
}