package com.remotearthsolutions.expensetracker.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.activities.ApplicationObject;
import com.remotearthsolutions.expensetracker.adapters.AccountsAdapter;
import com.remotearthsolutions.expensetracker.contracts.AccountContract;
import com.remotearthsolutions.expensetracker.contracts.BaseView;
import com.remotearthsolutions.expensetracker.databaseutils.DatabaseClient;
import com.remotearthsolutions.expensetracker.databaseutils.daos.AccountDao;
import com.remotearthsolutions.expensetracker.databaseutils.models.AccountModel;
import com.remotearthsolutions.expensetracker.utils.AlertDialogUtils;
import com.remotearthsolutions.expensetracker.utils.Utils;
import com.remotearthsolutions.expensetracker.viewmodels.AccountViewModel;
import com.remotearthsolutions.expensetracker.viewmodels.viewmodel_factory.AccountViewModelFactory;

import java.util.List;

public class AccountsFragment extends BaseFragment implements AccountContract.View, OptionBottomSheetFragment.Callback {

    private AccountViewModel viewModel;
    private ListView listview;
    private AccountsAdapter adapter;
    private AccountModel selectAccountModel;
    private int limitOfAccount;
    private String currencySymbol;
    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public AccountsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_accounts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listview = view.findViewById(R.id.accountList);

        currencySymbol = "$";
        if (context != null) {
            currencySymbol = Utils.getCurrency(context);
        }

        AccountDao accountDao = DatabaseClient.getInstance(getContext()).getAppDatabase().accountDao();
        this.viewModel = ViewModelProviders.of(this,
                new AccountViewModelFactory(this, accountDao)).
                get(AccountViewModel.class);
        this.viewModel.loadAccounts();

        viewModel.getNumberOfItem().observe(this,
                (Integer count) -> limitOfAccount = count);

        view.findViewById(R.id.addAccountBtn).setOnClickListener(v -> {

            if (limitOfAccount < 5 ||
                    ((ApplicationObject) ((Activity) context).getApplication()).isPremium()) {
                selectAccountModel = null;
                onClickEditBtn();
            } else {
                showAlert("Attention", "You need to be premium user to add more Account", "Ok", null, null);
            }
        });
    }

    @Override
    public void onAccountFetch(List<AccountModel> accounts) {
        if (isAdded()) {
            adapter = new AccountsAdapter(context, accounts, currencySymbol);
            listview.setAdapter(adapter);
            listview.setOnItemClickListener((parent, view, position, id) -> {
                this.selectAccountModel = accounts.get(position);
                OptionBottomSheetFragment optionBottomSheetFragment = new OptionBottomSheetFragment();
                optionBottomSheetFragment.setCallback(AccountsFragment.this, OptionBottomSheetFragment.OptionsFor.ACCOUNT);
                optionBottomSheetFragment.show(getChildFragmentManager(), OptionBottomSheetFragment.class.getName());
            });
        }
    }

    @Override
    public void onSuccess(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickAddAmountBtn() {
        AddAccountAmountDialogFragment addAccountAmountDialogFragment = new AddAccountAmountDialogFragment();
        addAccountAmountDialogFragment.setAccountIncome(selectAccountModel);
        addAccountAmountDialogFragment.setCallback(accountIncome -> {
            viewModel.addOrUpdateAccount(accountIncome);
            addAccountAmountDialogFragment.dismiss();
        });
        addAccountAmountDialogFragment.show(getChildFragmentManager(), AddAccountAmountDialogFragment.class.getName());
    }

    @Override
    public void onClickEditBtn() {
        AddUpdateAccountDialogFragment dialogFragment = new AddUpdateAccountDialogFragment();
        dialogFragment.setAccountModel(selectAccountModel);
        dialogFragment.show(getChildFragmentManager(), AddUpdateAccountDialogFragment.class.getName());

    }

    @Override
    public void onClickDeleteBtn() {
        if (selectAccountModel.getNotremovable() == 1) {
            Toast.makeText(context, "You cannot delete this account", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialogUtils.show(context, "Warning",
                "Deleting this account will remove expenses related to this also. Are you sure, You want to Delete?",
                "Yes",
                "Not now",
                new BaseView.Callback() {
                    @Override
                    public void onOkBtnPressed() {
                        viewModel.deleteAccount(selectAccountModel);
                    }

                    @Override
                    public void onCancelBtnPressed() {

                    }
                });
    }

    @Override
    public Context getContext() {
        return context;
    }
}
