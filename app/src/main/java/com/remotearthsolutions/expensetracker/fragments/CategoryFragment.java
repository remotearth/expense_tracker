package com.remotearthsolutions.expensetracker.fragments;

import android.content.DialogInterface;
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
import com.remotearthsolutions.expensetracker.contracts.CategoryFragmentContract;
import com.remotearthsolutions.expensetracker.databaseutils.DatabaseClient;
import com.remotearthsolutions.expensetracker.databaseutils.daos.CategoryDao;
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel;
import com.remotearthsolutions.expensetracker.viewmodels.CategoryViewModel;

import java.util.List;

public class CategoryFragment extends Fragment implements CategoryFragmentContract.View {

    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private CategoryListViewAdapter adapter;
    private CategoryViewModel viewModel;


    public CategoryFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_fragment, container, false);

        floatingActionButton = view.findViewById(R.id.addcategory);
        recyclerView = view.findViewById(R.id.cat_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        CategoryDao categoryDao = DatabaseClient.getInstance(getContext()).getAppDatabase().categoryDao();
        viewModel = new CategoryViewModel(this, categoryDao);
        viewModel.showCategories();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm = getChildFragmentManager();
                final AddCategoryDialogFragment categoryDialogFragment = AddCategoryDialogFragment.newInstance("Add Category");
                categoryDialogFragment.setCallback(new AddCategoryDialogFragment.Callback() {
                    @Override
                    public void onCategoryAdded(CategoryModel categoryModel) {
                        viewModel.showCategories();
                        categoryDialogFragment.dismiss();

                    }
                });
                categoryDialogFragment.show(fm, AddCategoryDialogFragment.class.getName());
            }
        });

        return view;
    }

    @Override
    public void showCategories(List<CategoryModel> categories) {

        adapter = new CategoryListViewAdapter(categories);
        adapter.setOnItemClickListener(new CategoryListViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CategoryModel categoryModel) {

                FragmentManager fm = getChildFragmentManager();
                final AddCategoryDialogFragment categoryDialogFragment = AddCategoryDialogFragment.newInstance("Update Category");
                categoryDialogFragment.setCategory(categoryModel);
                categoryDialogFragment.setCallback(new AddCategoryDialogFragment.Callback() {
                    @Override
                    public void onCategoryAdded(CategoryModel categoryModel) {
                        viewModel.showCategories();
                        categoryDialogFragment.dismiss();

                    }
                });
                categoryDialogFragment.show(fm, AddCategoryDialogFragment.class.getName());
            }

            @Override
            public void onItemLongClick(CategoryModel categoryModel) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setTitle("Warning");
                alertDialogBuilder.setIcon(R.drawable.ic_warning);
                alertDialogBuilder.setMessage("Are you sure,You want to Delete?");
                alertDialogBuilder.setCancelable(false);

                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {

                        viewModel.deleteCategory(categoryModel);
                        Toast.makeText(getActivity(), "Category Deleted Successfully", Toast.LENGTH_LONG).show();
                        dialog.dismiss();

                    }
                });

                alertDialogBuilder.setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });
        recyclerView.setAdapter(adapter);

    }
}
