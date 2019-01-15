package com.remotearthsolutions.expensetracker.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.remotearthsolutions.expensetracker.R;
import com.wunderlist.slidinglayer.SlidingLayer;
import com.wunderlist.slidinglayer.transformer.SlideJoyTransformer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ExpenseFragment extends Fragment implements View.OnClickListener {

    public ExpenseFragment() {
    }

    private Handler handler;
    private ImageView calenderTask;
    private Dialog dialog;
    private LinearLayout previousdate, currentdate, selectdate;
    private TextView datestatus, dialogyesterday, dialogtoday;
    private SlidingLayer mSlidingLayer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.add_expense, container, false);
        calenderTask = v.findViewById(R.id.selectdata);

        mSlidingLayer = v.findViewById(R.id.slidingDrawer);
        mSlidingLayer.setLayerTransformer(new SlideJoyTransformer());


        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.add_date);
        datestatus = v.findViewById(R.id.ShowDate);

        previousdate = dialog.findViewById(R.id.previousdate);
        currentdate = dialog.findViewById(R.id.currentdate);
        selectdate = dialog.findViewById(R.id.selectdate);
        dialogyesterday = dialog.findViewById(R.id.showdyesterday);
        dialogtoday = dialog.findViewById(R.id.showdtoday);


        showDialogCurrentDate();
        showDialogPreviousDate();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 0);
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String today = dateFormat.format(calendar.getTime());
        datestatus.setText("TODAY IS: " + today);

        previousdate.setOnClickListener(this);
        currentdate.setOnClickListener(this);
        selectdate.setOnClickListener(this);

        calenderTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.show();
            }
        });

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toggleDrawer();
            }
        }, 50);

        return v;
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.previousdate) {

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            String yesterday = dateFormat.format(calendar.getTime());
            datestatus.setText("YESTERDAY WAS: " + yesterday);
            dialog.dismiss();

        } else if (v.getId() == R.id.currentdate) {

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, 0);
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            String today = dateFormat.format(calendar.getTime());
            datestatus.setText("TODAY IS: " + today);
            dialog.dismiss();
        } else if (v.getId() == R.id.selectdate) {

            DatePicker datePicker = new DatePicker(getActivity());
            int cdate = datePicker.getDayOfMonth();
            int cmonth = (datePicker.getMonth() + 1);
            int cyear = datePicker.getYear();

            DatePickerDialog datePickerDialog;
            datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                    datestatus.setText("SELECTED DATE: " + dayOfMonth + "-" + (month + 1) + "-" + year);
                    dialog.dismiss();
                }
            }, cyear, cmonth, cdate);
            datePickerDialog.show();

        }
    }

    public void showDialogPreviousDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String yesterday = dateFormat.format(calendar.getTime());
        dialogyesterday.setText(yesterday);
        dialog.dismiss();
    }

    public void showDialogCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 0);
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String today = dateFormat.format(calendar.getTime());
        dialogtoday.setText(today);
        dialog.dismiss();
    }

    public boolean isDrawerOpened() {
        if (mSlidingLayer == null) return false;
        return mSlidingLayer.isOpened();
    }

    public void toggleDrawer() {
        if (mSlidingLayer == null) return;

        if (mSlidingLayer.isOpened()) {
            mSlidingLayer.closeLayer(true);
        } else {
            mSlidingLayer.openLayer(true);
        }

    }
}
