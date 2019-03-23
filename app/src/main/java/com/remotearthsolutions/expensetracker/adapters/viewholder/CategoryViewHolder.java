package com.remotearthsolutions.expensetracker.adapters.viewholder;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.adapters.CategoryListAdapter;
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel;
import com.remotearthsolutions.expensetracker.utils.CategoryIcons;
import com.remotearthsolutions.expensetracker.utils.Utils;

public class CategoryViewHolder extends RecyclerView.ViewHolder {

    private RelativeLayout container;
    private ImageView categoryImageIv;
    private TextView categoryNameTv;
    private CategoryModel category;
    private Utils.ScreenSize screenSize;


    public CategoryViewHolder(@NonNull View view, final CategoryListAdapter.OnItemClickListener listener, Utils.ScreenSize screenSize) {
        super(view);
        categoryImageIv = view.findViewById(R.id.eventtitle);
        categoryNameTv = view.findViewById(R.id.eventdate);
        container = view.findViewById(R.id.container);
        this.screenSize = screenSize;

        view.setOnClickListener(v -> listener.onItemClick(category));


    }

    public void bind(CategoryModel category, boolean isSelected) {
        this.category = category;
        categoryImageIv.setImageResource(CategoryIcons.getIconId(category.getIcon()));
        categoryNameTv.setText(category.getName());

        if (isSelected) {
            container.setBackgroundColor(Color.parseColor("#C0C0C0"));
        } else {
            container.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

        if (screenSize != null) {
            categoryImageIv.getLayoutParams().height = screenSize.width / 7;
            categoryImageIv.getLayoutParams().width = screenSize.width / 7;
            container.getLayoutParams().height = screenSize.height / 7;
        }
    }


}
