package com.remotearthsolutions.expensetracker.adapters.viewholder;

import android.view.View;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.adapters.IconListAdapter;
import com.remotearthsolutions.expensetracker.entities.Icon;

public class IconViewHolder extends RecyclerView.ViewHolder {

    private ImageView categoryIconIv;
    private Icon icon;
    private int position;

    public IconViewHolder(View view, final IconListAdapter.OnItemClickListener listener) {
        super(view);
        categoryIconIv = view.findViewById(R.id.iconviews);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(icon);
            }
        });
    }

    public void bind(Icon icon, int position) {
        this.icon = icon;
        this.position = position;
        categoryIconIv.setImageResource(icon.getIcon());
    }
}
