package com.remotearthsolutions.expensetracker.fragments.home;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.activities.MainActivity;
import com.remotearthsolutions.expensetracker.adapters.CategoryListAdapter;
import com.remotearthsolutions.expensetracker.contracts.HomeFragmentContract;
import com.remotearthsolutions.expensetracker.databaseutils.AppDatabase;
import com.remotearthsolutions.expensetracker.databaseutils.DatabaseClient;
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel;
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.CategoryExpense;
import com.remotearthsolutions.expensetracker.databinding.FragmentHomeBinding;
import com.remotearthsolutions.expensetracker.entities.ExpeneChartData;
import com.remotearthsolutions.expensetracker.fragments.AddCategoryDialogFragment;
import com.remotearthsolutions.expensetracker.fragments.BaseFragment;
import com.remotearthsolutions.expensetracker.utils.ChartManager;
import com.remotearthsolutions.expensetracker.utils.ChartManagerImpl;
import com.remotearthsolutions.expensetracker.viewmodels.HomeFragmentViewModel;
import com.remotearthsolutions.expensetracker.viewmodels.viewmodel_factory.HomeFragmentViewModelFactory;

import java.util.List;

public class HomeFragment extends BaseFragment implements ChartManagerImpl.ChartView, HomeFragmentContract.View, View.OnClickListener {

    private CategoryListAdapter adapter;
    private HomeFragmentViewModel viewModel;
    private FragmentHomeBinding binding;
    private Integer limitOfCategory;
    private long startTime, endTime;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        binding.addCategoryBtn.setOnClickListener(this);
        binding.fab.setOnClickListener(this);

        binding.recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        binding.recyclerView.setLayoutManager(llm);

        AppDatabase db = DatabaseClient.getInstance(getContext()).getAppDatabase();
        viewModel = ViewModelProviders.of(this, new HomeFragmentViewModelFactory(this, db.categoryExpenseDao(), db.categoryDao(), db.accountDao()))
                .get(HomeFragmentViewModel.class);
        viewModel.init();
        viewModel.loadExpenseChart(startTime, endTime);

        viewModel.getNumberOfItem().observe(getViewLifecycleOwner(), (Integer integer) -> limitOfCategory = integer);

        return binding.getRoot();
    }

    @Override
    public void loadChartConfig(AnimatedPieViewConfig config) {
        binding.chartView.applyConfig(config).start();
    }

    @Override
    public void showCategories(List<CategoryModel> categories) {

        adapter = new CategoryListAdapter(categories);
        adapter.setOnItemClickListener(category -> {
            if (getActivity() != null) {
                CategoryExpense categoryExpense = new CategoryExpense();
                categoryExpense.setCategory(category);
                ((MainActivity) getActivity()).openAddExpenseScreen(categoryExpense);
            }
        });
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void loadExpenseChart(List<ExpeneChartData> listOfCategoryWithAmount) {

        ChartManager chartManager = new ChartManagerImpl();
        chartManager.initPierChart();
        if (listOfCategoryWithAmount == null || listOfCategoryWithAmount.size() == 0) {
            binding.chartView.setVisibility(View.GONE);
            binding.nodatacontainer.setVisibility(View.VISIBLE);

        } else {
            binding.chartView.setVisibility(View.VISIBLE);
            binding.nodatacontainer.setVisibility(View.GONE);
            chartManager.loadExpensePieChart(this, listOfCategoryWithAmount);
        }
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.addCategoryBtn) {

            if (limitOfCategory < 20) {
                FragmentManager fm = getChildFragmentManager();
                final AddCategoryDialogFragment categoryDialogFragment = AddCategoryDialogFragment.newInstance("Add Category");
                categoryDialogFragment.setCallback(categoryModel -> categoryDialogFragment.dismiss());
                categoryDialogFragment.show(fm, AddCategoryDialogFragment.class.getName());
            } else {
                showAlert("Attention", "You need to be premium user to add more categories", "Ok", null, null);
            }


        } else if (v.getId() == R.id.fab) {
            binding.fab.setClickable(false);
            ((MainActivity) getActivity()).openAddExpenseScreen(null);
            new Handler().postDelayed(() -> binding.fab.setClickable(true), 500);
        }

    }

    public void updateChartView(long startTime, long endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        viewModel.loadExpenseChart(startTime, endTime);
    }

    @Override
    public Context getContext() {
        return getActivity();
    }
}

