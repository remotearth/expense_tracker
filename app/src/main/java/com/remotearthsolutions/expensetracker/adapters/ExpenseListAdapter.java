package com.remotearthsolutions.expensetracker.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.adapters.viewholder.ExpenseListViewHolder;
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel;
import com.remotearthsolutions.expensetracker.databaseutils.models.ExpenseModel;
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.CategoryExpense;

import java.util.List;

public class ExpenseListAdapter extends RecyclerView.Adapter<ExpenseListViewHolder> {

    private List<CategoryExpense> expenselist;

    public ExpenseListAdapter(List<CategoryExpense> expenselist) {
        this.expenselist = expenselist;
    }

    @NonNull
    @Override
    public ExpenseListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_all_expense, parent, false);
        return new ExpenseListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseListViewHolder holder, int position) {
        CategoryExpense expense = expenselist.get(position);
        holder.bind(expense);
    }

    @Override
    public int getItemCount() {
        if (expenselist == null) return 0;
        return expenselist.size();
    }
}
