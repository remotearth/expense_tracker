package com.remotearthsolutions.expensetracker.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.adapters.viewholder.IconViewHolder;

import java.util.List;

public class IconListAdapter extends RecyclerView.Adapter<IconViewHolder> {

    private List<String> iconlist;
    private IconListAdapter.OnItemClickListener listener;
    private String selectedIcon;
    private GridLayoutManager layoutManager;

    public IconListAdapter(List<String> iconlist, GridLayoutManager layoutManager) {
        this.iconlist = iconlist;
        this.layoutManager = layoutManager;
    }

    @NonNull
    @Override
    public IconViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_icon, parent, false);
        return new IconViewHolder(v, listener, layoutManager);
    }

    @Override
    public void onBindViewHolder(@NonNull IconViewHolder holder, int position) {

        String icon = iconlist.get(position);
        holder.bind(icon, selectedIcon.equals(icon));
    }

    @Override
    public int getItemCount() {

        if (iconlist == null) {
            return 0;
        }
        return iconlist.size();
    }

    public void setOnItemClickListener(IconListAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setSelectedIcon(String selectedIcon) {
        this.selectedIcon = selectedIcon;
        int position = iconlist.indexOf(selectedIcon);
        if (position > 0) {
            layoutManager.scrollToPosition(position);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String icon);
    }
}
