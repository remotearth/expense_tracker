package com.remotearthsolutions.expensetracker.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.remotearthsolutions.expensetracker.R;

public class CategoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;


    public CategoryFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_fragment, container, false);

        recyclerView = view.findViewById(R.id.cat_recycler);
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


}
