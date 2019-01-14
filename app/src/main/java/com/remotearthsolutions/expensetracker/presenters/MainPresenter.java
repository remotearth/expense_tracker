package com.remotearthsolutions.expensetracker.presenters;


import com.razerdp.widget.animatedpieview.AnimatedPieView;
import com.remotearthsolutions.expensetracker.contracts.MainContract;
import com.remotearthsolutions.expensetracker.utils.ChartUtils;

public class MainPresenter{

    private MainContract.View view;

    public void initChart(AnimatedPieView mAnimatedPieView)
    {
        view.initializeView();
        ChartUtils chartUtils = new ChartUtils();
        mAnimatedPieView.applyConfig(chartUtils.getPieChart()).start();

    }


}
