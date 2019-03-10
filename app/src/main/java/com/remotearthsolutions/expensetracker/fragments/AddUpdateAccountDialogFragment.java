package com.remotearthsolutions.expensetracker.fragments;

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
import com.remotearthsolutions.expensetracker.databaseutils.daos.AccountDao;
import com.remotearthsolutions.expensetracker.databaseutils.models.AccountModel;
import com.remotearthsolutions.expensetracker.utils.CategoryIcons;
import com.remotearthsolutions.expensetracker.utils.Utils;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import java.util.List;

public class AddUpdateAccountDialogFragment extends DialogFragment {

    private RecyclerView recyclerView;
    private AccountModel accountModel;
    private EditText nameEdtxt;
    private TextView headerTv;
    private Button okBtn;
    private String selectedIcon;
    private IconListAdapter iconListAdapter;

    public void setAccountModel(AccountModel accountModel) {
        this.accountModel = accountModel;
        if (accountModel != null) {
            selectedIcon = accountModel.getIcon();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_update_category_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        headerTv = view.findViewById(R.id.header);
        nameEdtxt = view.findViewById(R.id.nameEdtxt);
        okBtn = view.findViewById(R.id.okBtn);

        if (accountModel != null) {
            headerTv.setText("Update Account");
            okBtn.setText("Update");
            nameEdtxt.setText(accountModel.getName());
            nameEdtxt.setSelection(nameEdtxt.getText().length());
        } else {
            headerTv.setText("Add Account");
            okBtn.setText("Add");
        }

        recyclerView = view.findViewById(R.id.accountrecyclearView);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                (Utils.getDeviceScreenSize(getActivity()).height / 2));
        recyclerView.setLayoutParams(params);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 4);
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

        okBtn.setOnClickListener(v -> saveAccount());

    }

    private void saveAccount() {

        final String accountName = nameEdtxt.getText().toString().trim();

        if (accountName.isEmpty()) {
            nameEdtxt.setError("Enter a name for account");
            nameEdtxt.requestFocus();
            return;
        }

        if (selectedIcon == null || selectedIcon.isEmpty()) {
            Toast.makeText(getActivity(), "Select an icon for the account", Toast.LENGTH_SHORT).show();
            return;
        }

        AccountDao accountDao = DatabaseClient.getInstance(getContext()).getAppDatabase().accountDao();

        if (accountModel == null) {
            accountModel = new AccountModel();
        }
        accountModel.setName(accountName);
        accountModel.setIcon(selectedIcon);

        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(Completable.fromAction(() -> {
            if (accountModel.getId() > 0) {
                accountDao.updateAccount(accountModel);
            } else {
                accountDao.addAccount(accountModel);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {

                }));

        dismiss();
    }
}
