package com.remotearthsolutions.expensetracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.intrusoft.sectionedrecyclerview.SectionRecyclerViewAdapter;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.adapters.viewholder.DateSectionedViewHolder;
import com.remotearthsolutions.expensetracker.adapters.viewholder.ExpenseListViewHolder;
import com.remotearthsolutions.expensetracker.databaseutils.models.DateModel;
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.CategoryExpense;

import java.util.List;

public class ExpenseListAdapter extends SectionRecyclerViewAdapter<DateModel, CategoryExpense, DateSectionedViewHolder, ExpenseListViewHolder> {
    private Context context;

    public ExpenseListAdapter(Context context, List<DateModel> sectionItemList) {
        super(context, sectionItemList);
        this.context = context;
    }

    @Override
    public DateSectionedViewHolder onCreateSectionViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sectioned_date, viewGroup, false);
        return new DateSectionedViewHolder(view);
    }

    @Override
    public ExpenseListViewHolder onCreateChildViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_all_expense, viewGroup, false);
        return new ExpenseListViewHolder(view);
    }

    @Override
    public void onBindSectionViewHolder(DateSectionedViewHolder dateSectionedViewHolder, int i, DateModel dateModel) {
        dateSectionedViewHolder.bind(dateModel);
    }

    @Override
    public void onBindChildViewHolder(ExpenseListViewHolder expenseListViewHolder, int i, int i1, CategoryExpense expenseModel) {
        expenseListViewHolder.bind(expenseModel);
    }

}
