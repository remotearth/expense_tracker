package com.remotearthsolutions.expensetracker.adapters.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.adapters.ExpenseListAdapter;
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.CategoryExpense;
import com.remotearthsolutions.expensetracker.utils.CategoryIcons;

public class ExpenseListViewHolder extends RecyclerView.ViewHolder {

    private TextView categoryTextView;
    private TextView amountTextView;
    private ImageView categoryExpenseIcon;
    private CategoryExpense expense;

    public ExpenseListViewHolder(@NonNull View itemView, ExpenseListAdapter.OnItemClickListener listener) {
        super(itemView);

        categoryTextView = itemView.findViewById(R.id.categoryNameTv);
        amountTextView = itemView.findViewById(R.id.amountTv);
        categoryExpenseIcon = itemView.findViewById(R.id.categoryIMG);

        itemView.setOnClickListener(v -> listener.onItemClick(expense));

    }

    public void bind(CategoryExpense expense, String currencySymbol) {
        this.expense = expense;
        categoryTextView.setText(String.valueOf(expense.getCategory_name()));
        amountTextView.setText(currencySymbol + " " + String.valueOf(expense.getTotal_amount()));
        categoryExpenseIcon.setImageResource(CategoryIcons.getIconId(expense.getIcon_name()));
    }

}
