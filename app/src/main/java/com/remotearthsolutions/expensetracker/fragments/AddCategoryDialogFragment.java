package com.remotearthsolutions.expensetracker.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.adapters.AccountListAdapter;
import com.remotearthsolutions.expensetracker.adapters.IconListAdapter;
import com.remotearthsolutions.expensetracker.entities.Account;
import com.remotearthsolutions.expensetracker.entities.Icon;

import java.util.ArrayList;
import java.util.List;

public class AddCategoryDialogFragment extends DialogFragment {


    public AddCategoryDialogFragment() {

    }

    private IconListAdapter iconListAdapter;
    private List<Icon> alliconList;
    private RecyclerView recyclerView;
    private AddCategoryDialogFragment.Callback callback;

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

        recyclerView = view.findViewById(R.id.accountrecyclearView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        loadicon();
        iconListAdapter = new IconListAdapter(alliconList);
        iconListAdapter.setOnItemClickListener(new IconListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Icon icon) {

                callback.onSelectIcon(icon);
            }
        });
        recyclerView.setAdapter(iconListAdapter);

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
        void onSelectIcon(Icon icon);
    }
}