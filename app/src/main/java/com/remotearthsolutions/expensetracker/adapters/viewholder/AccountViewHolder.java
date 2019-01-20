package com.remotearthsolutions.expensetracker.adapters.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.remotearthsolutions.expensetracker.R;

import com.remotearthsolutions.expensetracker.adapters.AccountListAdapter;
import com.remotearthsolutions.expensetracker.entities.Account;

public class AccountViewHolder extends RecyclerView.ViewHolder {

    private ImageView accountImageIv;
    private TextView accountNameTv;
    private TextView ammountTv;
    private Account account;

    public AccountViewHolder(View view, final AccountListAdapter.OnItemClickListener listener) {
        super(view);

        accountImageIv = view.findViewById(R.id.acimage);
        accountNameTv = view.findViewById(R.id.actitle);
        ammountTv = view.findViewById(R.id.acammount);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(account);
            }
        });

    }

    public void bind(Account account) {
        this.account = account;

        accountImageIv.setImageResource(account.getAccountImage());
        accountNameTv.setText(account.getAccountName());
        ammountTv.setText(String.valueOf(account.getAccountAmount()));



    }

}
