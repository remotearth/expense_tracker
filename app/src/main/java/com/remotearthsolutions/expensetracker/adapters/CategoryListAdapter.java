package com.remotearthsolutions.expensetracker.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.adapters.viewholder.CategoryViewHolder;
import com.remotearthsolutions.expensetracker.entities.Category;

import java.util.List;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryViewHolder> {

    private List<Category> categorylist;
    private OnItemClickListener listener;

    public CategoryListAdapter(List<Category> categorylist) {
        this.categorylist = categorylist;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_catagory, parent, false);
        return new CategoryViewHolder(v,listener);
    }

    @Override
    public void onBindViewHolder(final CategoryViewHolder holder, final int position) {

        Category category = categorylist.get(position);
        holder.bind(category);
    }

    @Override
    public int getItemCount() {

        if (categorylist == null) return 0;
        return categorylist.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Category category);
    }
}
