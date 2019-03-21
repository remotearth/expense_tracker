package com.remotearthsolutions.expensetracker.utils;

import android.content.Context;
import com.remotearthsolutions.expensetracker.entities.ExpeneChartData;

import java.util.List;

public interface ChartManager {

    void initPierChart(int deviceDp, Utils.ScreenSize screenSize);

    void loadExpensePieChart(Context context, ChartManagerImpl.ChartView chartView, List<ExpeneChartData> data);

}
