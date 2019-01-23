package com.remotearthsolutions.expensetracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.adapters.viewholder.CategoryListViewHolder;
import com.remotearthsolutions.expensetracker.databaseutils.CategoryModel;


import java.util.List;

public class CategoryListViewAdapter extends RecyclerView.Adapter<CategoryListViewHolder> {

    private List<CategoryModel> categorylist;
    private CategoryListViewAdapter.OnItemClickListener listener;
    private Context context;

    public CategoryListViewAdapter(List<CategoryModel> categorylist, Context context) {
        this.categorylist = categorylist;
        this.context = context;
    }

    @NonNull
    @Override
    public CategoryListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_category_view, parent, false);
        return new CategoryListViewHolder(v,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryListViewHolder holder, int position) {

        CategoryModel categoryModel = categorylist.get(position);
        holder.bind(categoryModel,position);
    }

    @Override
    public int getItemCount() {

        if (categorylist == null) return 0;
        return categorylist.size();
    }

    public void setOnItemClickListener(CategoryListViewAdapter.OnItemClickListener listener){
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(CategoryModel categoryModel, int position);
    }
}
