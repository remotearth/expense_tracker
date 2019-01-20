package com.remotearthsolutions.expensetracker.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.adapters.AccountListAdapter;
import com.remotearthsolutions.expensetracker.entities.Account;

import java.util.ArrayList;
import java.util.List;

public class AccountDialogFragment extends DialogFragment {

    public AccountDialogFragment() {
    }

    private AccountListAdapter accountListAdapter;
    private List<Account> accountslist;
    private RecyclerView accountrecyclerView;
    private AccountDialogFragment.Callback callback;

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
        return inflater.inflate(R.layout.add_account, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        accountrecyclerView = view.findViewById(R.id.accountrecyclearView);
        accountrecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        accountrecyclerView.setLayoutManager(llm);

        loadAccountlIST();
        accountListAdapter = new AccountListAdapter(accountslist);
        accountListAdapter.setOnItemClickListener(new AccountListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Account account) {
                callback.onSelectAccount(account);
            }
        });
        accountrecyclerView.setAdapter(accountListAdapter);
    }

    public void loadAccountlIST() {
        accountslist = new ArrayList<>();
        accountslist.add(new Account(R.drawable.ic_currency, "CASH", 1000.00));
        accountslist.add(new Account(R.drawable.ic_currency, "BANK", 2000.00));
        accountslist.add(new Account(R.drawable.ic_currency, "LOAN", 3000.00));

    }

    public interface Callback {
        void onSelectAccount(Account category);
    }
}
