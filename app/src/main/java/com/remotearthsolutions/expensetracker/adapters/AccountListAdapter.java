package com.remotearthsolutions.expensetracker.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.adapters.viewholder.AccountViewHolder;
import com.remotearthsolutions.expensetracker.entities.Accounts;

import java.util.List;

public class AccountListAdapter extends RecyclerView.Adapter<AccountViewHolder> {

    private List<Accounts> accountlist;
    private Context context;

    public AccountListAdapter(List<Accounts> accountlist, Context context) {
        this.accountlist = accountlist;
        this.context = context;
    }

    @Override
    public AccountViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_account, parent, false);
        return new AccountViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AccountViewHolder holder, int position) {

        Accounts accounts = accountlist.get(position);
        holder.bind(accounts);
    }

    @Override
    public int getItemCount() {

        if (accountlist == null) return 0;
        return accountlist.size();
    }
}
