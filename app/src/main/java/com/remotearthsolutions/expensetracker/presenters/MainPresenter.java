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

    public MainPresenter(MainContract.View view) {
        this.view = view;
    }

    public void init() {
        view.initializeView();

    }

    public void checkAuthectication(FirebaseService firebaseService) {
        FirebaseUser user = firebaseService.getUser();
        if (user == null) {
            view.openLoginScreen();
        }
    }

//    public void loadChart(List<ExpeneChartData> data) {
//        chartManager.loadExpensePieChart(chartView, data);
//    }
}
