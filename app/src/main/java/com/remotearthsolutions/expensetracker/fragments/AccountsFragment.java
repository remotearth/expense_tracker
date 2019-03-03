package com.remotearthsolutions.expensetracker.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.adapters.AccountsAdapter;
import com.remotearthsolutions.expensetracker.contracts.AccountContract;
import com.remotearthsolutions.expensetracker.contracts.BaseView;
import com.remotearthsolutions.expensetracker.databaseutils.DatabaseClient;
import com.remotearthsolutions.expensetracker.databaseutils.daos.AccountDao;
import com.remotearthsolutions.expensetracker.databaseutils.models.AccountModel;
import com.remotearthsolutions.expensetracker.utils.AlertDialogUtils;
import com.remotearthsolutions.expensetracker.viewmodels.AccountViewModel;
import com.remotearthsolutions.expensetracker.viewmodels.viewmodel_factory.AccountViewModelFactory;

import java.util.List;

public class AccountsFragment extends Fragment implements AccountContract.View, OptionBottomSheetFragment.Callback {

    private AccountViewModel viewModel;
    private ListView listview;
    private AccountsAdapter adapter;
    private AccountModel selectAccountModel;

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

        AccountDao accountDao = DatabaseClient.getInstance(getContext()).getAppDatabase().accountDao();
        this.viewModel = ViewModelProviders.of(this,
                new AccountViewModelFactory(this, accountDao)).
                get(AccountViewModel.class);
        this.viewModel.loadAccounts();

        view.findViewById(R.id.addAccountBtn).setOnClickListener(v -> {
            selectAccountModel = null;
            onClickEditBtn();
        });
    }

    @Override
    public void onAccountFetch(List<AccountModel> accounts) {
        if (isAdded()) {
            adapter = new AccountsAdapter(getActivity(), accounts);
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
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(), "You cannot delete this account", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialogUtils.show(getActivity(), "Warning", "Are you sure,You want to Delete?", "Yes", "No", new BaseView.Callback() {
            @Override
            public void onOkBtnPressed() {
                viewModel.deleteAccount(selectAccountModel);
            }

            @Override
            public void onCancelBtnPressed() {

            }
        });
    }
}
