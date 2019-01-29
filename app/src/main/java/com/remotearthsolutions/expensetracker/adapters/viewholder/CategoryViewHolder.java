package com.remotearthsolutions.expensetracker.adapters.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.adapters.CategoryListAdapter;
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel;

public class CategoryViewHolder extends RecyclerView.ViewHolder {

    private ImageView categoryImageIv;
    private TextView categoryNameTv;
    private CategoryModel category;

    public CategoryViewHolder(@NonNull View view, final CategoryListAdapter.OnItemClickListener listener) {
        super(view);
        categoryImageIv = view.findViewById(R.id.eventtitle);
        categoryNameTv = view.findViewById(R.id.eventdate);

        view.setOnClickListener(v -> listener.onItemClick(category));

    }

    public void bind(CategoryModel category) {
        this.category = category;
        //categoryImageIv.setImageResource(category.getIcon());
        categoryImageIv.setImageResource(R.drawable.ic_bills);
        categoryNameTv.setText(category.getName());
    }


}
