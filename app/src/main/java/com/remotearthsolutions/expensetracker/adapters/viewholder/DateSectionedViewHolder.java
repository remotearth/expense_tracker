package com.remotearthsolutions.expensetracker.adapters.viewholder;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.databaseutils.models.DateModel;

public class DateSectionedViewHolder extends RecyclerView.ViewHolder {

    private TextView dateTextView;

    public DateSectionedViewHolder(@NonNull View itemView, boolean monthViewHidden) {
        super(itemView);

        dateTextView = itemView.findViewById(R.id.date_section);

        if (monthViewHidden) {
            itemView.setVisibility(View.GONE);
        } else {
            itemView.setVisibility(View.VISIBLE);
        }

    }

    public void bind(DateModel dateModel) {
        if (dateTextView != null) {
            dateTextView.setText(dateModel.getDate());
        }
    }
}
