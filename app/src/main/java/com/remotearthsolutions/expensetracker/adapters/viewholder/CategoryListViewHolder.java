package com.remotearthsolutions.expensetracker.adapters.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.adapters.CategoryListViewAdapter;
import com.remotearthsolutions.expensetracker.databaseutils.CategoryModel;

public class CategoryListViewHolder extends RecyclerView.ViewHolder {

    private TextView categoryName;
    private int position;
    private CategoryModel categoryModel;

    public CategoryListViewHolder(@NonNull View view, final CategoryListViewAdapter.OnItemClickListener listener){
        super(view);
        categoryName = view.findViewById(R.id.cat_nameview);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onItemClick(categoryModel);
            }
        });
    }

    public void bind(CategoryModel categoryModel, int position)
    {
        this.categoryModel = categoryModel;
        this.position = position;
        categoryName.setText(categoryModel.getName());

    }
}
