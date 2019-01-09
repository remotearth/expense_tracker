package com.remotearthsolutions.expensetracker.adapters.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.entities.HomeModel;

import java.util.List;

public class Adapter_Home extends RecyclerView.Adapter<Adapter_Home.Holder> {


    private List<HomeModel> categorylist;
    private Context context;

    public Adapter_Home(List<HomeModel> categorylist, Context context) {
        this.categorylist = categorylist;
        this.context = context;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_catagory, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {


        holder.image.setImageResource(categorylist.get(position).getCategory_image());
        holder.name.setText(categorylist.get(position).getCategory_name());

    }

    @Override
    public int getItemCount() {
        return categorylist.size();
    }


    class Holder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name;


        Holder(View fview) {
            super(fview);
            image = fview.findViewById(R.id.eventtitle);
            name = fview.findViewById(R.id.eventdate);


        }


    }
}
