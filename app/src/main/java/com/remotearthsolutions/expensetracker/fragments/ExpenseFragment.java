package com.remotearthsolutions.expensetracker.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.remotearthsolutions.expensetracker.R;

public class ExpenseFragment extends Fragment {


    public ExpenseFragment() {

    }

    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.add_expense, container, false);


        return v;
    }
}
