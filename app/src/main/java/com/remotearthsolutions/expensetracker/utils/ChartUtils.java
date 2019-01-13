package com.remotearthsolutions.expensetracker.utils;

import android.graphics.Color;
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig;
import com.razerdp.widget.animatedpieview.data.SimplePieInfo;

public class ChartUtils implements ChartManager {


    @Override
    public AnimatedPieViewConfig getPieChart() {

        AnimatedPieViewConfig config = new AnimatedPieViewConfig();
        config.startAngle(-90)// Starting angle offset
                .addData(new SimplePieInfo(17.2f, Color.parseColor("#00aaee"), "Food"))
                .addData(new SimplePieInfo(18.0f, Color.parseColor("#000000"), "Gift"))
                .addData(new SimplePieInfo(11.0f, Color.parseColor("#FF008577"), "Bills"))
                .addData(new SimplePieInfo(15.0f, Color.parseColor("#D81B60"), "Taxi"))
                .canTouch(true)
                .drawText(true)
                .autoSize(true)
                .strokeWidth(40)
                .textSize(30)
                .duration(1000);

        return config;

    }


}
