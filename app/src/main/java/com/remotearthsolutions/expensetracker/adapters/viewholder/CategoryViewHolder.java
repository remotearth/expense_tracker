package com.remotearthsolutions.expensetracker.adapters.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.entities.Category;

public class CategoryViewHolder extends RecyclerView.ViewHolder {

    private ImageView categoryImageIv;
    private TextView categoryNameTv;

    public CategoryViewHolder(View view) {
        super(view);
        categoryImageIv = view.findViewById(R.id.eventtitle);
        categoryNameTv = view.findViewById(R.id.eventdate);

    }

    public void bind(Category category) {
        categoryImageIv.setImageResource(category.getCategoryImage());
        categoryNameTv.setText(category.getCategoryName());
    }
}
