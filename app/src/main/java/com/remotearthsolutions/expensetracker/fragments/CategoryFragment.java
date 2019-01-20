package com.remotearthsolutions.expensetracker.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.adapters.CategoryListAdapter;
import com.remotearthsolutions.expensetracker.entities.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends DialogFragment {

    public CategoryFragment() {
    }

    private View v;
    private RecyclerView recyclerView;
    private CategoryListAdapter categoryListAdapter;
    private List<Category> categoryList;


    public static CategoryFragment newInstance(String title) {
        CategoryFragment frag = new CategoryFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.add_category, container);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.categoryrecyclearView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        loadcategory();
        categoryListAdapter = new CategoryListAdapter(categoryList);
        recyclerView.setAdapter(categoryListAdapter);

        categoryListAdapter.setOnItemClickListener(new CategoryListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Category category, int position) {

                int Image = categoryList.get(position).getCategoryImage();
                String Name = categoryList.get(position).getCategoryName();

                Bundle bundle = new Bundle();
                bundle.putInt("image",Image);
                bundle.putString("name",Name);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                ExpenseFragment expenseFragment = new ExpenseFragment();
                expenseFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.framelayout,expenseFragment);
                fragmentTransaction.commit();


            }
        });



    }

    private void loadcategory() {

        categoryList = new ArrayList<>();
        categoryList.add(new Category(R.drawable.ic_food, "Food"));
        categoryList.add(new Category(R.drawable.ic_gift, "Gift"));
        categoryList.add(new Category(R.drawable.ic_bills, "Bills"));
        categoryList.add(new Category(R.drawable.ic_taxi, "Taxi"));
        categoryList.add(new Category(R.drawable.ic_delivery_truck, "Transport"));

    }
}
