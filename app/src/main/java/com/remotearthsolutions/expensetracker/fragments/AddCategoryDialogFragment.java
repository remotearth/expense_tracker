package com.remotearthsolutions.expensetracker.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.remotearthsolutions.expensetracker.entities.Icon;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;

public class AddCategoryDialogFragment extends DialogFragment {

    public AddCategoryDialogFragment() {
    }

    private IconListAdapter iconListAdapter;
    private List<Icon> alliconList;
    private RecyclerView recyclerView;
    private AddCategoryDialogFragment.Callback callback;
    private EditText nameeditText;
    private Button addbutton;

    public static AddCategoryDialogFragment newInstance(String title) {
        AddCategoryDialogFragment frag = new AddCategoryDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    public void setCallback(AddCategoryDialogFragment.Callback callback){
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

        nameeditText = view.findViewById(R.id.addnametodb);
        addbutton = view.findViewById(R.id.addtodb);

        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Call db operation method here
                saveCategory();
            }
        });

        recyclerView = view.findViewById(R.id.accountrecyclearView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        loadicon();
        iconListAdapter = new IconListAdapter(alliconList);
        iconListAdapter.setOnItemClickListener(new IconListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Icon icon, int position) {

                //----- task to do----------//
            }
        });
        recyclerView.setAdapter(iconListAdapter);

    }

    private void saveCategory() {

        final String categoryName = nameeditText.getText().toString().trim();

        if (categoryName.isEmpty()) {
            nameeditText.setError("Plz Enter Category Name");
            nameeditText.requestFocus();
            return;
        }

        CategoryDao categoryDao = DatabaseClient.getInstance(getContext()).getAppDatabase().categoryDao();

//        CompositeDisposable disposable = new CompositeDisposable();
//        disposable.add(categoryDao.addCategory(categoryModel))
        CategoryModel categoryModel = new CategoryModel();
        categoryModel.setName(categoryName);

        Completable.fromAction(() -> categoryDao.addCategory(categoryModel)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> callback.onCategoryAdded(categoryModel));

    }

    public void loadicon() {

        alliconList = new ArrayList<>();
        alliconList.add(new Icon(R.drawable.ic_currency));
        alliconList.add(new Icon(R.drawable.ic_currency));
        alliconList.add(new Icon(R.drawable.ic_currency));
        alliconList.add(new Icon(R.drawable.ic_currency));
        alliconList.add(new Icon(R.drawable.ic_currency));
        alliconList.add(new Icon(R.drawable.ic_currency));
        alliconList.add(new Icon(R.drawable.ic_currency));
        alliconList.add(new Icon(R.drawable.ic_currency));
        alliconList.add(new Icon(R.drawable.ic_currency));
        alliconList.add(new Icon(R.drawable.ic_currency));
        alliconList.add(new Icon(R.drawable.ic_currency));
    }

    public interface Callback {
        void onCategoryAdded(CategoryModel categoryModel);
    }


}
