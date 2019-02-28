package com.remotearthsolutions.expensetracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.AccountIncome;

import java.util.ArrayList;

public class AccountsAdapter extends ArrayAdapter<AccountIncome> {

    private Context context;
    private ArrayList<AccountIncome> accountList;

    public AccountsAdapter(Context context, ArrayList<AccountIncome> accountList) {
        super(context, R.layout.custom_account, accountList);
        this.context = context;
        this.accountList = accountList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.custom_account, parent, false);
        }

        AccountIncome model = accountList.get(position);
        ImageView accountImageIv = view.findViewById(R.id.acimage);
        TextView accountNameTv = view.findViewById(R.id.actitle);
        TextView ammountTv = view.findViewById(R.id.acammount);

        accountImageIv.setImageResource(R.drawable.ic_currency);
        accountNameTv.setText(model.getAccount_name());
        ammountTv.setText(String.valueOf(model.getTotal_amount()));

        return view;
    }
}
