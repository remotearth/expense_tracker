package com.remotearthsolutions.expensetracker.utils

import com.github.mikephil.charting.formatter.ValueFormatter
import kotlin.math.max


class DayAxisValueFormatter(private val dates: ArrayList<String>) :
    ValueFormatter() {

    override fun getFormattedValue(value: Float): String {
        var str = ""
        try {
            str = dates[value.toInt()]
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return str
    }

}