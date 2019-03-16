package com.remotearthsolutions.expensetracker.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.contracts.ExpenseFragmentContract;
import com.remotearthsolutions.expensetracker.databaseutils.AppDatabase;
import com.remotearthsolutions.expensetracker.databaseutils.DatabaseClient;
import com.remotearthsolutions.expensetracker.databaseutils.models.AccountModel;
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel;
import com.remotearthsolutions.expensetracker.databaseutils.models.ExpenseModel;
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.CategoryExpense;
import com.remotearthsolutions.expensetracker.utils.*;
import com.remotearthsolutions.expensetracker.viewmodels.ExpenseFragmentViewModel;
import com.remotearthsolutions.expensetracker.viewmodels.viewmodel_factory.ExpenseFragmentViewModelFactory;
import org.parceler.Parcels;

import java.util.Calendar;

public class ExpenseFragment extends BaseFragment implements ExpenseFragmentContract.View {

    private ImageView calenderBtnIv, categoryBtnIv, accountBtnIv, deleteBtn, okBtn, expenseDeleteBtn;
    private TextView dateTv, categoryNameTv, accountNameTv;
    private RelativeLayout selectAccountBtn, selectCategoryBtn;
    private EditText expenseEdtxt, expenseNoteEdtxt;

    private ExpenseFragmentViewModel viewModel;
    private CategoryExpense categoryExpense;

