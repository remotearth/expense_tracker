package com.remotearthsolutions.expensetracker.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.adapters.viewholder.IconViewHolder;
import com.remotearthsolutions.expensetracker.entities.Icon;

import java.util.List;

public class IconListAdapter extends RecyclerView.Adapter<IconViewHolder>{

    private List<Icon> iconlist;
    private IconListAdapter.OnItemClickListener listener;


    public IconListAdapter(List<Icon> iconlist) {
        this.iconlist = iconlist;
    }

    @NonNull
    @Override
    public IconViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_icon, parent, false);
        return new IconViewHolder(v,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull IconViewHolder holder, int position) {

        Icon icon = iconlist.get(position);
        holder.bind(icon,position);
    }

    @Override
    public int getItemCount() {

        if (iconlist == null) return 0;
        return iconlist.size();
    }



    public void setOnItemClickListener(IconListAdapter.OnItemClickListener listener){
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Icon icon, int position);
    }
}
