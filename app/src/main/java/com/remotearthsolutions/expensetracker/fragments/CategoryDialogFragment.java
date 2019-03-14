package com.remotearthsolutions.expensetracker.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.adapters.CategoryListAdapter;
import com.remotearthsolutions.expensetracker.contracts.CategoryFragmentContract;
import com.remotearthsolutions.expensetracker.databaseutils.DatabaseClient;
import com.remotearthsolutions.expensetracker.databaseutils.daos.CategoryDao;
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel;
import com.remotearthsolutions.expensetracker.utils.Constants;
import com.remotearthsolutions.expensetracker.viewmodels.CategoryViewModel;
import com.remotearthsolutions.expensetracker.viewmodels.viewmodel_factory.CategoryViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class CategoryDialogFragment extends DialogFragment implements CategoryFragmentContract.View {

    private CategoryViewModel viewModel;
    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private CategoryListAdapter categoryListAdapter;
    private CategoryDialogFragment.Callback callback;
    private int selectedCategoryId;

    public CategoryDialogFragment() {
    }

    public static CategoryDialogFragment newInstance(String title) {
        CategoryDialogFragment frag = new CategoryDialogFragment();
        Bundle args = new Bundle();
        args.putString(Constants.KEY_TITLE, title);
        frag.setArguments(args);
        return frag;
    }

    public void setCallback(CategoryDialogFragment.Callback callback) {
        this.callback = callback;
    }

    public void setCategory(int categoryId) {
        this.selectedCategoryId = categoryId;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_category, container);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CategoryDao categoryDao = DatabaseClient.getInstance(getContext()).getAppDatabase().categoryDao();
        viewModel = ViewModelProviders.of(this, new CategoryViewModelFactory(this, categoryDao)).get(CategoryViewModel.class);

        recyclerView = view.findViewById(R.id.categoryrecyclearView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(layoutManager);
        categoryListAdapter = new CategoryListAdapter(new ArrayList<>());
        recyclerView.setAdapter(categoryListAdapter);
        viewModel.showCategories();
    }

    @Override
    public void showCategories(List<CategoryModel> categories) {

        categoryListAdapter = new CategoryListAdapter(categories, selectedCategoryId);
        categoryListAdapter.setOnItemClickListener(new CategoryListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CategoryModel category) {
                callback.onSelectCategory(category);
            }
        });

        recyclerView.setAdapter(categoryListAdapter);
        layoutManager.scrollToPosition(getPositionOfSelectedItem(categories, selectedCategoryId));
    }

    public int getPositionOfSelectedItem(List<CategoryModel> categories, int categoryId) {
        for (CategoryModel category : categories) {
            if (category.getId() == categoryId) {
                return categories.indexOf(category);
            }
        }

        return 0;
    }

    public interface Callback {
        void onSelectCategory(CategoryModel category);
    }
}
