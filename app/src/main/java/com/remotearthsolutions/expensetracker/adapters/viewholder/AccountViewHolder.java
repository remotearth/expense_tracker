package com.remotearthsolutions.expensetracker.adapters.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.remotearthsolutions.expensetracker.R;

import com.remotearthsolutions.expensetracker.adapters.AccountListAdapter;
import com.remotearthsolutions.expensetracker.databaseutils.models.AccountIncome;

public class AccountViewHolder extends RecyclerView.ViewHolder {

    private ImageView accountImageIv;
    private TextView accountNameTv;
    private TextView ammountTv;
    private AccountIncome account;

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

    public void bind(AccountIncome account) {
        this.account = account;

        //accountImageIv.setImageResource(account.getIcon_name().);
        accountImageIv.setImageResource(R.drawable.ic_currency);
        accountNameTv.setText(account.getAccount_name());
        ammountTv.setText(String.valueOf(account.getTotal_amount()));



    }

}
