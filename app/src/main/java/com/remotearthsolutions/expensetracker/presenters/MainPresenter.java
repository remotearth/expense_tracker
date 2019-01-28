package com.remotearthsolutions.expensetracker.presenters;


import android.content.Intent;
import com.google.firebase.auth.FirebaseUser;
import com.remotearthsolutions.expensetracker.contracts.MainContract;
import com.remotearthsolutions.expensetracker.entities.User;
import com.remotearthsolutions.expensetracker.services.FirebaseService;


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

        User user1 = new User();
        Intent intent = new Intent();
        user1  = intent.getParcelableExtra("key");

        if (user == null && user1 == null)
        {
            view.openLoginScreen();
        }




    }



//    public void loadChart(List<ExpeneChartData> data) {
//        chartManager.loadExpensePieChart(chartView, data);
//    }
}
