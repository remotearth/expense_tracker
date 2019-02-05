package com.remotearthsolutions.expensetracker.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
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
import com.remotearthsolutions.expensetracker.utils.DateTimeUtils;
import com.remotearthsolutions.expensetracker.viewmodels.HomeFragmentViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class HomeFragment extends Fragment implements ChartManagerImpl.ChartView, HomeFragmentContract.View, View.OnClickListener {

    private CategoryListAdapter adapter;
//    private AnimatedPieView mAnimatedPieView;
//    private RecyclerView recyclerView;
    private HomeFragmentViewModel viewModel;
//    private ImageView addCategory, nextDate, previousDate;
//    private TextView showdate;
//    private Button dailyButton, weeklyButton, monthlyButton, yearlyButton;
    private int cDay, cMonth, cYear;
    private FragmentHomeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false);
        View view = binding.getRoot();

        binding.addCategoryBtn.setOnClickListener(this);
        binding.nextDateBtn.setOnClickListener(this);
        binding.previousDateBtn.setOnClickListener(this);
        binding.dailyRangeBtn.setOnClickListener(this);
        binding.weeklyRangeBtn.setOnClickListener(this);
        binding.monthlyRangeBtn.setOnClickListener(this);
        binding.yearlyRangeBtn.setOnClickListener(this);
        binding.fab.setOnClickListener(this);


        //mAnimatedPieView = view.findViewById(R.id.chartView);
        binding.recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        binding.recyclerView.setLayoutManager(llm);

        CategoryDao categoryDao = DatabaseClient.getInstance(getContext()).getAppDatabase().categoryDao();
        AccountDao accountDao = DatabaseClient.getInstance(getContext()).getAppDatabase().accountDao();
        viewModel = new HomeFragmentViewModel(this, categoryDao, accountDao);
        viewModel.init();
        viewModel.loadExpenseChart();

        binding.navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        binding.dateTv.setText(DateTimeUtils.getCurrentDate(DateTimeUtils.dd_MM_yyyy));

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

            SimpleDateFormat sdf = new SimpleDateFormat(DateTimeUtils.dd_MM_yyyy);
            try {

                Date getCurrentDate = sdf.parse(DateTimeUtils.getCurrentDate(DateTimeUtils.dd_MM_yyyy));
                Date getNextDate = sdf.parse(DateTimeUtils.getDate(DateTimeUtils.dd_MM_yyyy, +1));

                if (getNextDate.compareTo(getCurrentDate) > 0) {
                    binding.dateTv.setText(DateTimeUtils.getCurrentDate(DateTimeUtils.dd_MM_yyyy));
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

        } else if (v.getId() == R.id.previousDateBtn) {

            String previousdate = DateTimeUtils.getDate(DateTimeUtils.dd_MM_yyyy, -1);
            binding.dateTv.setText(previousdate);
        } else if (v.getId() == R.id.dailyRangeBtn) {

            DatePicker datePicker = new DatePicker(getActivity());
            cDay = datePicker.getDayOfMonth();
            cMonth = datePicker.getMonth() + 1;
            cYear = datePicker.getYear();


            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                    binding.dateTv.setText(dayOfMonth + "/" + month + "/" + year);

                }
            }, cYear, cMonth, cDay);
            datePickerDialog.show();
        } else if (v.getId() == R.id.weeklyRangeBtn) {


        } else if (v.getId() == R.id.monthlyRangeBtn) {
            DatePicker datePicker = new DatePicker(getActivity());
            cDay = datePicker.getDayOfMonth();
            cMonth = datePicker.getMonth() + 1;
            cYear = datePicker.getYear();


            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                    binding.dateTv.setText(month + "/" + year);

                }
            }, cYear, cMonth, cDay);
            datePickerDialog.show();

        } else if (v.getId() == R.id.yearlyRangeBtn) {
            DatePicker datePicker = new DatePicker(getActivity());
            cDay = datePicker.getDayOfMonth();
            cMonth = datePicker.getMonth() + 1;
            cYear = datePicker.getYear();

            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                    binding.dateTv.setText(year);

                }
            }, cYear, cMonth, cDay);
            datePickerDialog.show();

        }
        else if(v.getId() == R.id.fab){
            CategoryModel categoryModel =  viewModel.getFirstCategory();
            ((MainActivity) getActivity()).openAddExpenseScreen(categoryModel);
        }
    }
}
