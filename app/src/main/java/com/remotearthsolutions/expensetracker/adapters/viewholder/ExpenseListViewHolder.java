package com.remotearthsolutions.expensetracker.adapters.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.databaseutils.models.ExpenseModel;
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.CategoryExpense;

public class ExpenseListViewHolder extends RecyclerView.ViewHolder {

    private TextView ammountCategoryTv,ammountTv;
    private CategoryExpense expense;
    private ImageView categoryExpenseIcon;


    public ExpenseListViewHolder(@NonNull View itemView) {
        super(itemView);
        ammountCategoryTv = itemView.findViewById(R.id.categorynameTv);
        ammountTv = itemView.findViewById(R.id.ammountTv);
        categoryExpenseIcon = itemView.findViewById(R.id.categoryIMG);

    }

    public void bind(CategoryExpense expense)
    {
        this.expense = expense;
        ammountCategoryTv.setText(String.valueOf(expense.getCategory_name()));
        ammountTv.setText(String.valueOf(expense.getTotal_amount()));
        categoryExpenseIcon.setImageResource(R.drawable.ic_currency);

    }
    
}
