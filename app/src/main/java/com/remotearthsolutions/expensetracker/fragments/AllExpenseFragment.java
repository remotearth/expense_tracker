package com.remotearthsolutions.expensetracker.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.activities.MainActivity;
import com.remotearthsolutions.expensetracker.adapters.ExpenseListAdapter;
import com.remotearthsolutions.expensetracker.contracts.ExpenseFragmentContract;
import com.remotearthsolutions.expensetracker.databaseutils.AppDatabase;
import com.remotearthsolutions.expensetracker.databaseutils.DatabaseClient;
import com.remotearthsolutions.expensetracker.databaseutils.models.DateModel;
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.CategoryExpense;
import com.remotearthsolutions.expensetracker.utils.Utils;
import com.remotearthsolutions.expensetracker.viewmodels.ExpenseViewModel;
import com.remotearthsolutions.expensetracker.viewmodels.viewmodel_factory.ExpenseViewModelFactory;

import java.util.List;

public class AllExpenseFragment extends Fragment implements ExpenseFragmentContract.ExpenseView {

    private RecyclerView recyclerView;
    private ExpenseListAdapter adapter;
    private ExpenseViewModel viewModel;
    private String currencySymbol;
    private ImageView noDataIV;
    private TextView noDataTV;

    public AllExpenseFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_all_expense, container, false);
        noDataIV = view.findViewById(R.id.errorimage);
        noDataTV = view.findViewById(R.id.errormessage);
        recyclerView = view.findViewById(R.id.expenserecyclearView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);

        currencySymbol = "$";
        if (getActivity() != null) {
            currencySymbol = Utils.getCurrency(getActivity());
        }

        AppDatabase db = DatabaseClient.getInstance(getContext()).getAppDatabase();
        viewModel = ViewModelProviders.of(this, new ExpenseViewModelFactory(this, db.expenseDao(), db.categoryExpenseDao())).get(ExpenseViewModel.class);

        return view;
    }

    @Override
    public void loadFilterExpense(List<CategoryExpense> listOffilterExpense) {

        adapter = new ExpenseListAdapter(listOffilterExpense, currencySymbol);
        adapter.setOnItemClickListener(categoryExpense -> ((MainActivity) getActivity()).openAddExpenseScreen(categoryExpense));
        if (listOffilterExpense.size() == 0) {
            recyclerView.setVisibility(View.GONE);
            noDataTV.setVisibility(View.VISIBLE);
            noDataIV.setVisibility(View.VISIBLE);
        }
        else
        {
            noDataTV.setVisibility(View.GONE);
            noDataIV.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(adapter);
        }

    }

    @Override
    public void loadDate(List<DateModel> listOfDate) {

    }

    public void updateFilterListWithDate(long startTime, long endTime, int btnId) {
        viewModel.loadFilterExpense(startTime, endTime, btnId);
    }

}
