package com.remotearthsolutions.expensetracker.adapters.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.adapters.CategoryListAdapter;
import com.remotearthsolutions.expensetracker.entities.Category;

public class CategoryViewHolder extends RecyclerView.ViewHolder {

    private ImageView categoryImageIv;
    private TextView categoryNameTv;
    private Category category;

    public CategoryViewHolder(View view, final CategoryListAdapter.OnItemClickListener listener) {
        super(view);
        categoryImageIv = view.findViewById(R.id.eventtitle);
        categoryNameTv = view.findViewById(R.id.eventdate);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(category);
            }
        });

    }

    public void bind(Category category) {
        this.category = category;
        categoryImageIv.setImageResource(category.getCategoryImage());
        categoryNameTv.setText(category.getCategoryName());
    }

}
