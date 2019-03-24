package com.remotearthsolutions.expensetracker.adapters.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.adapters.AccountListAdapter;
import com.remotearthsolutions.expensetracker.databaseutils.models.AccountModel;
import com.remotearthsolutions.expensetracker.utils.CategoryIcons;
import com.remotearthsolutions.expensetracker.utils.Utils;

public class AccountViewHolder extends RecyclerView.ViewHolder {

    private ImageView accountImageIv;
    private TextView accountNameTv;
    private TextView ammountTv;
    private AccountModel account;

    public AccountViewHolder(View view, final AccountListAdapter.OnItemClickListener listener) {
        super(view);

        accountImageIv = view.findViewById(R.id.acimage);
        accountNameTv = view.findViewById(R.id.actitle);
        ammountTv = view.findViewById(R.id.acammount);

        view.setOnClickListener(v -> listener.onItemClick(account));

    }

    public void bind(AccountModel account) {
        this.account = account;
        accountImageIv.setImageResource(CategoryIcons.getIconId(account.getIcon()));
        accountNameTv.setText(account.getName());
        ammountTv.setText(Utils.formatDecimalValues(account.getAmount()));


    }

}
