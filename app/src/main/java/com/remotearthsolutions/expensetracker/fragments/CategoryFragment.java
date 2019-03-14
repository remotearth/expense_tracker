package com.remotearthsolutions.expensetracker.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.activities.ApplicationObject;
import com.remotearthsolutions.expensetracker.adapters.CategoryListViewAdapter;
import com.remotearthsolutions.expensetracker.contracts.CategoryFragmentContract;
import com.remotearthsolutions.expensetracker.databaseutils.DatabaseClient;
import com.remotearthsolutions.expensetracker.databaseutils.daos.CategoryDao;
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel;
import com.remotearthsolutions.expensetracker.viewmodels.CategoryViewModel;

import java.util.List;

public class CategoryFragment extends BaseFragment implements CategoryFragmentContract.View, OptionBottomSheetFragment.Callback {

    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private CategoryListViewAdapter adapter;
    private CategoryViewModel viewModel;
    private CategoryModel selectedCategory;
    private int limitOfCategory;


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

        viewModel.getNumberOfItem().observe(getViewLifecycleOwner(), (Integer count) -> limitOfCategory = count);

        floatingActionButton.setOnClickListener(v -> {
            if (limitOfCategory < 20 ||
                    ((ApplicationObject) getActivity().getApplication()).isPremium()) {
                selectedCategory = null;
                onClickEditBtn();
            } else {
                showAlert(getString(R.string.attention), getString(R.string.you_need_to_be_premium_user_to_add_more_categories), getString(R.string.ok), null, null);
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
        //THis is not need for category. need to refactor this somehow so this method will not be needed to implement here.
    }

    @Override
    public void onClickEditBtn() {
        FragmentManager fm = getChildFragmentManager();
        final AddCategoryDialogFragment categoryDialogFragment = AddCategoryDialogFragment.newInstance(getString(R.string.update_category));
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
            showToast(getString(R.string.you_cannot_delete_this_category));
            return;
        }
        showAlert(getString(R.string.warning), getString(R.string.deleting_this_category_will_remove_expenses_related_to_this_also_are_you_sure_you_want_to_delete), getString(R.string.yes), getString(R.string.not_now), new Callback() {
            @Override
            public void onOkBtnPressed() {
                viewModel.deleteCategory(selectedCategory);
                Toast.makeText(getActivity(), getString(R.string.category_deleted_successfully), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelBtnPressed() {

            }
        });
    }

    @Override
    public Context getContext() {
        return getActivity();
    }
}
