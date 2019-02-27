package com.remotearthsolutions.expensetracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.entities.DashboardModel;

import java.util.ArrayList;

public class DashboardAdapter extends ArrayAdapter<DashboardModel> {

    private Context context;
    private ArrayList<DashboardModel> dashboardItem;

    public DashboardAdapter(Context context, ArrayList<DashboardModel> dashboardItem)  {
        super(context, R.layout.custom_dashboard, dashboardItem);
        this.context = context;
        this.dashboardItem = dashboardItem;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.custom_dashboard,parent,false);
        ImageView imageView = convertView.findViewById(R.id.dashboardimageView);
        TextView textView = convertView.findViewById(R.id.dashboardtextView);
        imageView.setImageResource(dashboardItem.get(position).getDashboardImage());
        textView.setText(dashboardItem.get(position).getDashboardName());
        return convertView;
    }
}
