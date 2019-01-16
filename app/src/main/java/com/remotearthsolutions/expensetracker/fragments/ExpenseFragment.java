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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.adapters.AccountListAdapter;
import com.remotearthsolutions.expensetracker.entities.Accounts;
import com.wunderlist.slidinglayer.SlidingLayer;
import com.wunderlist.slidinglayer.transformer.SlideJoyTransformer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ExpenseFragment extends Fragment implements View.OnClickListener {

    public ExpenseFragment() {
    }

    private Handler handler;
    private ImageView calenderTask;
    private Dialog datebaseddialog, accountbaseddialog;
    private LinearLayout previousdate, currentdate, selectdate;
    private TextView datestatus, dialogyesterday, dialogtoday;
    private SlidingLayer mSlidingLayer;

    private AccountListAdapter accountListAdapter;
    private List<Accounts> accountslist;
    private RecyclerView accountrecyclerView;
    private LinearLayout selectAccount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.add_expense, container, false);
        calenderTask = v.findViewById(R.id.selectdata);

        // load account list
        accountbaseddialog = new Dialog(getActivity());
        accountbaseddialog.setContentView(R.layout.add_account);
        accountrecyclerView = accountbaseddialog.findViewById(R.id.accountrecyclearView);
        accountrecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        accountrecyclerView.setLayoutManager(llm);
        selectAccount = v.findViewById(R.id.fromaccountselection);
        accountListAdapter = new AccountListAdapter(accountslist,getActivity());
        accountrecyclerView.setAdapter(accountListAdapter);
        loadAccountlIST();

        selectAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                accountbaseddialog.show();
            }
        });

        mSlidingLayer = v.findViewById(R.id.slidingDrawer);
        mSlidingLayer.setLayerTransformer(new SlideJoyTransformer());

        datebaseddialog = new Dialog(getActivity());
        datebaseddialog.setContentView(R.layout.add_date);
        datestatus = v.findViewById(R.id.ShowDate);

        previousdate = datebaseddialog.findViewById(R.id.previousdate);
        currentdate = datebaseddialog.findViewById(R.id.currentdate);
        selectdate = datebaseddialog.findViewById(R.id.selectdate);
        dialogyesterday = datebaseddialog.findViewById(R.id.showdyesterday);
        dialogtoday = datebaseddialog.findViewById(R.id.showdtoday);


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

                datebaseddialog.show();
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

    public void loadAccountlIST()
    {
        accountslist = new ArrayList<>();
        accountslist.add(new Accounts(R.drawable.ic_currency,"CASH",1000.00));
        accountslist.add(new Accounts(R.drawable.ic_currency,"BANK",2000.00));
        accountslist.add(new Accounts(R.drawable.ic_currency,"LOAN",3000.00));

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.previousdate) {

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            String yesterday = dateFormat.format(calendar.getTime());
            datestatus.setText("YESTERDAY WAS: " + yesterday);
            datebaseddialog.dismiss();

        } else if (v.getId() == R.id.currentdate) {

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, 0);
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            String today = dateFormat.format(calendar.getTime());
            datestatus.setText("TODAY IS: " + today);
            datebaseddialog.dismiss();
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
                    datebaseddialog.dismiss();
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
        datebaseddialog.dismiss();
    }

    public void showDialogCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 0);
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String today = dateFormat.format(calendar.getTime());
        dialogtoday.setText(today);
        datebaseddialog.dismiss();
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
