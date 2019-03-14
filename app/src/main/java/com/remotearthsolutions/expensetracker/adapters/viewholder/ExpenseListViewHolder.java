package com.remotearthsolutions.expensetracker.adapters.viewholder;

import android.view.View;
import android.widget.ImageView;
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
    private TextView accountNameTv;
    private ImageView categoryExpenseIcon;
    private ImageView accountImageIv;
    private CategoryExpense expense;

    public ExpenseListViewHolder(@NonNull View itemView, ExpenseListAdapter.OnItemClickListener listener) {
        super(itemView);

        categoryTextView = itemView.findViewById(R.id.categoryNameTv);
        amountTextView = itemView.findViewById(R.id.amountTv);
        categoryExpenseIcon = itemView.findViewById(R.id.categoryIMG);
        accountNameTv = itemView.findViewById(R.id.accountNameTv);
        accountImageIv = itemView.findViewById(R.id.accountImageIv);

        itemView.setOnClickListener(v -> listener.onItemClick(expense));

    }

    public void bind(CategoryExpense expense, String currencySymbol) {
        this.expense = expense;
        categoryTextView.setText(String.valueOf(expense.getCategoryName()));
        amountTextView.setText(currencySymbol + " " + String.valueOf(expense.getTotalAmount()));
        categoryExpenseIcon.setImageResource(CategoryIcons.getIconId(expense.getCategoryIcon()));
        accountNameTv.setText(expense.getAccountName());
        accountImageIv.setImageResource(CategoryIcons.getIconId(expense.getAccountIcon()));
    }

}
