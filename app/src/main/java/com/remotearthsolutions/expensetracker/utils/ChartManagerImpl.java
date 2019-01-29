package com.remotearthsolutions.expensetracker.utils;

import android.graphics.Color;
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig;
import com.razerdp.widget.animatedpieview.data.SimplePieInfo;
import com.remotearthsolutions.expensetracker.entities.ExpeneChartData;

import java.util.List;

public class ChartManagerImpl implements ChartManager {

    private AnimatedPieViewConfig config;

    public ChartManagerImpl() {
        config = new AnimatedPieViewConfig();
    }

    @Override
    public void initPierChart() {
        config.startAngle(-90)
                .canTouch(true)
                .drawText(true)
                .autoSize(true)
                .strokeWidth(40)
                .textSize(30)
                .duration(1000);
    }

    @Override
    public void loadExpensePieChart(ChartView chartView, List<ExpeneChartData> data) {
        for (ExpeneChartData item : data) {
            config.addData(new SimplePieInfo(item.getValue(), Color.parseColor(item.getColor()), item.getCategoryName()));
        }

        chartView.loadChartConfig(config);
    }

    public interface ChartView {
        void loadChartConfig(AnimatedPieViewConfig config);
    }
}
