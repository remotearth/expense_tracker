package com.remotearthsolutions.expensetracker.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.adapters.AccountsAdapter;
import com.remotearthsolutions.expensetracker.contracts.AccountContract;
import com.remotearthsolutions.expensetracker.databaseutils.DatabaseClient;
import com.remotearthsolutions.expensetracker.databaseutils.daos.AccountDao;
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.AccountIncome;
import com.remotearthsolutions.expensetracker.viewmodels.AccountViewModel;
import com.remotearthsolutions.expensetracker.viewmodels.viewmodel_factory.AccountViewModelFactory;

import java.util.ArrayList;

public class AccountsFragment extends Fragment implements AccountContract.View {

    private AccountViewModel viewModel;
    private ListView lv;
    private AccountsAdapter adapter;

    public AccountsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_accounts, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lv = view.findViewById(R.id.accountList);
        AccountDao accountDao = DatabaseClient.getInstance(getContext()).getAppDatabase().accountDao();
        this.viewModel = ViewModelProviders.of(this,
                new AccountViewModelFactory(this, accountDao)).
                get(AccountViewModel.class);
        this.viewModel.loadAccounts();
    }

    @Override
    public void onAccountFetch(ArrayList<AccountIncome> accounts) {
        accounts = new ArrayList<>();
        adapter = new AccountsAdapter(getActivity(),accounts);
        lv.setAdapter(adapter);
    }
}
