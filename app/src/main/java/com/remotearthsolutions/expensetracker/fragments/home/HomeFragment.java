package com.remotearthsolutions.expensetracker.fragments.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.remotearthsolutions.expensetracker.fragments.AddCategoryDialogFragment;
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


public class HomeFragment extends Fragment implements ChartManagerImpl.ChartView, HomeFragmentContract.View, View.OnClickListener, DateFilterButtonClickListener.Callback {

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

    private long startTime, endTime;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        View view = binding.getRoot();

        binding.addCategoryBtn.setOnClickListener(this);
        binding.fab.setOnClickListener(this);

        DateFilterButtonClickListener dateFilterButtonClickListener = new DateFilterButtonClickListener(this);
        binding.nextDateBtn.setOnClickListener(dateFilterButtonClickListener);
        binding.previousDateBtn.setOnClickListener(dateFilterButtonClickListener);
        binding.dailyRangeBtn.setOnClickListener(dateFilterButtonClickListener);
        binding.weeklyRangeBtn.setOnClickListener(dateFilterButtonClickListener);
        binding.monthlyRangeBtn.setOnClickListener(dateFilterButtonClickListener);
        binding.yearlyRangeBtn.setOnClickListener(dateFilterButtonClickListener);


        binding.recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        binding.recyclerView.setLayoutManager(llm);

        CategoryDao categoryDao = DatabaseClient.getInstance(getContext()).getAppDatabase().categoryDao();
        AccountDao accountDao = DatabaseClient.getInstance(getContext()).getAppDatabase().accountDao();
        viewModel = new HomeFragmentViewModel(this, categoryDao, accountDao);
        viewModel.init();
        viewModel.loadExpenseChart();

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

        } else if (v.getId() == R.id.fab) {
            CategoryModel categoryModel = viewModel.getFirstCategory();
            ((MainActivity) getActivity()).openAddExpenseScreen(categoryModel);
        }

    }

    @Override
    public void onDateChanged(String date, long startTime, long endTime) {
        binding.dateTv.setText(date);
        this.startTime = startTime;
        this.endTime = endTime;
    }
}

