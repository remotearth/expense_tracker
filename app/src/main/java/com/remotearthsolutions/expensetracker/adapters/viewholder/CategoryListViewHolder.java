package com.remotearthsolutions.expensetracker.adapters.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.adapters.CategoryListViewAdapter;
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel;
import com.remotearthsolutions.expensetracker.utils.CategoryIcons;

public class CategoryListViewHolder extends RecyclerView.ViewHolder {

    private TextView categoryName;
    private ImageView categoryImage;
    private CategoryModel categoryModel;

    public CategoryListViewHolder(@NonNull View view, final CategoryListViewAdapter.OnItemClickListener listener) {
        super(view);
        categoryName = view.findViewById(R.id.cat_nameview);
        categoryImage = view.findViewById(R.id.cat_iconview);
        view.setOnClickListener(v -> listener.onItemClick(categoryModel));
    }

    public void bind(CategoryModel categoryModel) {
        this.categoryModel = categoryModel;
        categoryName.setText(categoryModel.getName());
        categoryImage.setImageResource(CategoryIcons.getIconId(categoryModel.getIcon()));
    }
}
