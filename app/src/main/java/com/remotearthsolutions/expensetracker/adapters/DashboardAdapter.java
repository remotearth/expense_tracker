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
import java.util.List;

public class DashboardAdapter extends ArrayAdapter<DashboardModel> {

    private Context context;
    private List<DashboardModel> dashboardItem;

    public DashboardAdapter(Context context, List<DashboardModel> dashboardItem) {
        super(context, R.layout.custom_dashboard, dashboardItem);
        this.context = context;
        this.dashboardItem = dashboardItem;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.custom_dashboard, parent, false);
        }

        DashboardModel model = dashboardItem.get(position);
        ImageView imageView = view.findViewById(R.id.dashboardimageView);
        TextView textView = view.findViewById(R.id.dashboardtextView);

        imageView.setImageResource(model.getDashboardImage());
        textView.setText(model.getDashboardName());

        return view;
    }
}
