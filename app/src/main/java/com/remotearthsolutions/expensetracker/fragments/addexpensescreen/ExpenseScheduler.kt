package com.remotearthsolutions.expensetracker.fragments.addexpensescreen

import java.util.*

object ExpenseScheduler {
    const val SCHEDULED_EXPENSE_ID = "SCHEDULED_EXPENSE_ID"
    fun nextOcurrenceDate(currentDateTime: Long, period: Int, periodType: Int): Long {
        val cal = Calendar.getInstance()
        cal.timeInMillis = currentDateTime
        when (periodType) {
            0 -> cal.add(Calendar.DAY_OF_MONTH, period)
            1 -> cal.add(Calendar.WEEK_OF_MONTH, period)
            2 -> cal.add(Calendar.MONTH, period)
            3 -> cal.add(Calendar.YEAR, period)
        }
        return cal.timeInMillis
    }
}
