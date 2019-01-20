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
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.entities.Account;
import com.remotearthsolutions.expensetracker.entities.Category;
import com.wunderlist.slidinglayer.SlidingLayer;
import com.wunderlist.slidinglayer.transformer.SlideJoyTransformer;
import org.parceler.Parcels;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ExpenseFragment extends Fragment implements View.OnClickListener {

    public ExpenseFragment() {
    }

    private Handler handler;
    private ImageView calenderTask, categoryImageIv,accountImageIv;
    private Dialog datebaseddialog;
    private LinearLayout previousdate, currentdate, selectdate;
    private TextView datestatus, dialogyesterday, dialogtoday, categoryNameTv,accountNameTv;
    private SlidingLayer mSlidingLayer;
    private LinearLayout selectAccount, selectCategory;
    private int cDay, cMonth, cYear;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_expense, container, false);
        categoryImageIv = view.findViewById(R.id.showcatimage);
        categoryNameTv = view.findViewById(R.id.showcatname);
        accountNameTv = view.findViewById(R.id.accountNameTv);
        accountImageIv = view.findViewById(R.id.accountImageIv);

        calenderTask = view.findViewById(R.id.selectdata);
        selectAccount = view.findViewById(R.id.fromaccountselection);
        selectCategory = view.findViewById(R.id.categorylayout);
        mSlidingLayer = view.findViewById(R.id.slidingDrawer);

        datestatus = view.findViewById(R.id.ShowDate);

        Bundle args = getArguments();
        if (args  != null){

            Category category = Parcels.unwrap(args.getParcelable("category_parcel"));
            categoryImageIv.setImageResource(category.getCategoryImage());
            categoryNameTv.setText(category.getCategoryName());
        }

        Calendar calendar = Calendar.getInstance();
        cDay = calendar.get(Calendar.DAY_OF_MONTH);
        cMonth = calendar.get(Calendar.MONTH);
        cYear = calendar.get(Calendar.YEAR);



        selectAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                FragmentManager fm = getChildFragmentManager();
                final AccountDialogFragment accountDialogFragment = AccountDialogFragment.newInstance("Select Account");
                accountDialogFragment.setCallback(new AccountDialogFragment.Callback() {
                    @Override
                    public void onSelectAccount(Account account) {
                        accountImageIv.setImageResource(account.getAccountImage());
                        accountNameTv.setText(account.getAccountName());
                        accountDialogFragment.dismiss();
                    }
                });
                accountDialogFragment.show(fm, AccountDialogFragment.class.getName());

            }
        });


        selectCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm = getChildFragmentManager();
                final CategoryDialogFragment editNameDialogFragment = CategoryDialogFragment.newInstance("Select Category");
                editNameDialogFragment.setCallback(new CategoryDialogFragment.Callback() {
                    @Override
                    public void onSelectCategory(Category category) {
                        categoryImageIv.setImageResource(category.getCategoryImage());
                        categoryNameTv.setText(category.getCategoryName());
                        editNameDialogFragment.dismiss();
                    }
                });
                editNameDialogFragment.show(fm, CategoryDialogFragment.class.getName());
            }
        });

        mSlidingLayer.setLayerTransformer(new SlideJoyTransformer());
        datebaseddialog = new Dialog(getActivity());
        datebaseddialog.setContentView(R.layout.add_date);
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

        return view;
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
