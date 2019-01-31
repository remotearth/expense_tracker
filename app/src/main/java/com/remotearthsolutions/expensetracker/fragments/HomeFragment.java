package com.remotearthsolutions.expensetracker.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
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

import java.util.List;


public class HomeFragment extends Fragment implements ChartManagerImpl.ChartView, HomeFragmentContract.View {

    private CategoryListAdapter adapter;
    private AnimatedPieView mAnimatedPieView;
    private RecyclerView recyclerView;
    private HomeFragmentViewModel viewModel;
    private ImageView addCategory,nextDate,previousDate;
    private TextView showdate;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        addCategory = view.findViewById(R.id.addcategoryinhome);
        showdate = view.findViewById(R.id.showdateinhome);
        nextDate = view.findViewById(R.id.nextdatebutton);
        previousDate = view.findViewById(R.id.previousdatebutton);


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

        addCategory.setOnClickListener(v -> {

            FragmentManager fm = getChildFragmentManager();
            final AddCategoryDialogFragment categoryDialogFragment = AddCategoryDialogFragment.newInstance("Add Category");
            categoryDialogFragment.setCallback(new AddCategoryDialogFragment.Callback() {
                @Override
                public void onCategoryAdded(CategoryModel categoryModel) {

                    categoryDialogFragment.dismiss();

                }
            });
            categoryDialogFragment.show(fm, AddCategoryDialogFragment.class.getName());


        });

        showdate.setText(DateTimeUtils.getCurrentDate(DateTimeUtils.dd_MM_yyyy));

        nextDate.setOnClickListener(v -> {

            

        });

        previousDate.setOnClickListener(v -> {

            String previousdate = DateTimeUtils.getDate(DateTimeUtils.dd_MM_yyyy, -1);
            showdate.setText(previousdate);

        });



        return view;
    }

    @Override
    public void loadChartConfig(AnimatedPieViewConfig config) {
        mAnimatedPieView.applyConfig(config).start();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
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
        }
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
}
