package com.remotearthsolutions.expensetracker.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.remotearthsolutions.expensetracker.presenters.CategoryFragmentPresenter;

import java.util.List;

public class CategoryFragment extends Fragment implements CategoryFragmentContract.View {

    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private CategoryListViewAdapter adapter;
    private CategoryFragmentPresenter presenter;


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
        presenter = new CategoryFragmentPresenter(this, categoryDao);
        presenter.showCategories();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm = getChildFragmentManager();
                final AddCategoryDialogFragment categoryDialogFragment = AddCategoryDialogFragment.newInstance("Add Category");
                categoryDialogFragment.setCallback(new AddCategoryDialogFragment.Callback() {
                    @Override
                    public void onCategoryAdded(CategoryModel categoryModel) {
                        presenter.showCategories();
                        categoryDialogFragment.dismiss();

                    }
                });
                categoryDialogFragment.show(fm, AddCategoryDialogFragment.class.getName());
            }
        });


        return view;
    }



//    private void updateCategory(final String username) {
//
//        class UpdateTask extends AsyncTask<Void, Void, Void> {
//
//            @Override
//            protected Void doInBackground(Void... voids) {
//
//                CategoryModel categoryModel = new CategoryModel();
//                categoryModel.setName(username);
//                DatabaseClient.getInstance(getContext())
//                        .getAppDatabase()
//                        .categoryDao()
//                        .updateCategory(categoryModel);
//
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void aVoid) {
//                super.onPostExecute(aVoid);
//                Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_LONG).show();
//
//            }
//        }
//
//        UpdateTask ut = new UpdateTask();
//        ut.execute();
//    }


    @Override
    public void showCategories(List<CategoryModel> categories) {

        adapter = new CategoryListViewAdapter(categories);
        recyclerView.setAdapter(adapter);

    }
}