    public ExpenseFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_expense, container, false);

        expenseEdtxt = view.findViewById(R.id.inputdigit);
        expenseNoteEdtxt = view.findViewById(R.id.expenseNoteEdtxt);
        deleteBtn = view.findViewById(R.id.deleteBtn);
        categoryBtnIv = view.findViewById(R.id.showcatimage);
        categoryNameTv = view.findViewById(R.id.showcatname);
        accountNameTv = view.findViewById(R.id.accountNameTv);
        accountBtnIv = view.findViewById(R.id.accountImageIv);
        calenderBtnIv = view.findViewById(R.id.selectdate);
        selectAccountBtn = view.findViewById(R.id.fromAccountBtn);
        selectCategoryBtn = view.findViewById(R.id.toCategoryBtn);
        expenseDeleteBtn = view.findViewById(R.id.expenseDeleteBtn);
        dateTv = view.findViewById(R.id.dateTv);
        okBtn = view.findViewById(R.id.okBtn);

        String currencySymbol = "$";
        if (getActivity() != null) {
            currencySymbol = Utils.getCurrency(getActivity());
        }
        expenseEdtxt.setHint(currencySymbol + " 0");

        NumpadFragment numpadFragment = (NumpadFragment) getChildFragmentManager().findFragmentById(R.id.numpadContainer);
        NumpadManager numpadManager = new NumpadManager();
        numpadManager.attachDisplay(expenseEdtxt);
        numpadManager.attachDeleteButton(deleteBtn);
        numpadFragment.setListener(numpadManager);

        AppDatabase db = DatabaseClient.getInstance(getActivity()).getAppDatabase();
        viewModel = ViewModelProviders.of(this, new ExpenseFragmentViewModelFactory(this, db.expenseDao(), db.accountDao(), db.categoryDao()))
                .get(ExpenseFragmentViewModel.class);
        viewModel.init();

        Bundle args = getArguments();
        if (args != null) {
            categoryExpense = Parcels.unwrap(args.getParcelable(Constants.CATEGORYEXPENSE_PARCEL));

            if (categoryExpense != null) {
                categoryBtnIv.setImageResource(CategoryIcons.getIconId(categoryExpense.getCategory_icon()));
                categoryNameTv.setText(categoryExpense.getCategory_name());
                if (categoryExpense.getTotal_amount() > 0) {
                    expenseEdtxt.setText(Double.toString(categoryExpense.getTotal_amount()));
                }
                expenseNoteEdtxt.setText(categoryExpense.getNote());
                if (categoryExpense.getDatetime() > 0) {
                    dateTv.setText(DateTimeUtils.getDate(categoryExpense.getDatetime(), DateTimeUtils.dd_MM_yyyy));
                }
            } else {
                viewModel.setDefaultCategory();
            }

            if (categoryExpense != null && categoryExpense.getAccount_icon() != null) {
                accountBtnIv.setImageResource(CategoryIcons.getIconId(categoryExpense.getAccount_icon()));
                accountNameTv.setText(categoryExpense.getAccount_name());
            } else {
                int accountId = SharedPreferenceUtils.getInstance(getActivity()).getInt(Constants.KEY_SELECTED_ACCOUNT_ID, 1);
                viewModel.setDefaultSourceAccount(accountId);
            }

        }

        return view;
    }

    @Override
    public void defineClickListener() {

        selectAccountBtn.setOnClickListener(v -> {
            FragmentManager fm = getChildFragmentManager();
            final AccountDialogFragment accountDialogFragment = AccountDialogFragment.newInstance("Select Account");
            accountDialogFragment.setCallback(account -> {

                categoryExpense.setAccount(account);
                accountBtnIv.setImageResource(CategoryIcons.getIconId(account.getIcon()));
                accountNameTv.setText(account.getName());
                accountDialogFragment.dismiss();

                SharedPreferenceUtils.getInstance(getActivity()).putInt(Constants.KEY_SELECTED_ACCOUNT_ID, account.getId());
            });
            accountDialogFragment.show(fm, AccountDialogFragment.class.getName());

        });

        selectCategoryBtn.setOnClickListener(v -> {

            FragmentManager fm = getChildFragmentManager();
            final CategoryDialogFragment categoryDialogFragment = CategoryDialogFragment.newInstance("Select Category");
            categoryDialogFragment.setCategory(categoryExpense.getCategory_id());
            categoryDialogFragment.setCallback(category -> {
                categoryBtnIv.setImageResource(CategoryIcons.getIconId(category.getIcon()));
                categoryNameTv.setText(category.getName());
                categoryDialogFragment.dismiss();

                categoryExpense.setCategory(category);
            });
            categoryDialogFragment.show(fm, CategoryDialogFragment.class.getName());
        });

        dateTv.setText(DateTimeUtils.getCurrentDate(DateTimeUtils.dd_MM_yyyy));

        calenderBtnIv.setOnClickListener(v -> {

            FragmentManager fm = getChildFragmentManager();
            final DatePickerDialogFragment datePickerDialogFragment = DatePickerDialogFragment.newInstance("");

            Calendar cal = DateTimeUtils.getCalendarFromDateString(DateTimeUtils.dd_MM_yyyy, dateTv.getText().toString());
            datePickerDialogFragment.setInitialDate(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH), cal.get(Calendar.YEAR));

            datePickerDialogFragment.setCallback(date -> {
                dateTv.setText(date);
                datePickerDialogFragment.dismiss();
            });
            datePickerDialogFragment.show(fm, DatePickerDialogFragment.class.getName());
        });

        okBtn.setOnClickListener(v -> {
            String expenseStr = expenseEdtxt.getText().toString();
            if (expenseStr.equals(".")) {
                expenseStr = "";
            }
            double amount = expenseStr.length() > 0 ? Double.parseDouble(expenseStr) : 0;

            ExpenseModel expenseModel = new ExpenseModel();
            if (categoryExpense.getExpense_id() > 0) {
                expenseModel.setId(categoryExpense.getExpense_id());
            }
            expenseModel.setAmount(amount);
            expenseModel.setDatetime(DateTimeUtils.getTimeInMillisFromDateStr(dateTv.getText().toString()
                    + " " + DateTimeUtils.getCurrentTime(), DateTimeUtils.dd_MM_yyyy_h_mm));
            expenseModel.setCategoryId(categoryExpense.getCategory_id());
            expenseModel.setSource(categoryExpense.getAccount_id());
            expenseModel.setNote(expenseNoteEdtxt.getText().toString());
            viewModel.addExpense(expenseModel);
        });

        expenseNoteEdtxt.setOnClickListener(v -> {

            AlertDialog builder = new AlertDialog.Builder(getActivity()).create();
            View dialogView = getLayoutInflater().inflate(R.layout.view_add_note, null);
            final EditText noteEdtxt = dialogView.findViewById(R.id.noteEdtxt);
            dialogView.findViewById(R.id.okBtn).setOnClickListener(v1 -> {
                String str = noteEdtxt.getText().toString();
                expenseNoteEdtxt.setText(str != null ? str : "");
                builder.dismiss();
            });
            builder.setView(dialogView);
            builder.show();
        });

        expenseDeleteBtn.setOnClickListener(v -> {
            if ((categoryExpense.getExpense_id() > 0)) {

                showAlert("Attention",
                        "Are you sure, you want to delete this expense entry",
                        "Yes",
                        "Not now",
                        new Callback() {
                            @Override
                            public void onOkBtnPressed() {
                                viewModel.deleteExpense(categoryExpense);
                            }

                            @Override
                            public void onCancelBtnPressed() {

                            }
                        });

            } else {
                getActivity().onBackPressed();
            }
        });
    }

    @Override
    public void onExpenseAdded(double amount) {
        expenseEdtxt.setText("");
        Toast.makeText(getActivity(), "Successfully added.", Toast.LENGTH_SHORT).show();
        viewModel.updateAccountAmount(categoryExpense.getAccount_id(), amount);
    }

    @Override
    public void onExpenseDeleted() {
        Toast.makeText(getActivity(), "Successfully deleted expense entry.", Toast.LENGTH_SHORT).show();
        getActivity().onBackPressed();
    }

    @Override
    public void setSourceAccount(AccountModel account) {
        if (categoryExpense == null)
            categoryExpense = new CategoryExpense();

        categoryExpense.setAccount(account);
        accountBtnIv.setImageResource(CategoryIcons.getIconId(account.getIcon()));
        accountNameTv.setText(account.getName());
        SharedPreferenceUtils.getInstance(getActivity()).putInt(Constants.KEY_SELECTED_ACCOUNT_ID, account.getId());
    }

    @Override
    public void showDefaultCategory(CategoryModel categoryModel) {
        if (categoryExpense == null)
            categoryExpense = new CategoryExpense();

        categoryExpense.setCategory(categoryModel);
        categoryBtnIv.setImageResource(CategoryIcons.getIconId(categoryModel.getIcon()));
        categoryNameTv.setText(categoryModel.getName());
    }

    @Override
    public Context getContext() {
        return getActivity();
    }
}
