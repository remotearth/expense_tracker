package com.remotearthsolutions.expensetracker.utils

import com.github.mikephil.charting.formatter.ValueFormatter

class DayAxisValueFormatter(private val dates: ArrayList<String>) :
    ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        if (value.toInt() < dates.size) {
            return dates[value.toInt()]
        }
        return ""
    }
}
