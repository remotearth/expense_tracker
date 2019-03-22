package com.remotearthsolutions.expensetracker.adapters.viewholder;

import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.databaseutils.models.DateModel;
import com.remotearthsolutions.expensetracker.utils.Constants;

public class DateSectionedViewHolder extends RecyclerView.ViewHolder {

    private static final float TEXT_SIZE = 15f;
    private TextView dateTextView;
    private View underLine;

    public DateSectionedViewHolder(@NonNull View itemView) {
        super(itemView);
        dateTextView = itemView.findViewById(R.id.date_section);
        underLine = itemView.findViewById(R.id.underLine);
    }

    public void bind(DateModel dateModel) {

        if(dateModel.getDate().contains(Constants.KEY_DESH)){
            dateTextView.setGravity(Gravity.LEFT);
            dateTextView.setTextSize(TEXT_SIZE);
            underLine.setVisibility(View.VISIBLE);
        }

        dateTextView.setText(dateModel.getDate());

    }
}
