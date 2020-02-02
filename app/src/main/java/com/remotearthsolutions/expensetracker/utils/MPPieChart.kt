package com.remotearthsolutions.expensetracker.utils

import android.content.Context
import android.graphics.Color
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.contracts.BaseView
import com.remotearthsolutions.expensetracker.entities.ExpenseChartData


class MPPieChart : OnChartValueSelectedListener {
    private lateinit var data: List<ExpenseChartData>
    private lateinit var context: Context
    private lateinit var currencySymbol: String
    private var chartView: PieChart? = null

    fun initPierChart(chart: PieChart, screenSize: Utils.ScreenSize?) {
        val deviceWidthDivTen = (screenSize!!.width / 10) / 3
        chart.setUsePercentValues(true)
        chart.description.isEnabled = false
        chart.setExtraOffsets(deviceWidthDivTen.toFloat(), 10f, deviceWidthDivTen.toFloat(), 5f)

        chart.dragDecelerationFrictionCoef = 0.95f

        chart.isDrawHoleEnabled = true
        chart.setHoleColor(Color.WHITE)

        chart.setTransparentCircleColor(Color.WHITE)
        chart.setTransparentCircleAlpha(110)

        chart.holeRadius = 58f
        chart.transparentCircleRadius = 61f

        chart.setDrawCenterText(true)

        chart.rotationAngle = 0f
        chart.isRotationEnabled = true
        chart.isHighlightPerTapEnabled = true

        chart.setOnChartValueSelectedListener(this)
        chart.animateY(1500, Easing.EaseInOutQuad)

        val l = chart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)

        chart.setEntryLabelColor(Color.GRAY)
        chart.setEntryLabelTextSize(10f)
    }

    fun loadExpensePieChart(
        context: Context,
        chartView: PieChart,
        data: List<ExpenseChartData>?,
        currencySymbol: String
    ) {
        this.data = data!!
        this.context = context
        this.currencySymbol = currencySymbol
        this.chartView = chartView
        val entries = ArrayList<PieEntry>()


        var sum = 0.0
        for (item in data) {
            sum += item.value
        }
        chartView.centerText = generateCenterSpannableText(context, sum, currencySymbol)
        for (item in data) {
            val expensePercent = Utils.formatDecimalValues(item.percentValue).toFloat()
            var catName = item.categoryName
            if (catName.length > 9) {
                catName = catName.substring(0, 7) + ".."
            }
            entries.add(PieEntry(expensePercent, catName))
        }

        val dataSet = PieDataSet(entries, "")

        dataSet.setDrawIcons(false)

        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0.0f, 40.0f)
        dataSet.selectionShift = 5f
        // add a lot of colors
        val colors = ArrayList<Int>()
        for (c in ColorTemplate.VORDIPLOM_COLORS) colors.add(c)
        for (c in ColorTemplate.JOYFUL_COLORS) colors.add(c)
        for (c in ColorTemplate.COLORFUL_COLORS) colors.add(c)
        for (c in ColorTemplate.LIBERTY_COLORS) colors.add(c)
        for (c in ColorTemplate.PASTEL_COLORS) colors.add(c)

        colors.add(ColorTemplate.getHoloBlue())
        dataSet.colors = colors

        val data1 = PieData(dataSet)
        data1.setValueFormatter(PercentFormatter(chartView))
        data1.setValueTextSize(11f)
        data1.setValueTextColor(Color.BLACK)
        chartView.data = data1

        chartView.highlightValues(null)
        chartView.invalidate()
    }

    private fun generateCenterSpannableText(
        context: Context,
        sum: Double,
        currencySymbol: String
    ): SpannableString? {
        val totalExpenseStr = context.getString(R.string.total_expense)
        val s =
            SpannableString("$totalExpenseStr\n${Utils.formatDecimalValues(sum)} $currencySymbol")
        s.setSpan(ForegroundColorSpan(Color.RED), totalExpenseStr.length, s.length, 0)
        s.setSpan(RelativeSizeSpan(1.2f), totalExpenseStr.length, s.length, 0)
        return s
    }

    override fun onNothingSelected() {

    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        val selectedValue = data[h?.x?.toInt()!!]
        val categoryTitle = "${context.getString(R.string.category)}:   "
        val expenseTitle = "${context.getString(R.string.expense)}:   "
        val s = "$categoryTitle${selectedValue.categoryName}\n" +
                "$expenseTitle${Utils.formatDecimalValues(selectedValue.value)} $currencySymbol  " +
                "(${Utils.formatDecimalValues(selectedValue.percentValue)}%)"

        AlertDialogUtils.show(
            context,
            "",
            s,
            context.getString(R.string.ok),
            null,
            object : BaseView.Callback {
                override fun onOkBtnPressed() {
                    chartView?.highlightValues(null)
                }

                override fun onCancelBtnPressed() {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            })
    }
}