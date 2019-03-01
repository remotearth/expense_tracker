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
import com.remotearthsolutions.expensetracker.databaseutils.models.AccountModel;
import com.remotearthsolutions.expensetracker.utils.CategoryIcons;

import java.util.List;

public class AccountsAdapter extends ArrayAdapter<AccountModel> {

    private Context context;
    private List<AccountModel> accountList;

    public AccountsAdapter(Context context, List<AccountModel> accountList) {
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

        AccountModel model = accountList.get(position);
        ImageView accountImageIv = view.findViewById(R.id.acimage);
        TextView accountNameTv = view.findViewById(R.id.actitle);
        TextView ammountTv = view.findViewById(R.id.acammount);

        accountImageIv.setImageResource(CategoryIcons.getIconId(model.getIcon()));
        accountNameTv.setText(model.getName());
        ammountTv.setText(String.valueOf(model.getAmount()));

        return view;
    }
}
