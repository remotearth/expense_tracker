package com.remotearthsolutions.expensetracker.adapters.viewholder;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.adapters.IconListAdapter;
import com.remotearthsolutions.expensetracker.utils.CategoryIcons;

public class IconViewHolder extends RecyclerView.ViewHolder {

    private LinearLayout container;
    private ImageView categoryIconIv;
    private String icon;

    public IconViewHolder(View view, final IconListAdapter.OnItemClickListener listener, GridLayoutManager layoutManager) {
        super(view);
        container = view.findViewById(R.id.container);
        categoryIconIv = view.findViewById(R.id.iconIv);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(icon);
            }
        });
    }

    public void bind(String icon, boolean isSelected) {
        this.icon = icon;
        categoryIconIv.setImageResource(CategoryIcons.getIconId(icon));
        if (isSelected) {
            container.setBackgroundColor(Color.parseColor("#C0C0C0"));
        } else {
            container.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
    }
}
