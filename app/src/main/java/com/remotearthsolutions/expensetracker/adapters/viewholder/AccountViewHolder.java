package com.remotearthsolutions.expensetracker.adapters.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.remotearthsolutions.expensetracker.R;

import com.remotearthsolutions.expensetracker.entities.Accounts;

public class AccountViewHolder extends RecyclerView.ViewHolder {

    private ImageView accountImageIv;
    private TextView accountNameTv;
    private TextView ammountTv;
    private Accounts accounts;

    public AccountViewHolder(View view) {
        super(view);

        accountImageIv = view.findViewById(R.id.acimage);
        accountNameTv = view.findViewById(R.id.actitle);
        ammountTv = view.findViewById(R.id.acammount);

    }

    public void bind(Accounts accounts) {
        this.accounts = accounts;

        accountImageIv.setImageResource(accounts.getAccountImage());
        accountNameTv.setText(accounts.getAccountName());
        ammountTv.setText(String.valueOf(accounts.getAccountAmount()));

    }

}
