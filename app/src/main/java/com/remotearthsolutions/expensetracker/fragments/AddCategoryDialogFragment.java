package com.remotearthsolutions.expensetracker.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.adapters.IconListAdapter;
import com.remotearthsolutions.expensetracker.databaseutils.DatabaseClient;
import com.remotearthsolutions.expensetracker.databaseutils.daos.CategoryDao;
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel;
import com.remotearthsolutions.expensetracker.utils.CategoryIcons;
import com.remotearthsolutions.expensetracker.utils.Utils;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AddCategoryDialogFragment extends DialogFragment {


    private IconListAdapter iconListAdapter;
    private RecyclerView recyclerView;
    private AddCategoryDialogFragment.Callback callback;
    private EditText categoryNameEdtxt;
    private TextView categorydialogstatus;
    private CategoryModel categoryModel;
    private String selectedIcon;

    public AddCategoryDialogFragment() {
    }


    public static AddCategoryDialogFragment newInstance(String title) {
        AddCategoryDialogFragment frag = new AddCategoryDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    public void setCallback(AddCategoryDialogFragment.Callback callback) {
        this.callback = callback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_category_fragment, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        categoryNameEdtxt = view.findViewById(R.id.addnametodb);
        categorydialogstatus = view.findViewById(R.id.header);
        Button addBtn = view.findViewById(R.id.addtodb);

        if (categoryModel != null) {
            categoryNameEdtxt.setText(categoryModel.getName());
            categoryNameEdtxt.setSelection(categoryNameEdtxt.getText().length());
            categorydialogstatus.setText("Update Category");
            addBtn.setText("Update");
        }

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Call db operation method here
                saveCategory();
            }
        });

        recyclerView = view.findViewById(R.id.accountrecyclearView);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                (Utils.getDeviceScreenSize(getActivity()).height / 2));
        recyclerView.setLayoutParams(params);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),4);
        recyclerView.setLayoutManager(gridLayoutManager);

        Map<String, Integer> iconMap = CategoryIcons.getAllIcons();
        List<String> alliconList = new ArrayList<>(iconMap.keySet());

        iconListAdapter = new IconListAdapter(alliconList,gridLayoutManager);
        iconListAdapter.setSelectedIcon(selectedIcon != null ? selectedIcon : "");
        iconListAdapter.setOnItemClickListener(new IconListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String icon) {
                selectedIcon = icon;
                iconListAdapter.setSelectedIcon(selectedIcon != null ? selectedIcon : "");
                iconListAdapter.notifyDataSetChanged();
            }
        });
        recyclerView.setAdapter(iconListAdapter);

    }

    private void saveCategory() {

        final String categoryName = categoryNameEdtxt.getText().toString().trim();

        if (categoryName.isEmpty()) {
            categoryNameEdtxt.setError("Plz Enter Category Name");
            categoryNameEdtxt.requestFocus();
            return;
        }

        CategoryDao categoryDao = DatabaseClient.getInstance(getContext()).getAppDatabase().categoryDao();
        CategoryModel newCategoryModel = new CategoryModel();
        if (categoryModel != null) {
            newCategoryModel.setId(categoryModel.getId());
        }

        newCategoryModel.setName(categoryName);
        newCategoryModel.setIcon(selectedIcon);

        Completable.fromAction(() -> {
            if (categoryModel != null) {
                categoryDao.updateCategory(newCategoryModel);
            } else {
                categoryDao.addCategory(newCategoryModel);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> callback.onCategoryAdded(categoryModel));

    }

    public void setCategory(CategoryModel categoryModel) {
        this.categoryModel = categoryModel;
        selectedIcon = categoryModel.getIcon();
    }

    public interface Callback {
        void onCategoryAdded(CategoryModel categoryModel);
    }


}
