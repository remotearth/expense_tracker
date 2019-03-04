package com.remotearthsolutions.expensetracker.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.adapters.viewholder.DateSectionedViewHolder;
import com.remotearthsolutions.expensetracker.adapters.viewholder.ExpenseListViewHolder;
import com.remotearthsolutions.expensetracker.databaseutils.models.DateModel;
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.CategoryExpense;

import java.util.List;

public class ExpenseListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int HEADER_ENABLE = 1;
    private static final int HEADER_DISABLE = 2;

    private List<CategoryExpense> categoryExpenseList;

    public ExpenseListAdapter(List<CategoryExpense> categoryExpenseList) {
        this.categoryExpenseList = categoryExpenseList;
    }

    @Override
    public int getItemViewType(int position) {
        CategoryExpense item = categoryExpenseList.get(position);

        if (item.isHeader) {
            return HEADER_ENABLE;
        } else {
            return HEADER_DISABLE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == HEADER_ENABLE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sectioned_date, parent, false);
            return new DateSectionedViewHolder(view);
        } else  {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_all_expense, parent, false);
            return new ExpenseListViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CategoryExpense item = categoryExpenseList.get(position);

        if (holder instanceof DateSectionedViewHolder) {
            DateSectionedViewHolder viewHolder = (DateSectionedViewHolder) holder;
            viewHolder.bind(new DateModel(item.getDatetime()));
        } else {
            ExpenseListViewHolder viewHolder = (ExpenseListViewHolder) holder;
            viewHolder.bind(item);
        }

    }

    @Override
    public int getItemCount() {
        return categoryExpenseList == null ? 0 : categoryExpenseList.size();
    }

}
