package com.remotearthsolutions.expensetracker.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.adapters.AccountListAdapter;
import com.remotearthsolutions.expensetracker.contracts.AccountDialogContract;
import com.remotearthsolutions.expensetracker.databaseutils.DatabaseClient;
import com.remotearthsolutions.expensetracker.databaseutils.daos.AccountDao;
import com.remotearthsolutions.expensetracker.databaseutils.models.AccountModel;
import com.remotearthsolutions.expensetracker.viewmodels.AccountDialogViewModel;
import com.remotearthsolutions.expensetracker.viewmodels.viewmodel_factory.AccountDialogViewModelFactory;

import java.util.List;

public class AccountDialogFragment extends DialogFragment implements AccountDialogContract.View {

    private AccountDialogViewModel viewModel;
    private AccountListAdapter accountListAdapter;
    private RecyclerView accountrecyclerView;
    private AccountDialogFragment.Callback callback;

    public AccountDialogFragment() {
    }

    public static AccountDialogFragment newInstance(String title) {
        AccountDialogFragment frag = new AccountDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    public void setCallback(AccountDialogFragment.Callback callback) {
        this.callback = callback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_account, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        accountrecyclerView = view.findViewById(R.id.accountrecyclearView);
        accountrecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        accountrecyclerView.setLayoutManager(llm);

        AccountDao accountDao = DatabaseClient.getInstance(getContext()).getAppDatabase().accountDao();
        this.viewModel = ViewModelProviders.of(this,
                new AccountDialogViewModelFactory(this, accountDao)).
                get(AccountDialogViewModel.class);
        this.viewModel.loadAccounts();


    }

    @Override
    public void onAccountFetchSuccess(List<AccountModel> accounts) {
        accountListAdapter = new AccountListAdapter(accounts);
        accountListAdapter.setOnItemClickListener(new AccountListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AccountModel account) {
                callback.onSelectAccount(account);
            }
        });
        accountrecyclerView.setAdapter(accountListAdapter);
    }

    @Override
    public void onAccountFetchFailure() {
        Toast.makeText(getActivity(),"Could not load data",Toast.LENGTH_SHORT).show();
    }

    public interface Callback {
        void onSelectAccount(AccountModel accountIncome);
    }
}
