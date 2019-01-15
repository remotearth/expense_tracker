package com.remotearthsolutions.expensetracker.presenters;


import com.google.firebase.auth.FirebaseUser;
import com.remotearthsolutions.expensetracker.contracts.MainContract;
import com.remotearthsolutions.expensetracker.entities.ExpeneChartData;
import com.remotearthsolutions.expensetracker.services.FirebaseService;
import com.remotearthsolutions.expensetracker.utils.ChartManager;
import com.remotearthsolutions.expensetracker.utils.ChartManagerImpl;

import java.util.List;

public class MainPresenter {

    private MainContract.View view;
    private ChartManager chartManager;
    private ChartManagerImpl.ChartView chartView;

    public MainPresenter(MainContract.View view, ChartManagerImpl.ChartView chartView, ChartManager chartManager) {
        this.view = view;
        this.chartManager = chartManager;
        this.chartView = chartView;
    }

    public void init() {
        view.initializeView();
        chartManager.initPierChart();

    }

    public void checkAuthectication(FirebaseService firebaseService) {
        FirebaseUser user = firebaseService.getUser();
        if (user == null) {
            view.openLoginScreen();
        }
    }

    public void loadChart(List<ExpeneChartData> data) {
        chartManager.loadExpensePieChart(chartView, data);
    }
}
