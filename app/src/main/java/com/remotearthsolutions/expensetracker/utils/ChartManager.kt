package com.remotearthsolutions.expensetracker.utils

import android.content.Context
import com.remotearthsolutions.expensetracker.entities.ExpeneChartData
import com.remotearthsolutions.expensetracker.utils.Utils.ScreenSize

interface ChartManager {
    fun initPierChart(deviceDp: Int, screenSize: ScreenSize?)
    fun loadExpensePieChart(
        context: Context?,
        chartView: com.remotearthsolutions.expensetracker.utils.ChartManagerImpl.ChartView?,
        data: List<ExpeneChartData?>?
    )
}