package com.remotearthsolutions.expensetracker.fragments;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import com.remotearthsolutions.expensetracker.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ExpenseFragment extends Fragment implements View.OnClickListener {


    public ExpenseFragment() {

    }

    private View v;
    private ImageView calenderTask;
    private Dialog dialog;
    private LinearLayout previousdate,currentdate, selectdate;
    private TextView datestatus,dialogyesterday,dialogtoday;


    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.add_expense, container, false);
        calenderTask = v.findViewById(R.id.selectdata);

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
        calendar.add(Calendar.DATE,0);
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String today = dateFormat.format(calendar.getTime());
        datestatus.setText("TODAY IS: "+today);

        previousdate.setOnClickListener(this);
        currentdate.setOnClickListener(this);
        selectdate.setOnClickListener(this);

        calenderTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.show();
            }
        });

        return v;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.previousdate) {

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            String yesterday = dateFormat.format(calendar.getTime());
            datestatus.setText("YESTERDAY WAS: "+yesterday);
            dialog.dismiss();

        }
        if (v.getId() == R.id.currentdate) {

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE,0);
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            String today = dateFormat.format(calendar.getTime());
            datestatus.setText("TODAY IS: "+today);
            dialog.dismiss();
        }

        if (v.getId() == R.id.selectdate) {

            DatePicker datePicker = new DatePicker(getActivity());
            int cdate = datePicker.getDayOfMonth();
            int cmonth = (datePicker.getMonth()+1);
            int cyear = datePicker.getYear();

            DatePickerDialog datePickerDialog;
            datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                    datestatus.setText("SELECTED DATE: "+dayOfMonth+"-"+(month+1)+"-"+year);
                    dialog.dismiss();
                }
            },cyear,cmonth,cdate);
            datePickerDialog.show();

        }

    }

    public void showDialogPreviousDate()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,-1);
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String yesterday = dateFormat.format(calendar.getTime());
        dialogyesterday.setText(yesterday);
        dialog.dismiss();
    }

    public void showDialogCurrentDate()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,0);
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String today = dateFormat.format(calendar.getTime());
        dialogtoday.setText(today);
        dialog.dismiss();
    }




}
