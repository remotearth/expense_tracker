package com.remotearthsolutions.expensetracker.utils;

import android.graphics.Color;
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig;
import com.razerdp.widget.animatedpieview.data.SimplePieInfo;
import com.remotearthsolutions.expensetracker.entities.ExpeneChartData;

import java.text.DecimalFormat;
import java.util.List;

public class ChartManagerImpl implements ChartManager {

    private AnimatedPieViewConfig config;
    private DecimalFormat df = new DecimalFormat(".#");

    public ChartManagerImpl() {
        config = new AnimatedPieViewConfig();
    }

    @Override
    public void initPierChart(int deviceDp, Utils.ScreenSize screenSize) {

        int defaultDpi = 320;
        int defaultStrokeWidth = 32;
        int defaultTextSize = 25;
        int defaultPieRadius = 120;

        int strokeWidth = (defaultStrokeWidth * deviceDp) / defaultDpi;
        int textSize = (defaultTextSize * deviceDp) / defaultDpi;
        int pieRadius = (defaultPieRadius * deviceDp) / defaultDpi;

        if (screenSize != null) {
            pieRadius = screenSize.width / 5;
        }

        config.startAngle(-90)
                .canTouch(true)
                .drawText(true)
                .autoSize(true)
                .pieRadius(pieRadius)
                .strokeWidth(strokeWidth)
                .textSize(textSize)
                .duration(500);
    }

    @Override
    public void loadExpensePieChart(ChartView chartView, List<ExpeneChartData> data) {
        double sum = 0;
        for (ExpeneChartData item : data) {
            sum += item.getValue();
        }

        for (ExpeneChartData item : data) {
            double val = (item.getValue()) * 100 / sum;
            String catName = item.getCategoryName();
            if (catName.length() > 9) {
                catName = catName.substring(0, 7) + "..";
            }
            config.addData(new SimplePieInfo(item.getValue(), Color.parseColor(item.getColor()), catName + "\n(" + df.format(val) + "%)"));
        }
        chartView.loadChartConfig(config);
    }

    public interface ChartView {
        void loadChartConfig(AnimatedPieViewConfig config);
    }
}
