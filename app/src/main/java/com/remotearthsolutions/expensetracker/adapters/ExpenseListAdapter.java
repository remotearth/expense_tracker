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

    private final int VIEW_ITEM = 1;
    private final int VIEW_SECTION = 0;

    private List<CategoryExpense> expenseList;
    private List<DateModel> dateModelList;

    public ExpenseListAdapter(List<CategoryExpense> expenseList, List<DateModel> dateModelList) {
        this.expenseList = expenseList;
        this.dateModelList = dateModelList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;

        if (viewType == VIEW_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_all_expense, parent, false);
            viewHolder = new ExpenseListViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sectioned_date, parent, false);
            viewHolder = new DateSectionedViewHolder(view);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ExpenseListViewHolder) {
            CategoryExpense expense = expenseList.get(position);
            ((ExpenseListViewHolder) holder).bind(expense);
        } else {
            DateModel dateModel = dateModelList.get(position);
            ((DateSectionedViewHolder)holder).bind(dateModel);
        }
    }

    @Override
    public int getItemCount() {
        if (expenseList == null) return 0;
        return expenseList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return expenseList.get(position).isHeader? VIEW_SECTION : VIEW_ITEM;
    }
}
