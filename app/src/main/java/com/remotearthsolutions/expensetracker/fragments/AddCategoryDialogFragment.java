package com.remotearthsolutions.expensetracker.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
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
import com.remotearthsolutions.expensetracker.utils.Constants;
import com.remotearthsolutions.expensetracker.utils.Utils;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import java.util.List;

public class AddCategoryDialogFragment extends DialogFragment {


    private IconListAdapter iconListAdapter;
    private RecyclerView recyclerView;
    private AddCategoryDialogFragment.Callback callback;
    private EditText categoryNameEdtxt;
    private TextView categorydialogstatus;
    private CategoryModel categoryModel;
    private String selectedIcon;
    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public AddCategoryDialogFragment() {
    }


    public static AddCategoryDialogFragment newInstance(String title) {
        AddCategoryDialogFragment frag = new AddCategoryDialogFragment();
        Bundle args = new Bundle();
        args.putString(Constants.KEY_TITLE, title);
        frag.setArguments(args);
        return frag;
    }

    public void setCallback(AddCategoryDialogFragment.Callback callback) {
        this.callback = callback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_update_category_account, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        categoryNameEdtxt = view.findViewById(R.id.nameEdtxt);
        categorydialogstatus = view.findViewById(R.id.header);
        Button okBtn = view.findViewById(R.id.okBtn);

        if (categoryModel != null) {
            categoryNameEdtxt.setText(categoryModel.getName());
            categoryNameEdtxt.setSelection(categoryNameEdtxt.getText().length());
            categorydialogstatus.setText(getString(R.string.update_category));
            okBtn.setText(getString(R.string.update));
        }

        okBtn.setOnClickListener(v -> saveCategory());

        recyclerView = view.findViewById(R.id.accountrecyclearView);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                (Utils.getDeviceScreenSize(context).height / 2));
        recyclerView.setLayoutParams(params);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 4);
        recyclerView.setLayoutManager(gridLayoutManager);

        List<String> alliconList = CategoryIcons.getAllIcons();
        iconListAdapter = new IconListAdapter(alliconList, gridLayoutManager);
        iconListAdapter.setSelectedIcon(selectedIcon != null ? selectedIcon : "");
        iconListAdapter.setOnItemClickListener(icon -> {
            selectedIcon = icon;
            iconListAdapter.setSelectedIcon(selectedIcon != null ? selectedIcon : "");
            iconListAdapter.notifyDataSetChanged();
        });
        recyclerView.setAdapter(iconListAdapter);

    }

    private void saveCategory() {

        final String categoryName = categoryNameEdtxt.getText().toString().trim();

        if (categoryName.isEmpty()) {
            categoryNameEdtxt.setError(getString(R.string.enter_a_name));
            categoryNameEdtxt.requestFocus();
            return;
        }

        if (selectedIcon == null || selectedIcon.isEmpty()) {
            Toast.makeText(getActivity(), getString(R.string.select_an_icon), Toast.LENGTH_SHORT).show();
            return;
        }

        CategoryDao categoryDao = DatabaseClient.getInstance(getContext()).getAppDatabase().categoryDao();

        if (categoryModel == null) {
            categoryModel = new CategoryModel();
        }
        categoryModel.setName(categoryName);
        categoryModel.setIcon(selectedIcon);

        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(Completable.fromAction(() -> {
            if (categoryModel.getId() > 0) {
                categoryDao.updateCategory(categoryModel);
            } else {
                categoryDao.addCategory(categoryModel);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> callback.onCategoryAdded(categoryModel)));

    }

    public void setCategory(CategoryModel categoryModel) {
        this.categoryModel = categoryModel;
        if (categoryModel != null) {
            selectedIcon = categoryModel.getIcon();
        }

    }

    public interface Callback {
        void onCategoryAdded(CategoryModel categoryModel);
    }


}
