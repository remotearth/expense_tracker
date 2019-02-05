package com.remotearthsolutions.expensetracker.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.razerdp.widget.animatedpieview.AnimatedPieView;
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.activities.MainActivity;
import com.remotearthsolutions.expensetracker.adapters.CategoryListAdapter;
import com.remotearthsolutions.expensetracker.contracts.HomeFragmentContract;
import com.remotearthsolutions.expensetracker.databaseutils.DatabaseClient;
import com.remotearthsolutions.expensetracker.databaseutils.daos.AccountDao;
import com.remotearthsolutions.expensetracker.databaseutils.daos.CategoryDao;
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel;
import com.remotearthsolutions.expensetracker.entities.ExpeneChartData;
import com.remotearthsolutions.expensetracker.utils.ChartManager;
import com.remotearthsolutions.expensetracker.utils.ChartManagerImpl;
import com.remotearthsolutions.expensetracker.utils.Constants;
import com.remotearthsolutions.expensetracker.utils.DateTimeUtils;
import com.remotearthsolutions.expensetracker.viewmodels.HomeFragmentViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class HomeFragment extends Fragment implements ChartManagerImpl.ChartView, HomeFragmentContract.View, View.OnClickListener {

    private CategoryListAdapter adapter;
    private AnimatedPieView mAnimatedPieView;
    private RecyclerView recyclerView;
    private HomeFragmentViewModel viewModel;
    private ImageView addCategory,nextDate,previousDate;
    private Button dailyButton,weeklyButton,monthlyButton,yearlyButton;
    private TextView dataTv;
    private String selectedDate;
    private Date startDate,endDate;
    private SimpleDateFormat simpleDateFormat;
    private Calendar calendar;

    private int day = 0;
    private int month = 0;
    private int year = 0;
    private int startingOfWeek = -7;
    private int endingOfWeek = 0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        addCategory = view.findViewById(R.id.addcategoryinhome);
        dataTv = view.findViewById(R.id.showdateinhome);
        nextDate = view.findViewById(R.id.nextdatebutton);
        previousDate = view.findViewById(R.id.previousdatebutton);
        dailyButton = view.findViewById(R.id.daily);
        weeklyButton = view.findViewById(R.id.weekly);
        monthlyButton = view.findViewById(R.id.monthly);
        yearlyButton = view.findViewById(R.id.yearly);

        addCategory.setOnClickListener(this);
        nextDate.setOnClickListener(this);
        previousDate.setOnClickListener(this);
        dailyButton.setOnClickListener(this);
        weeklyButton.setOnClickListener(this);
        monthlyButton.setOnClickListener(this);
        yearlyButton.setOnClickListener(this);


        mAnimatedPieView = view.findViewById(R.id.animatedpie);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(llm);

        CategoryDao categoryDao = DatabaseClient.getInstance(getContext()).getAppDatabase().categoryDao();
        AccountDao accountDao = DatabaseClient.getInstance(getContext()).getAppDatabase().accountDao();
        viewModel = new HomeFragmentViewModel(this, categoryDao, accountDao);
        viewModel.init();
        viewModel.loadExpenseChart();

        BottomNavigationView navigation = view.findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        simpleDateFormat = new SimpleDateFormat(DateTimeUtils.dd_MM_yyyy);
        selectedDate = Constants.KEY_DAILY;
        try {
            startDate = simpleDateFormat.parse(DateTimeUtils.getCurrentDate(DateTimeUtils.dd_MM_yyyy));
            String startdatetoget = simpleDateFormat.format(startDate);
            dataTv.setText(startdatetoget);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
       return view;

    }

    @Override
    public void loadChartConfig(AnimatedPieViewConfig config) {
        mAnimatedPieView.applyConfig(config).start();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                Toast.makeText(getActivity(), "Clicked On Bottom menu 1", Toast.LENGTH_LONG).show();
                return true;
            case R.id.navigation_dashboard:
                Toast.makeText(getActivity(), "Clicked On Bottom Menu 2", Toast.LENGTH_LONG).show();
                return true;
            case R.id.navigation_notifications:
                Toast.makeText(getActivity(), "Clicked On Bottom Menu 3", Toast.LENGTH_LONG).show();
                return true;
        }
        return false;
    };

    @Override
    public void showCategories(List<CategoryModel> categories) {

        adapter = new CategoryListAdapter(categories);
        adapter.setOnItemClickListener(new CategoryListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CategoryModel category) {
                ((MainActivity) getActivity()).openAddExpenseScreen(category);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void loadExpenseChart(List<ExpeneChartData> listOfCategoryWithAmount) {

        ChartManager chartManager = new ChartManagerImpl();
        chartManager.initPierChart();
        chartManager.loadExpensePieChart(this, listOfCategoryWithAmount);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.addcategoryinhome) {

            FragmentManager fm = getChildFragmentManager();
            final AddCategoryDialogFragment categoryDialogFragment = AddCategoryDialogFragment.newInstance("Add Category");
            categoryDialogFragment.setCallback(new AddCategoryDialogFragment.Callback() {
                @Override
                public void onCategoryAdded(CategoryModel categoryModel) {

                    categoryDialogFragment.dismiss();

                }
            });
            categoryDialogFragment.show(fm, AddCategoryDialogFragment.class.getName());

        }

        else if (v.getId() == R.id.nextdatebutton) {

            if (selectedDate.equals(Constants.KEY_DAILY))
            {

                if (day < 0) {
                    day = day + 1;
                }

                String nextdate = DateTimeUtils.getDate(DateTimeUtils.dd_MM_yyyy, day);
                dataTv.setText(nextdate);
            }

            if (selectedDate.equals(Constants.KEY_WEEKLY))
            {
                simpleDateFormat = new SimpleDateFormat(DateTimeUtils.dd_MM_yyyy);
                try
                {

                    if (endingOfWeek < 0) {
                        endingOfWeek = endingOfWeek + 7;
                        startingOfWeek = startingOfWeek + 7;
                    }

                    startDate = simpleDateFormat.parse(DateTimeUtils.getDate(DateTimeUtils.dd_MM_yyyy,startingOfWeek));
                    endDate = simpleDateFormat.parse(DateTimeUtils.getDate(DateTimeUtils.dd_MM_yyyy,endingOfWeek));

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String weekcurrentdate = simpleDateFormat.format(endDate);
                String weeklastdate = simpleDateFormat.format(startDate);
                dataTv.setText(weeklastdate+" - "+weekcurrentdate);
            }


            else if (selectedDate.equals(Constants.KEY_MONTHLY))
            {
                simpleDateFormat = new SimpleDateFormat(DateTimeUtils.MM_yy);
                calendar = Calendar.getInstance();
                if (month < 0) {
                    month = month + 1;
                }

                calendar.add(Calendar.MONTH, month);
                String previousmonth =simpleDateFormat.format(calendar.getTime());
                dataTv.setText(previousmonth);
            }

            else if (selectedDate.equals(Constants.KEY_YEARLY))
            {
                selectedDate = Constants.KEY_YEARLY;
                simpleDateFormat = new SimpleDateFormat(DateTimeUtils.yyyy);
                calendar =Calendar.getInstance();

                if (year < 0) {
                    year = year + 1;
                }
                calendar.add(Calendar.YEAR, year);
                String year = simpleDateFormat.format(calendar.getTime());
                dataTv.setText(year);
            }

        }

        else if (v.getId() == R.id.previousdatebutton) {

            if (selectedDate.equals(Constants.KEY_DAILY))
            {
                day = day - 1;

                String previousdate = DateTimeUtils.getDate(DateTimeUtils.dd_MM_yyyy, day);
                dataTv.setText(previousdate);
            }

            else if (selectedDate.equals(Constants.KEY_WEEKLY))
            {
                simpleDateFormat = new SimpleDateFormat(DateTimeUtils.dd_MM_yyyy);
                try
                {
                    startingOfWeek = startingOfWeek - 7;
                    endingOfWeek = endingOfWeek - 7;

                    endDate = simpleDateFormat.parse(DateTimeUtils.getDate(DateTimeUtils.dd_MM_yyyy, endingOfWeek));
                    startDate = simpleDateFormat.parse(DateTimeUtils.getDate(DateTimeUtils.dd_MM_yyyy, startingOfWeek));

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String weekcurrentdate = simpleDateFormat.format(endDate);
                String weeklastdate = simpleDateFormat.format(startDate);
                dataTv.setText(weeklastdate+" - "+weekcurrentdate);
            }

            else if (selectedDate.equals(Constants.KEY_MONTHLY))
            {
                simpleDateFormat = new SimpleDateFormat(DateTimeUtils.MM_yy);
                calendar = Calendar.getInstance();

                month = month - 1;

                calendar.add(Calendar.MONTH, month);
                String previousmonth =simpleDateFormat.format(calendar.getTime());
                dataTv.setText(previousmonth);
            }

            else if (selectedDate.equals(Constants.KEY_YEARLY))
            {

                selectedDate = Constants.KEY_YEARLY;
                simpleDateFormat = new SimpleDateFormat(DateTimeUtils.yyyy);
                calendar =Calendar.getInstance();

                year = year - 1;
                calendar.add(Calendar.YEAR, year);
                String year = simpleDateFormat.format(calendar.getTime());
                dataTv.setText(year);
            }

        }

        else if (v.getId() == R.id.daily)
        {
            selectedDate = Constants.KEY_DAILY;
            String dailydate = DateTimeUtils.getCurrentDate(DateTimeUtils.dd_MM_yyyy);
            dataTv.setText(dailydate);

        }

        else if (v.getId() == R.id.weekly)
        {
            selectedDate = Constants.KEY_WEEKLY;
            simpleDateFormat = new SimpleDateFormat(DateTimeUtils.dd_MM_yyyy);
            try
            {
                endDate = simpleDateFormat.parse(DateTimeUtils.getCurrentDate(DateTimeUtils.dd_MM_yyyy));
                startDate = simpleDateFormat.parse(DateTimeUtils.getDate(DateTimeUtils.dd_MM_yyyy,-7));

            } catch (ParseException e) {
                e.printStackTrace();
            }

            String weekcurrentdate = simpleDateFormat.format(endDate);
            String weeklastdate = simpleDateFormat.format(startDate);
            dataTv.setText(weeklastdate+" - "+weekcurrentdate);

        }

        else if (v.getId() == R.id.monthly)
        {
            selectedDate = Constants.KEY_MONTHLY;
            simpleDateFormat = new SimpleDateFormat(DateTimeUtils.MM_yy);
            calendar =Calendar.getInstance();
            String month =simpleDateFormat.format(calendar.getTime());
            dataTv.setText(month);

        }

        else if (v.getId() == R.id.yearly)
        {
            selectedDate = Constants.KEY_YEARLY;
            SimpleDateFormat sdf = new SimpleDateFormat(DateTimeUtils.yyyy);
            calendar =Calendar.getInstance();
            String year = sdf.format(calendar.getTime());
            dataTv.setText(year);

        }

    }

}

