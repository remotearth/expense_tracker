package com.remotearthsolutions.expensetracker.adapters.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.CategoryExpense;
import com.remotearthsolutions.expensetracker.utils.CategoryIcons;

public class ExpenseListViewHolder extends RecyclerView.ViewHolder {

    private TextView categoryTextView;
    private TextView amountTextView;
    private ImageView categoryExpenseIcon;

    public ExpenseListViewHolder(@NonNull View itemView) {
        super(itemView);

        categoryTextView = itemView.findViewById(R.id.categoryNameTv);
        amountTextView = itemView.findViewById(R.id.amountTv);
        categoryExpenseIcon = itemView.findViewById(R.id.categoryIMG);
    }

    public void bind(CategoryExpense expense) {

        categoryTextView.setText(String.valueOf(expense.getCategory_name()));
        amountTextView.setText(String.valueOf(expense.getTotal_amount()));
        categoryExpenseIcon.setImageResource(CategoryIcons.getIconId(expense.getIcon_name()));
    }
    
}
