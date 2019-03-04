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

    private static final int HEADER_NULL = 0;
    private static final int HEADER_ENABLE = 1;
    private static final int HEADER_DISABLE = 2;

    private List<CategoryExpense> categoryExpenseList;
    private int clickedBtnId;

    public ExpenseListAdapter(List<CategoryExpense> categoryExpenseList, int clickedBtnId) {
        this.categoryExpenseList = categoryExpenseList;
        this.clickedBtnId = clickedBtnId;
    }

    @Override
    public int getItemViewType(int position) {
        CategoryExpense item = categoryExpenseList.get(position);
        if (clickedBtnId != R.id.yearlyRangeBtn && item.isHeader) {
            if (item.getCategory_name().contains("-")) {
                return HEADER_ENABLE;
            } else {
                return HEADER_NULL;
            }
        } else {

            if (item.isHeader) {
                return HEADER_ENABLE;
            } else {
                return HEADER_DISABLE;
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == HEADER_ENABLE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sectioned_date, parent, false);
            DateSectionedViewHolder dateSectionedViewHolder = new DateSectionedViewHolder(view, false);
            return dateSectionedViewHolder;
        } else if (viewType == HEADER_DISABLE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_all_expense, parent, false);
            return new ExpenseListViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sectioned_date, parent, false);
            DateSectionedViewHolder dateSectionedViewHolder = new DateSectionedViewHolder(view, true);
            return dateSectionedViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CategoryExpense item = categoryExpenseList.get(position);

        if (holder instanceof DateSectionedViewHolder) {
            DateSectionedViewHolder viewHolder = (DateSectionedViewHolder) holder;
            viewHolder.bind(new DateModel(item.getCategory_name()));
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
