package com.remotearthsolutions.expensetracker.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.adapters.CategoryListViewAdapter;
import com.remotearthsolutions.expensetracker.contracts.BaseView;
import com.remotearthsolutions.expensetracker.contracts.CategoryFragmentContract;
import com.remotearthsolutions.expensetracker.databaseutils.DatabaseClient;
import com.remotearthsolutions.expensetracker.databaseutils.daos.CategoryDao;
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel;
import com.remotearthsolutions.expensetracker.utils.AlertDialogUtils;
import com.remotearthsolutions.expensetracker.viewmodels.CategoryViewModel;

import java.util.List;

public class CategoryFragment extends Fragment implements CategoryFragmentContract.View, OptionBottomSheetFragment.Callback {

    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private CategoryListViewAdapter adapter;
    private CategoryViewModel viewModel;
    private CategoryModel selectedCategory;

    private Integer limitOfCategory;


    public CategoryFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        floatingActionButton = view.findViewById(R.id.addcategory);
        recyclerView = view.findViewById(R.id.cat_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        CategoryDao categoryDao = DatabaseClient.getInstance(getContext()).getAppDatabase().categoryDao();
        viewModel = new CategoryViewModel(this, categoryDao);
        viewModel.showCategories();

        viewModel.getNumberOfItem().observe(this, (Integer integer) -> limitOfCategory = integer);

        floatingActionButton.setOnClickListener(v -> {
            if (limitOfCategory <= 20) {
                selectedCategory = null;
                onClickEditBtn();
            } else {
                AlertDialogUtils.show(getContext(), "Attention", "You have to be premium user", "Ok", null, new BaseView.Callback() {
                    @Override
                    public void onOkBtnPressed() {

                    }

                    @Override
                    public void onCancelBtnPressed() {

                    }
                });
            }
        });



        return view;
    }

    @Override
    public void showCategories(List<CategoryModel> categories) {

        adapter = new CategoryListViewAdapter(categories);
        adapter.setOnItemClickListener(categoryModel -> {

            selectedCategory = categoryModel;
            OptionBottomSheetFragment optionBottomSheetFragment = new OptionBottomSheetFragment();
            optionBottomSheetFragment.setCallback(CategoryFragment.this, OptionBottomSheetFragment.OptionsFor.CATEGORY);
            optionBottomSheetFragment.show(getChildFragmentManager(), OptionBottomSheetFragment.class.getName());

        });
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onClickAddAmountBtn() {
        //THis is not neeed for category. need to refactor this somehow so this method will not be need to imlpement here.
    }

    @Override
    public void onClickEditBtn() {
        FragmentManager fm = getChildFragmentManager();
        final AddCategoryDialogFragment categoryDialogFragment = AddCategoryDialogFragment.newInstance("Update Category");
        categoryDialogFragment.setCategory(selectedCategory);
        categoryDialogFragment.setCallback(categoryModel1 -> {
            //viewModel.showCategories();
            categoryDialogFragment.dismiss();

        });
        categoryDialogFragment.show(fm, AddCategoryDialogFragment.class.getName());
    }

    @Override
    public void onClickDeleteBtn() {

        if (selectedCategory.getNotremovable() == 1) {
            Toast.makeText(getActivity(), "You cannot delete this expense category", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Warning");
        alertDialogBuilder.setIcon(R.drawable.ic_warning);
        alertDialogBuilder.setMessage("Are you sure,You want to Delete?");
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton("Yes", (dialog, arg1) -> {

            viewModel.deleteCategory(selectedCategory);
            Toast.makeText(getActivity(), "Category Deleted Successfully", Toast.LENGTH_LONG).show();
            dialog.dismiss();

        });
        alertDialogBuilder.setNegativeButton("Not Now", (dialog, which) -> dialog.dismiss());
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
}
