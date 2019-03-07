package com.remotearthsolutions.expensetracker.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.activities.MainActivity;
import com.remotearthsolutions.expensetracker.adapters.ExpenseListAdapter;
import com.remotearthsolutions.expensetracker.contracts.ExpenseFragmentContract;
import com.remotearthsolutions.expensetracker.databaseutils.DatabaseClient;
import com.remotearthsolutions.expensetracker.databaseutils.daos.ExpenseDao;
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel;
import com.remotearthsolutions.expensetracker.databaseutils.models.DateModel;
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.CategoryExpense;
import com.remotearthsolutions.expensetracker.utils.Utils;
import com.remotearthsolutions.expensetracker.viewmodels.ExpenseViewModel;

import java.util.List;

public class AllExpenseFragment extends Fragment implements ExpenseFragmentContract.ExpenseView {

    private RecyclerView recyclerView;
    private ExpenseListAdapter adapter;
    private ExpenseViewModel viewModel;
    private String currencySymbol;

    public AllExpenseFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_all_expense, container, false);
        recyclerView = view.findViewById(R.id.expenserecyclearView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);

        currencySymbol = "$";
        if (getActivity() != null) {
            currencySymbol = Utils.getCurrency(getActivity());
        }

        ExpenseDao expenseDao = DatabaseClient.getInstance(getContext()).getAppDatabase().expenseDao();
        viewModel = new ExpenseViewModel(this, expenseDao);
        return view;
    }

    @Override
    public void loadFilterExpense(List<CategoryExpense> listOffilterExpense) {
        adapter = new ExpenseListAdapter(listOffilterExpense, currencySymbol);
        adapter.setOnItemClickListener(categoryExpense -> {

            CategoryModel categoryModel = new CategoryModel();
            categoryModel.setId(categoryExpense.getCategory_id());
            categoryModel.setName(categoryExpense.getCategory_name());
            categoryModel.setIcon(categoryExpense.getIcon_name());
            ((MainActivity)getActivity()).openAddExpenseScreen(categoryModel);

        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void loadDate(List<DateModel> listOfDate) {

    }

    public void updateFilterListWithDate(long startTime, long endTime, int btnId) {
        viewModel.loadFilterExpense(startTime, endTime, btnId);
    }

}
