package com.remotearthsolutions.expensetracker.adapters.viewholder;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.adapters.CategoryListAdapter;
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel;
import com.remotearthsolutions.expensetracker.utils.CategoryIcons;

public class CategoryViewHolder extends RecyclerView.ViewHolder {

    private LinearLayout container;
    private ImageView categoryImageIv;
    private TextView categoryNameTv;
    private CategoryModel category;

    public CategoryViewHolder(@NonNull View view, final CategoryListAdapter.OnItemClickListener listener) {
        super(view);
        categoryImageIv = view.findViewById(R.id.eventtitle);
        categoryNameTv = view.findViewById(R.id.eventdate);
        container = view.findViewById(R.id.container);

        view.setOnClickListener(v -> listener.onItemClick(category));

    }

    public void bind(CategoryModel category, boolean isSelected) {
        this.category = category;
//        categoryImageIv.setImageResource(CategoryIcons.getIconId(category.getIcon()));
        categoryNameTv.setText(category.getName());

        if (isSelected) {
            container.setBackgroundColor(Color.parseColor("#C0C0C0"));
        } else {
            container.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
    }


}
