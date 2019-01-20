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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
    private Dialog datebaseddialog;
    private LinearLayout previousdate, currentdate, selectdate;
    private TextView datestatus, dialogyesterday, dialogtoday;
    private SlidingLayer mSlidingLayer;
    private LinearLayout selectAccount, selectCategory;
    private int cDay, cMonth, cYear;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.add_expense, container, false);
        calenderTask = v.findViewById(R.id.selectdata);

        // receiving Data from Category Fragment
        Bundle args = getArguments();
        if (args  != null){

            int getImage = getArguments().getInt("image");
            String getName = getArguments().getString("name");
            ImageView imageView = v.findViewById(R.id.showcatimage);
            TextView textView = v.findViewById(R.id.showcatname);
            imageView.setImageResource(getImage);
            textView.setText(getName);
        }

        Calendar calendar = Calendar.getInstance();
        cDay = calendar.get(Calendar.DAY_OF_MONTH);
        cMonth = calendar.get(Calendar.MONTH);
        cYear = calendar.get(Calendar.YEAR);


        selectAccount = v.findViewById(R.id.fromaccountselection);
        selectAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                FragmentManager fm = getChildFragmentManager();
                AccountDialogFragment editNameDialogFragment = AccountDialogFragment.newInstance("Select Account");
                editNameDialogFragment.show(fm, AccountDialogFragment.class.getName());

            }
        });

        selectCategory = v.findViewById(R.id.categorylayout);
        selectCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm = getChildFragmentManager();
                CategoryFragment editNameDialogFragment = CategoryFragment.newInstance("Select Category");
                editNameDialogFragment.show(fm, CategoryFragment.class.getName());

                
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

        calendar = Calendar.getInstance();
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

            DatePickerDialog datePickerDialog;
            datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    cYear = year;
                    cMonth = month;
                    cDay = dayOfMonth;

                    datestatus.setText("SELECTED DATE: " + dayOfMonth + "-" + (month + 1) + "-" + year);
                    datebaseddialog.dismiss();
                }
            }, cYear, cMonth, cDay);
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
