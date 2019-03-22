package com.remotearthsolutions.expensetracker.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.activities.ApplicationObject;
import com.remotearthsolutions.expensetracker.activities.MainActivity;
import com.remotearthsolutions.expensetracker.adapters.CategoryListAdapter;
import com.remotearthsolutions.expensetracker.contracts.HomeFragmentContract;
import com.remotearthsolutions.expensetracker.databaseutils.AppDatabase;
import com.remotearthsolutions.expensetracker.databaseutils.DatabaseClient;
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel;
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.CategoryExpense;
import com.remotearthsolutions.expensetracker.databinding.FragmentHomeBinding;
import com.remotearthsolutions.expensetracker.entities.ExpeneChartData;
import com.remotearthsolutions.expensetracker.utils.ChartManager;
import com.remotearthsolutions.expensetracker.utils.ChartManagerImpl;
import com.remotearthsolutions.expensetracker.utils.Utils;
import com.remotearthsolutions.expensetracker.viewmodels.HomeFragmentViewModel;
import com.remotearthsolutions.expensetracker.viewmodels.viewmodel_factory.HomeFragmentViewModelFactory;

import java.util.List;

public class HomeFragment extends BaseFragment implements ChartManagerImpl.ChartView, HomeFragmentContract.View, View.OnClickListener {

    private CategoryListAdapter adapter;
    private HomeFragmentViewModel viewModel;
    private FragmentHomeBinding binding;
    private Integer limitOfCategory;
    private long startTime, endTime;
    private List<ExpeneChartData> listOfCategoryWithAmount;
    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.addCategoryBtn.setOnClickListener(this);
        binding.fab.setOnClickListener(this);

        binding.recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        binding.recyclerView.setLayoutManager(llm);

        AppDatabase db = DatabaseClient.getInstance(getContext()).getAppDatabase();
        viewModel = ViewModelProviders.of(this, new HomeFragmentViewModelFactory(this, db.categoryExpenseDao(), db.categoryDao(), db.accountDao()))
                .get(HomeFragmentViewModel.class);
        viewModel.init();
        viewModel.loadExpenseChart(startTime, endTime);

        viewModel.getNumberOfItem().observe(getViewLifecycleOwner(), (Integer integer) -> limitOfCategory = integer);

    }

    @Override
    public void loadChartConfig(AnimatedPieViewConfig config) {
        binding.chartView.applyConfig(config).start();
    }

    @Override
    public void showCategories(List<CategoryModel> categories) {

        adapter = new CategoryListAdapter(categories);
        adapter.setScreenSize(Utils.getDeviceScreenSize(context));
        adapter.setOnItemClickListener(category -> {
            if (context != null) {
                CategoryExpense categoryExpense = new CategoryExpense();
                categoryExpense.setCategory(category);
                ((MainActivity) context).openAddExpenseScreen(categoryExpense);
            }
        });
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void loadExpenseChart(List<ExpeneChartData> listOfCategoryWithAmount) {
        this.listOfCategoryWithAmount = listOfCategoryWithAmount;

        ChartManager chartManager = new ChartManagerImpl();
        chartManager.initPierChart(Utils.getDeviceDP(context), Utils.getDeviceScreenSize(context));
        if (listOfCategoryWithAmount == null || listOfCategoryWithAmount.size() == 0) {
            binding.chartView.setVisibility(View.GONE);
            binding.nodatacontainer.setVisibility(View.VISIBLE);

        } else {
            binding.chartView.setVisibility(View.VISIBLE);
            binding.nodatacontainer.setVisibility(View.GONE);
            chartManager.loadExpensePieChart(context, this, listOfCategoryWithAmount);
        }
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.addCategoryBtn) {

            if (limitOfCategory < 20 ||
                    ((ApplicationObject) ((Activity) context).getApplication()).isPremium()) {
                FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                final AddCategoryDialogFragment categoryDialogFragment = AddCategoryDialogFragment.newInstance(getString(R.string.add_category));
                categoryDialogFragment.setCallback(categoryModel -> categoryDialogFragment.dismiss());
                categoryDialogFragment.show(ft, AddCategoryDialogFragment.class.getName());
            } else {
                showAlert(getString(R.string.attention), getString(R.string.you_need_to_be_premium_user_to_add_more_categories), getString(R.string.ok), null, null);
            }


        } else if (v.getId() == R.id.fab) {
            binding.fab.setClickable(false);
            ((MainActivity) context).openAddExpenseScreen(null);
            new Handler().postDelayed(() -> binding.fab.setClickable(true), 500);
        }

    }

    public void updateChartView(long startTime, long endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        viewModel.loadExpenseChart(startTime, endTime);
    }

    public void refresh() {
        loadExpenseChart(this.listOfCategoryWithAmount);
    }

    @Override
    public Context getContext() {
        return context;
    }
}

