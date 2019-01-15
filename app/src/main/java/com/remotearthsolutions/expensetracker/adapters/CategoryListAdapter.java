package com.remotearthsolutions.expensetracker.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.adapters.viewholder.CategoryViewHolder;
import com.remotearthsolutions.expensetracker.entities.Category;

import java.util.List;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryViewHolder> {


    private List<Category> categorylist;
    private Context context;

    public CategoryListAdapter(List<Category> categorylist, Context context) {
        this.categorylist = categorylist;
        this.context = context;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_catagory, parent, false);
        return new CategoryViewHolder(v);
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

}
