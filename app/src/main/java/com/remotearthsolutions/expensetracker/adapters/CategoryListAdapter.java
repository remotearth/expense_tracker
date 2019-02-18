package com.remotearthsolutions.expensetracker.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.adapters.viewholder.CategoryViewHolder;
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel;

import java.util.List;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryViewHolder> {

    private List<CategoryModel> categorylist;
    private CategoryListAdapter.OnItemClickListener listener;
    private int selectedCategoryId;

    public CategoryListAdapter(List<CategoryModel> categorylist, int selectedCategoryId) {
        this.categorylist = categorylist;
        this.selectedCategoryId = selectedCategoryId;
    }

    public CategoryListAdapter(List<CategoryModel> categorylist) {
        this.categorylist = categorylist;

    }

    public void setOnItemClickListener(CategoryListAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_catagory, parent, false);
        return new CategoryViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategoryModel category = categorylist.get(position);
        holder.bind(category, category.getId() == selectedCategoryId);
    }

    @Override
    public int getItemCount() {
        if (categorylist == null) return 0;
        return categorylist.size();
    }

    public interface OnItemClickListener {
        void onItemClick(CategoryModel category);
    }
}
