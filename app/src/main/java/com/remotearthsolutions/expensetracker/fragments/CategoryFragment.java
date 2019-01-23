package com.remotearthsolutions.expensetracker.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.adapters.CategoryListViewAdapter;
import com.remotearthsolutions.expensetracker.databaseutils.CategoryModel;
import com.remotearthsolutions.expensetracker.databaseutils.DatabaseClient;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class CategoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private CategoryListViewAdapter adapter;
    private List<CategoryModel> showcategorylist;


    public CategoryFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_fragment, container, false);

        recyclerView = view.findViewById(R.id.cat_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        getList();


        floatingActionButton = view.findViewById(R.id.addcategory);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm = getChildFragmentManager();
                final AddCategoryDialogFragment categoryDialogFragment = AddCategoryDialogFragment.newInstance("Add Category");
                categoryDialogFragment.show(fm, AddCategoryDialogFragment.class.getName());
            }
        });

        return view;
    }

    private void getList() {


        class GetCategoryList extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                showcategorylist = DatabaseClient.getInstance(getActivity()).getAppDatabase().categoryDao().getCategory();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(),"SHOWING DATA", Toast.LENGTH_LONG).show();
                CategoryListViewAdapter adapter = new CategoryListViewAdapter(showcategorylist,getActivity());
                recyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(new CategoryListViewAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(CategoryModel categoryModel) {

                        Toast.makeText(getActivity(),"Clicked on"+categoryModel.getName(), Toast.LENGTH_SHORT).show();


                    }
                });

            }
        }

        GetCategoryList getCategoryList = new GetCategoryList();
        getCategoryList.execute();
    }


}
