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
import com.remotearthsolutions.expensetracker.viewmodels.CategoryViewModel;
import com.remotearthsolutions.expensetracker.viewmodels.viewmodel_factory.CategoryViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class CategoryDialogFragment extends DialogFragment implements CategoryFragmentContract.View {

    private CategoryViewModel viewModel;
    private RecyclerView recyclerView;
    private CategoryListAdapter categoryListAdapter;
    //private List<CategoryModel> categories;
    private CategoryDialogFragment.Callback callback;

    public CategoryDialogFragment() {
    }

    public static CategoryDialogFragment newInstance(String title) {
        CategoryDialogFragment frag = new CategoryDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    public void setCallback(CategoryDialogFragment.Callback callback) {
        this.callback = callback;
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
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        categoryListAdapter = new CategoryListAdapter(new ArrayList<>());
        recyclerView.setAdapter(categoryListAdapter);

        //if(categories!=null && categories.size()>0){
        viewModel.showCategories();
//        }
//        else{
//            showCategories(categories);
//        }
    }

    @Override
    public void showCategories(List<CategoryModel> categories) {

        categoryListAdapter = new CategoryListAdapter(categories);
        categoryListAdapter.setOnItemClickListener(new CategoryListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CategoryModel category) {
                callback.onSelectCategory(category);
            }
        });
        recyclerView.setAdapter(categoryListAdapter);
    }

    public interface Callback {
        void onSelectCategory(CategoryModel category);
    }
}
