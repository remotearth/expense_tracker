package com.remotearthsolutions.expensetracker.utils

import android.content.Context
import androidx.core.content.ContextCompat
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig
import com.razerdp.widget.animatedpieview.data.SimplePieInfo
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.entities.ExpeneChartData
import com.remotearthsolutions.expensetracker.utils.Utils.ScreenSize
import com.remotearthsolutions.expensetracker.utils.Utils.formatDecimalValues

class ChartManagerImpl : ChartManager {
    private val colors = intArrayOf(
        R.color.color1,
        R.color.color8,
        R.color.color3,
        R.color.color4,
        R.color.color14,
        R.color.color26,
        R.color.color28,
        R.color.color29,
        R.color.color5,
        R.color.color9,
        R.color.color23,
        R.color.color21,
        R.color.color2,
        R.color.color18,
        R.color.color11,
        R.color.color10,
        R.color.color22,
        R.color.color27,
        R.color.color17,
        R.color.color30,
        R.color.color37,
        R.color.color13,
        R.color.color31,
        R.color.color19,
        R.color.color32,
        R.color.color24
    )
    private val config: AnimatedPieViewConfig = AnimatedPieViewConfig()
    override fun initPierChart(deviceDp: Int, screenSize: ScreenSize?) {
        val defaultDpi = 320
        val defaultStrokeWidth = 32
        val defaultTextSize = 25
        val defaultPieRadius = 120
        val strokeWidth = defaultStrokeWidth * deviceDp / defaultDpi
        val textSize = defaultTextSize * deviceDp / defaultDpi
        var pieRadius = defaultPieRadius * deviceDp / defaultDpi
        if (screenSize != null) {
            pieRadius = screenSize.width / 5
        }
        config.startAngle(-90f)
            .canTouch(true)
            .drawText(true)
            .autoSize(true)
            .pieRadius(pieRadius.toFloat())
            .strokeWidth(strokeWidth)
            .textSize(textSize.toFloat())
            .duration(500)
    }

    override fun loadExpensePieChart(
        context: Context?,
        chartView: ChartView?,
        data: List<ExpeneChartData?>?
    ) {
        var sum = 0.0
        for (item in data!!) {
            sum += item!!.value
        }
        var colorPosition = 0
        for (item in data) {
            val `val` = item!!.value * 100 / sum
            var catName = item.categoryName
            if (catName.length > 9) {
                catName = catName.substring(0, 7) + ".."
            }
            config.addData(
                SimplePieInfo(
                    item.value, ContextCompat.getColor(context!!, colors[colorPosition]),
                    "$catName\n(" + formatDecimalValues(
                        `val`
                    ) + "%)"
                )
            )
            colorPosition++
            if (colorPosition > 25) {
                colorPosition = 0
            }
        }
        chartView?.loadChartConfig(config)
    }

    interface ChartView {
        fun loadChartConfig(config: AnimatedPieViewConfig?)
    }

}