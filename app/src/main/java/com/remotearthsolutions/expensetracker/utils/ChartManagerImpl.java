package com.remotearthsolutions.expensetracker.utils;

import android.content.Context;
import android.content.res.Resources;
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig;
import com.razerdp.widget.animatedpieview.data.SimplePieInfo;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.entities.ExpeneChartData;

import java.text.DecimalFormat;
import java.util.List;

public class ChartManagerImpl implements ChartManager {

    private int[] colors = new int[]{R.color.color1, R.color.color8, R.color.color3, R.color.color4, R.color.color14, R.color.color26,
            R.color.color28, R.color.color29, R.color.color5, R.color.color9, R.color.color23, R.color.color21, R.color.color2, R.color.color18,
            R.color.color11, R.color.color10, R.color.color22, R.color.color27, R.color.color17, R.color.color30, R.color.color37, R.color.color13,
            R.color.color31, R.color.color19, R.color.color32, R.color.color24};

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
    public void loadExpensePieChart(Context context, ChartView chartView, List<ExpeneChartData> data) {
        double sum = 0;
        for (ExpeneChartData item : data) {
            sum += item.getValue();
        }


        int colorPosition = 0;
        Resources resources = context.getResources();
        for (ExpeneChartData item : data) {
            double val = (item.getValue()) * 100 / sum;
            String catName = item.getCategoryName();
            if (catName.length() > 9) {
                catName = catName.substring(0, 7) + "..";
            }
            config.addData(new SimplePieInfo(item.getValue(), resources.getColor(colors[colorPosition]),
                    catName + "\n(" + df.format(val) + "%)"));

            colorPosition++;
            if (colorPosition > 25) {
                colorPosition = 0;
            }
        }
        chartView.loadChartConfig(config);
    }

    public interface ChartView {
        void loadChartConfig(AnimatedPieViewConfig config);
    }
}
