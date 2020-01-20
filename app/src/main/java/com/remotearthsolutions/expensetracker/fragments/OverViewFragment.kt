package com.remotearthsolutions.expensetracker.fragments

import android.content.Context
import android.graphics.RectF
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.databaseutils.DatabaseClient
import com.remotearthsolutions.expensetracker.utils.*
import com.remotearthsolutions.expensetracker.viewmodels.AllTransactionsViewModel
import com.remotearthsolutions.expensetracker.viewmodels.viewmodel_factory.BaseViewModelFactory
import com.remotearthsolutions.expensetracker.views.XYMarkerView
import kotlinx.android.synthetic.main.fragment_overview.*
import java.util.*
import kotlin.collections.ArrayList


class OverViewFragment : BaseFragment(), OnChartValueSelectedListener {
    private lateinit var mView: View
    private lateinit var viewModel: AllTransactionsViewModel

    private var mContext: Context? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_overview, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val height = Utils.getDeviceScreenSize(requireActivity())?.height?.div(4)
        val param = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height!!)
        barChart.layoutParams = param

        val db = DatabaseClient.getInstance(mContext!!)?.appDatabase

        viewModel =
            ViewModelProviders.of(requireActivity(), BaseViewModelFactory {
                AllTransactionsViewModel(
                    null, db?.expenseDao()!!, db.categoryExpenseDao(),
                    SharedPreferenceUtils.getInstance(mContext!!)!!
                        .getString(
                            Constants.PREF_TIME_FORMAT,
                            resources.getString(R.string.default_time_format)
                        )
                )
            }).get(AllTransactionsViewModel::class.java)

        setupChart()



        viewModel.chartDataRequirementLiveData.observe(this, Observer {
            barChart.clear()
            val currencySymbol = Utils.getCurrency(requireContext())

            when (it.selectedFilterBtnId) {
                R.id.dailyRangeBtn -> {
                    val totalExpense = generateGraph(it, "MMM dd", Calendar.DAY_OF_MONTH)
                    val avg = Utils.formatDecimalValues(totalExpense)
                    totalCountView.setInfo(it.filteredList.size.toString())
                    dailyAvgView.setInfo("$avg $currencySymbol")
                }
                R.id.weeklyRangeBtn -> {
                    val totalExpense = generateGraph(it, "MMM dd", Calendar.DAY_OF_MONTH)
                    val avg = Utils.formatDecimalValues(totalExpense / 7.0)
                    totalCountView.setInfo(it.filteredList.size.toString())
                    dailyAvgView.setInfo("$avg $currencySymbol")
                }
                R.id.monthlyRangeBtn -> {
                    val totalExpense = generateGraph(it, "MMM dd", Calendar.DAY_OF_MONTH)
                    val currentTime = DateTimeUtils.getCurrentTimeInMills()
                    val noOfDays = if (currentTime < it.endTime) {
                        DateTimeUtils.daysBetween(it.startTime, currentTime)
                    } else {
                        DateTimeUtils.daysBetween(it.startTime, it.endTime)
                    }

                    val avg = Utils.formatDecimalValues(totalExpense / (noOfDays + 1))
                    totalCountView.setInfo(it.filteredList.size.toString())
                    dailyAvgView.setInfo("$avg $currencySymbol")
                }
                R.id.yearlyRangeBtn -> {
                    val totalExpense = generateGraph(it, "MMM", Calendar.MONTH)
                    val currentTime = DateTimeUtils.getCurrentTimeInMills()
                    val noOfDays = if (currentTime < it.endTime) {
                        DateTimeUtils.daysBetween(it.startTime, currentTime)
                    } else {
                        DateTimeUtils.daysBetween(it.startTime, it.endTime)
                    }

                    val avg = Utils.formatDecimalValues(totalExpense / (noOfDays + 1))
                    totalCountView.setInfo(it.filteredList.size.toString())
                    dailyAvgView.setInfo("$avg $currencySymbol")
                }
            }


        })

    }

    private fun generateGraph(
        it: AllTransactionsViewModel.ChartDataRequirement,
        dateFormat: String,
        calendarValueType: Int
    ): Double {
        val endTime = DateTimeUtils.getDate(it.endTime, dateFormat)

        val cal = Calendar.getInstance()
        cal.timeInMillis = it.startTime

        val xVals = ArrayList<String>()
        val barEntry = ArrayList<BarEntry>()
        var i = 0

        //add previous date of the starting day to show in the graph
        cal.add(calendarValueType, -1)
        var date = DateTimeUtils.getDate(cal.timeInMillis, dateFormat)
        xVals.add(date)
        barEntry.add(BarEntry(i.toFloat(), 0f))
        i++
        //.......

        cal.timeInMillis = it.startTime
        while (true) {
            var date = DateTimeUtils.getDate(cal.timeInMillis, dateFormat)

            xVals.add(date)
            barEntry.add(BarEntry(i.toFloat(), 0f))
            i++
            if (date == endTime)
                break
            cal.add(calendarValueType, 1)
        }

        val xAxisFormatter: ValueFormatter = DayAxisValueFormatter(xVals)
        barChart.xAxis.valueFormatter = xAxisFormatter

        val mv = XYMarkerView(requireContext(), xAxisFormatter)
        mv.chartView = barChart // For bounds control
        barChart.marker = mv // Set the marker to the chart

        var sum = 0.0
        it.filteredList.forEach { exp ->
            sum += exp.totalAmount
            var date = DateTimeUtils.getDate(exp.datetime, dateFormat)
            val pos = xVals.indexOf(date)
            if (pos > 0) {
                val entry = barEntry[pos]
                entry.y += exp.totalAmount.toFloat()
            }
        }

        val barDataset = BarDataSet(barEntry, "")
        barDataset.setDrawValues(false)

        val data = BarData(barDataset)
        barDataset.setColors(*ColorTemplate.COLORFUL_COLORS)
        barChart.data = data

        return sum
    }

    private fun setupChart() {
        barChart.description.isEnabled = false
        barChart.setPinchZoom(true)
        barChart.setDrawBarShadow(false)
        barChart.setDrawGridBackground(false)
        barChart.setOnChartValueSelectedListener(this)

        barChart.setDrawValueAboveBar(true)

        val xAxis = barChart.xAxis
        xAxis.position = XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f // only intervals of 1 day
        xAxis.labelCount = 7

        barChart.axisLeft.setDrawGridLines(false)

        barChart.animateY(500)
        barChart.legend.isEnabled = false
        barChart.setFitBars(true)


    }

    override fun onNothingSelected() {

    }

    private val onValueSelectedRectF = RectF()
    override fun onValueSelected(e: Entry?, h: Highlight?) {
        if (e == null || e.y == 0.0f) return

        val bounds = onValueSelectedRectF
        barChart.getBarBounds(e as BarEntry?, bounds)
        val position: MPPointF = barChart.getPosition(e, AxisDependency.LEFT)

//        Log.i("bounds", bounds.toString())
//        Log.i("position", position.toString())
//
//        Log.i(
//            "x-index",
//            "low: " + chart.getLowestVisibleX() + ", high: "
//                    + chart.getHighestVisibleX()
//        )

        MPPointF.recycleInstance(position)
    }
}