package com.remotearthsolutions.expensetracker.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.adapters.viewholder.AccountViewHolder;
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.AccountIncome;

import java.util.List;

public class AccountListAdapter extends RecyclerView.Adapter<AccountViewHolder> {

    private List<AccountIncome> accountlist;
    private AccountListAdapter.OnItemClickListener listener;

    public AccountListAdapter(List<AccountIncome> accountlist) {
        this.accountlist = accountlist;
    }

    @Override
    public AccountViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_account, parent, false);
        return new AccountViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(AccountViewHolder holder, int position) {
        AccountIncome account = accountlist.get(position);
        holder.bind(account);
    }

    @Override
    public int getItemCount() {

        if (accountlist == null) return 0;
        return accountlist.size();
    }

    public void setOnItemClickListener(AccountListAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(AccountIncome account);
    }
}
