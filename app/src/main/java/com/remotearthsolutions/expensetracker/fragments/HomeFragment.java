package com.remotearthsolutions.expensetracker.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.remotearthsolutions.expensetracker.databaseutils.daos.CategoryDao;
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel;
import com.remotearthsolutions.expensetracker.entities.ExpeneChartData;
import com.remotearthsolutions.expensetracker.utils.ChartManager;
import com.remotearthsolutions.expensetracker.utils.ChartManagerImpl;
import com.remotearthsolutions.expensetracker.utils.DateTimeUtils;
import com.remotearthsolutions.expensetracker.viewmodels.CategoryFragmentViewModel;
import com.remotearthsolutions.expensetracker.viewmodels.HomeFragmentViewModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class HomeFragment extends Fragment implements ChartManagerImpl.ChartView, HomeFragmentContract.View, View.OnClickListener {

    private CategoryListAdapter adapter;
    private AnimatedPieView mAnimatedPieView;
    private RecyclerView recyclerView;
    private HomeFragmentViewModel viewModel;
    private ImageView addCategory,nextDate,previousDate;
    private Button dailyButton,weeklyButton,monthlyButton,yearlyButton;

    private TextView showdate;
    private Date getPreviousDate,getNextDate,getCurrentDate;
    private Calendar calendar;
    private Date getPreviousWeek,getNextWeak,getCurrentWeek;
    private Date getPreviousMonth,getNextMonth,getCurrentMonth;
    private Date getPreviousYear,getNextYear,getCurrentYear;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        addCategory = view.findViewById(R.id.addcategoryinhome);
        showdate = view.findViewById(R.id.showdateinhome);
        nextDate = view.findViewById(R.id.nextdatebutton);
        previousDate = view.findViewById(R.id.previousdatebutton);
        dailyButton = view.findViewById(R.id.daily);
        weeklyButton = view.findViewById(R.id.weekly);
        monthlyButton= view.findViewById(R.id.monthly);
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
        viewModel = new HomeFragmentViewModel(this, categoryDao);
        viewModel.init();
        viewModel.loadExpenseChart();

        BottomNavigationView navigation = view.findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        showdate.setText(DateTimeUtils.getCurrentDate(DateTimeUtils.dd_MM_yyyy));
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

            SimpleDateFormat sdf = new SimpleDateFormat(DateTimeUtils.dd_MM_yyyy);
            String getstatusdate = showdate.getText().toString();

            try {

                getCurrentDate = sdf.parse(DateTimeUtils.getCurrentDate(DateTimeUtils.dd_MM_yyyy));
                getNextDate = sdf.parse(DateTimeUtils.getDate(DateTimeUtils.dd_MM_yyyy,+1));

                if (getNextDate.compareTo(getCurrentDate) > 0)
                {
                    showdate.setText(DateTimeUtils.getCurrentDate(DateTimeUtils.dd_MM_yyyy));
                }

                getNextWeak = sdf.parse(DateTimeUtils.getDate(DateTimeUtils.dd_MM_yyyy,+7));
                String weeknextdate = sdf.format(getNextWeak);

                if (getstatusdate.matches(weeknextdate))
                {
                    Toast.makeText(getActivity(), "Not possible to move into next week",Toast.LENGTH_LONG).show();
                }




            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        else if (v.getId() == R.id.previousdatebutton) {

            String previousdate = DateTimeUtils.getDate(DateTimeUtils.dd_MM_yyyy, -1);
            showdate.setText(previousdate);
        }

        else if (v.getId() == R.id.daily)
        {
            String previous = DateTimeUtils.getCurrentDate(DateTimeUtils.dd_MM_yyyy);
            showdate.setText(previous);

        }

        else if (v.getId() == R.id.weekly)
        {
            try
            {
                SimpleDateFormat sdf = new SimpleDateFormat(DateTimeUtils.dd_MM_yyyy);
                getCurrentWeek = sdf.parse(DateTimeUtils.getCurrentDate(DateTimeUtils.dd_MM_yyyy));
                getPreviousWeek = sdf.parse(DateTimeUtils.getDate(DateTimeUtils.dd_MM_yyyy,-7));
                String weekcurrentdate = sdf.format(getCurrentWeek);
                String weeklastdate = sdf.format(getPreviousWeek);
                showdate.setText(weeklastdate+" - "+weekcurrentdate);

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        else if (v.getId() == R.id.monthly)
        {
            SimpleDateFormat sdf = new SimpleDateFormat(DateTimeUtils.MM_yy);
            calendar =Calendar.getInstance();
            String month =sdf.format(calendar.getTime());
            showdate.setText(month);

        }

        else if (v.getId() == R.id.yearly)
        {
            SimpleDateFormat sdf = new SimpleDateFormat(DateTimeUtils.yyyy);
            calendar =Calendar.getInstance();
            String year = sdf.format(calendar.getTime());
            showdate.setText(year);

        }



    }
}
