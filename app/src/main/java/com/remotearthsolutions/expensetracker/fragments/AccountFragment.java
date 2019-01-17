package com.remotearthsolutions.expensetracker.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.adapters.AccountListAdapter;
import com.remotearthsolutions.expensetracker.entities.Accounts;

import java.util.ArrayList;
import java.util.List;

public class AccountFragment extends DialogFragment {

    public AccountFragment() {
    }

    private View view;
    //private Dialog accountbaseddialog;
    private AccountListAdapter accountListAdapter;
    private List<Accounts> accountslist;
    private RecyclerView accountrecyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.add_account, container, false);


//        accountbaseddialog = new Dialog(getActivity());
//        accountbaseddialog.setContentView(R.layout.add_account);
        accountrecyclerView = view.findViewById(R.id.accountrecyclearView);
        accountrecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        accountrecyclerView.setLayoutManager(llm);

        loadAccountlIST();
        accountListAdapter = new AccountListAdapter(accountslist, getActivity());
        accountrecyclerView.setAdapter(accountListAdapter);
//        accountbaseddialog.show();


        return view;
    }


    public void loadAccountlIST() {
        accountslist = new ArrayList<>();
        accountslist.add(new Accounts(R.drawable.ic_currency, "CASH", 1000.00));
        accountslist.add(new Accounts(R.drawable.ic_currency, "BANK", 2000.00));
        accountslist.add(new Accounts(R.drawable.ic_currency, "LOAN", 3000.00));

    }
}
