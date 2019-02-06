package com.remotearthsolutions.expensetracker.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.activities.MainActivity;
import com.remotearthsolutions.expensetracker.adapters.CategoryListAdapter;
import com.remotearthsolutions.expensetracker.contracts.HomeFragmentContract;
import com.remotearthsolutions.expensetracker.databaseutils.DatabaseClient;
import com.remotearthsolutions.expensetracker.databaseutils.daos.AccountDao;
import com.remotearthsolutions.expensetracker.databaseutils.daos.CategoryDao;
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel;
import com.remotearthsolutions.expensetracker.databinding.FragmentHomeBinding;
import com.remotearthsolutions.expensetracker.entities.ExpeneChartData;
import com.remotearthsolutions.expensetracker.utils.ChartManager;
import com.remotearthsolutions.expensetracker.utils.ChartManagerImpl;
import com.remotearthsolutions.expensetracker.utils.Constants;
import com.remotearthsolutions.expensetracker.utils.DateTimeUtils;
import com.remotearthsolutions.expensetracker.viewmodels.HomeFragmentViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class HomeFragment extends Fragment implements ChartManagerImpl.ChartView, HomeFragmentContract.View, View.OnClickListener {

    private CategoryListAdapter adapter;
    private HomeFragmentViewModel viewModel;
    private FragmentHomeBinding binding;
    private String selectedDate;
    private Date startDate, endDate;
    private SimpleDateFormat simpleDateFormat;
    private Calendar calendar;

    private int day = 0;
    private int month = 0;
    private int year = 0;
    private int startingOfWeek = -7;
    private int endingOfWeek = 0;

    private long startTime,endTime;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        View view = binding.getRoot();

        binding.addCategoryBtn.setOnClickListener(this);
        binding.nextDateBtn.setOnClickListener(this);
        binding.previousDateBtn.setOnClickListener(this);
        binding.dailyRangeBtn.setOnClickListener(this);
        binding.weeklyRangeBtn.setOnClickListener(this);
        binding.monthlyRangeBtn.setOnClickListener(this);
        binding.yearlyRangeBtn.setOnClickListener(this);
        binding.fab.setOnClickListener(this);

        binding.recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        binding.recyclerView.setLayoutManager(llm);

        CategoryDao categoryDao = DatabaseClient.getInstance(getContext()).getAppDatabase().categoryDao();
        AccountDao accountDao = DatabaseClient.getInstance(getContext()).getAppDatabase().accountDao();
        viewModel = new HomeFragmentViewModel(this, categoryDao, accountDao);
        viewModel.init();
        viewModel.loadExpenseChart();

        binding.navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        simpleDateFormat = new SimpleDateFormat(DateTimeUtils.dd_MM_yyyy);
        selectedDate = Constants.KEY_DAILY;
        try {
            startDate = simpleDateFormat.parse(DateTimeUtils.getCurrentDate(DateTimeUtils.dd_MM_yyyy));
            String startdatetoget = simpleDateFormat.format(startDate);
            binding.dateTv.setText(startdatetoget);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return view;

    }

    @Override
    public void loadChartConfig(AnimatedPieViewConfig config) {
        binding.chartView.applyConfig(config).start();
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
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void loadExpenseChart(List<ExpeneChartData> listOfCategoryWithAmount) {

        ChartManager chartManager = new ChartManagerImpl();
        chartManager.initPierChart();
        chartManager.loadExpensePieChart(this, listOfCategoryWithAmount);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.addCategoryBtn) {

            FragmentManager fm = getChildFragmentManager();
            final AddCategoryDialogFragment categoryDialogFragment = AddCategoryDialogFragment.newInstance("Add Category");
            categoryDialogFragment.setCallback(new AddCategoryDialogFragment.Callback() {
                @Override
                public void onCategoryAdded(CategoryModel categoryModel) {

                    categoryDialogFragment.dismiss();

                }
            });
            categoryDialogFragment.show(fm, AddCategoryDialogFragment.class.getName());


        } else if (v.getId() == R.id.nextDateBtn) {

            if (selectedDate.equals(Constants.KEY_DAILY)) {

                if (day < 0) {
                    day = day + 1;
                }

                String nextdate = DateTimeUtils.getDate(DateTimeUtils.dd_MM_yyyy, day);
                binding.dateTv.setText(nextdate);
            }

            if (selectedDate.equals(Constants.KEY_WEEKLY)) {
                simpleDateFormat = new SimpleDateFormat(DateTimeUtils.dd_MM_yyyy);
                try {

                    if (endingOfWeek < 0) {
                        endingOfWeek = endingOfWeek + 7;
                        startingOfWeek = startingOfWeek + 7;
                    }

                    startDate = simpleDateFormat.parse(DateTimeUtils.getDate(DateTimeUtils.dd_MM_yyyy, startingOfWeek));
                    endDate = simpleDateFormat.parse(DateTimeUtils.getDate(DateTimeUtils.dd_MM_yyyy, endingOfWeek));

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String weekcurrentdate = simpleDateFormat.format(endDate);
                String weeklastdate = simpleDateFormat.format(startDate);
                binding.dateTv.setText(weeklastdate + " - " + weekcurrentdate);
            } else if (selectedDate.equals(Constants.KEY_MONTHLY)) {
                simpleDateFormat = new SimpleDateFormat(DateTimeUtils.MM_yy);
                calendar = Calendar.getInstance();
                if (month < 0) {
                    month = month + 1;
                }

                calendar.add(Calendar.MONTH, month);
                String previousmonth = simpleDateFormat.format(calendar.getTime());
                binding.dateTv.setText(previousmonth);
            } else if (selectedDate.equals(Constants.KEY_YEARLY)) {
                selectedDate = Constants.KEY_YEARLY;
                simpleDateFormat = new SimpleDateFormat(DateTimeUtils.yyyy);
                calendar = Calendar.getInstance();

                if (year < 0) {
                    year = year + 1;
                }

                calendar.add(Calendar.YEAR, year);
                String year = simpleDateFormat.format(calendar.getTime());
                binding.dateTv.setText(year);
            }

        } else if (v.getId() == R.id.previousDateBtn) {

            if (selectedDate.equals(Constants.KEY_DAILY)) {
                day = day - 1;

                String previousdate = DateTimeUtils.getDate(DateTimeUtils.dd_MM_yyyy, day);
                binding.dateTv.setText(previousdate);
            } else if (selectedDate.equals(Constants.KEY_WEEKLY)) {
                simpleDateFormat = new SimpleDateFormat(DateTimeUtils.dd_MM_yyyy);
                try {
                    startingOfWeek = startingOfWeek - 7;
                    endingOfWeek = endingOfWeek - 7;

                    endDate = simpleDateFormat.parse(DateTimeUtils.getDate(DateTimeUtils.dd_MM_yyyy, endingOfWeek));
                    startDate = simpleDateFormat.parse(DateTimeUtils.getDate(DateTimeUtils.dd_MM_yyyy, startingOfWeek));

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String weekcurrentdate = simpleDateFormat.format(endDate);
                String weeklastdate = simpleDateFormat.format(startDate);
                binding.dateTv.setText(weeklastdate + " - " + weekcurrentdate);
            } else if (selectedDate.equals(Constants.KEY_MONTHLY)) {
                simpleDateFormat = new SimpleDateFormat(DateTimeUtils.MM_yy);
                calendar = Calendar.getInstance();
                month = month - 1;

                calendar.add(Calendar.MONTH, month);
                String previousmonth = simpleDateFormat.format(calendar.getTime());
                binding.dateTv.setText(previousmonth);
            } else if (selectedDate.equals(Constants.KEY_YEARLY)) {

                selectedDate = Constants.KEY_YEARLY;
                simpleDateFormat = new SimpleDateFormat(DateTimeUtils.yyyy);
                calendar = Calendar.getInstance();

                year = year - 1;
                calendar.add(Calendar.YEAR, year);
                String year = simpleDateFormat.format(calendar.getTime());
                binding.dateTv.setText(year);
            }

        } else if (v.getId() == R.id.dailyRangeBtn) {
            resetDate();

            selectedDate = Constants.KEY_DAILY;
            String dailydate = DateTimeUtils.getDate(DateTimeUtils.dd_MM_yyyy, day);
            binding.dateTv.setText(dailydate);

            calendar = DateTimeUtils.getCalendarFromDateString(DateTimeUtils.dd_MM_yyyy,dailydate);
            calendar.set(Calendar.HOUR_OF_DAY,0);
            calendar.set(Calendar.MINUTE,0);
            calendar.set(Calendar.SECOND,0);
            startTime = calendar.getTimeInMillis();
            calendar.set(Calendar.HOUR_OF_DAY,23);
            calendar.set(Calendar.MINUTE,59);
            calendar.set(Calendar.SECOND,59);
            endTime = calendar.getTimeInMillis();
            Toast.makeText(getActivity(), "start time"+startTime+" endtime"+endTime,Toast.LENGTH_SHORT).show();


        } else if (v.getId() == R.id.weeklyRangeBtn) {
            resetDate();

            selectedDate = Constants.KEY_WEEKLY;
            simpleDateFormat = new SimpleDateFormat(DateTimeUtils.dd_MM_yyyy);
            try {
                startDate = simpleDateFormat.parse(DateTimeUtils.getDate(DateTimeUtils.dd_MM_yyyy, startingOfWeek));
                endDate = simpleDateFormat.parse(DateTimeUtils.getDate(DateTimeUtils.dd_MM_yyyy, endingOfWeek));

            } catch (ParseException e) {
                e.printStackTrace();
            }

            String weekcurrentdate = simpleDateFormat.format(endDate);
            String weeklastdate = simpleDateFormat.format(startDate);
            binding.dateTv.setText(weeklastdate + " - " + weekcurrentdate);

            Calendar calendarforweeklastday = DateTimeUtils.getCalendarFromDateString(DateTimeUtils.dd_MM_yyyy,weeklastdate);
            calendarforweeklastday.set(Calendar.HOUR_OF_DAY,0);
            calendarforweeklastday.set(Calendar.MINUTE,0);
            calendarforweeklastday.set(Calendar.SECOND,0);
            startTime = calendarforweeklastday.getTimeInMillis();
            Calendar calendarforweekendday = DateTimeUtils.getCalendarFromDateString(DateTimeUtils.dd_MM_yyyy,weekcurrentdate);
            calendarforweekendday.set(Calendar.HOUR_OF_DAY,23);
            calendarforweekendday.set(Calendar.MINUTE,59);
            calendarforweekendday.set(Calendar.SECOND,59);
            endTime = calendarforweekendday.getTimeInMillis();
            Toast.makeText(getActivity(), "start time: "+startTime+" endtime: "+endTime,Toast.LENGTH_SHORT).show();

        } else if (v.getId() == R.id.monthlyRangeBtn) {
            resetDate();

            selectedDate = Constants.KEY_MONTHLY;

            simpleDateFormat = new SimpleDateFormat(DateTimeUtils.MM_yy);

            calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, month);
            String currentMonth = simpleDateFormat.format(calendar.getTime());
            binding.dateTv.setText(currentMonth);

            calendar = DateTimeUtils.getCalendarFromDateString(DateTimeUtils.dd_MM_yyyy,currentMonth);
            calendar.set(Calendar.DAY_OF_MONTH,1);
            calendar.set(Calendar.HOUR_OF_DAY,0);
            calendar.set(Calendar.MINUTE,0);
            calendar.set(Calendar.SECOND,0);
            startTime = calendar.getTimeInMillis();

            calendar.set(Calendar.DAY_OF_MONTH,28);
            calendar.set(Calendar.HOUR_OF_DAY,23);
            calendar.set(Calendar.MINUTE,59);
            calendar.set(Calendar.SECOND,59);
            endTime = calendar.getTimeInMillis();
            Toast.makeText(getActivity(), "start time"+startTime+" endtime"+endTime,Toast.LENGTH_SHORT).show();

        }
        else if (v.getId() == R.id.yearlyRangeBtn) {
            resetDate();

            selectedDate = Constants.KEY_YEARLY;
            SimpleDateFormat sdf = new SimpleDateFormat(DateTimeUtils.yyyy);

            calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR, year);
            String currentYear = sdf.format(calendar.getTime());
            binding.dateTv.setText(currentYear);

        }
        else if (v.getId() == R.id.fab) {
            CategoryModel categoryModel = viewModel.getFirstCategory();
            ((MainActivity) getActivity()).openAddExpenseScreen(categoryModel);
        }

    }

    private void resetDate() {
        day = 0;
        month = 0;
        year = 0;
        startingOfWeek = -7;
        endingOfWeek = 0;
    }

}

