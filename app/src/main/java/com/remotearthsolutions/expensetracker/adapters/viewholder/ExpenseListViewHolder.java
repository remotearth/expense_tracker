package com.remotearthsolutions.expensetracker.adapters.viewholder;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.databaseutils.models.ExpenseModel;

public class ExpenseListViewHolder extends RecyclerView.ViewHolder {

    private TextView ammountTv,ammountDateTime;
    private ExpenseModel expenseModel;

    public ExpenseListViewHolder(@NonNull View itemView) {
        super(itemView);
        ammountTv = itemView.findViewById(R.id.ammountTv);
        ammountDateTime = itemView.findViewById(R.id.ammountDateTime);
    }

    public void bind(ExpenseModel expenseModel)
    {
        this.expenseModel = expenseModel;
        ammountTv.setText(String.valueOf(expenseModel.getAmount()));
        ammountDateTime.setText(String.valueOf(expenseModel.getDatetime()));

    }
}
