package com.remotearthsolutions.expensetracker.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.contracts.ExpenseFragmentContract;
import com.remotearthsolutions.expensetracker.databaseutils.DatabaseClient;
import com.remotearthsolutions.expensetracker.databaseutils.daos.AccountDao;
import com.remotearthsolutions.expensetracker.databaseutils.daos.CategoryDao;
import com.remotearthsolutions.expensetracker.databaseutils.daos.ExpenseDao;
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel;
import com.remotearthsolutions.expensetracker.databaseutils.models.ExpenseModel;
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.AccountIncome;
import com.remotearthsolutions.expensetracker.utils.*;
import com.remotearthsolutions.expensetracker.viewmodels.ExpenseFragmentViewModel;
import org.parceler.Parcels;

import java.util.Calendar;

public class ExpenseFragment extends BaseFragment implements ExpenseFragmentContract.View {

    private ImageView calenderBtnIv, categoryBtnIv, accountBtnIv, deleteBtn, okBtn;
    private TextView dateTv, categoryNameTv, accountNameTv;
    private RelativeLayout selectAccountBtn, selectCategoryBtn;
    private EditText expenseEdtxt, expenseNoteEdtxt;

    private ExpenseFragmentViewModel viewModel;

    private CategoryModel selectedCategory;
    private AccountIncome selectedSourceAccount;

    private Bundle args;

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
        dateTv = view.findViewById(R.id.dateTv);
        okBtn = view.findViewById(R.id.okBtn);

        NumpadFragment numpadFragment = (NumpadFragment) getChildFragmentManager().findFragmentById(R.id.numpadContainer);
        NumpadManager numpadManager = new NumpadManager();
        numpadManager.attachDisplay(expenseEdtxt);
        numpadManager.attachDeleteButton(deleteBtn);
        numpadFragment.setListener(numpadManager);

        ExpenseDao expenseDao = DatabaseClient.getInstance(getActivity()).getAppDatabase().expenseDao();
        AccountDao accountDao = DatabaseClient.getInstance(getActivity()).getAppDatabase().accountDao();
        CategoryDao categoryDao = DatabaseClient.getInstance(getActivity()).getAppDatabase().categoryDao();

        int accountId = SharedPreferenceUtils.getInstance(getActivity()).getInt(Constants.KEY_SELECTED_ACCOUNT_ID, 1);
        viewModel = new ExpenseFragmentViewModel(this, expenseDao, accountDao, categoryDao);
        viewModel.init(accountId);

        args = getArguments();
        if (args != null) {
            selectedCategory = Parcels.unwrap(args.getParcelable(Constants.CATEGORY_PARCEL));
            if (selectedCategory != null) {
                categoryBtnIv.setImageResource(CategoryIcons.getIconId(selectedCategory.getIcon()));
                categoryNameTv.setText(selectedCategory.getName());
            } else {
                viewModel.setDefaultCategory();
            }
        }

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


        return view;
    }

    @Override
    public void defineClickListener() {

        selectAccountBtn.setOnClickListener(v -> {
            FragmentManager fm = getChildFragmentManager();
            final AccountDialogFragment accountDialogFragment = AccountDialogFragment.newInstance("Select Account");
            accountDialogFragment.setCallback(new AccountDialogFragment.Callback() {
                @Override
                public void onSelectAccount(AccountIncome account) {
                    //accountBtnIv.setImageResource(account.getIcon_name());
                    accountBtnIv.setImageResource(R.drawable.ic_currency);
                    accountNameTv.setText(account.getAccount_name());
                    accountDialogFragment.dismiss();

                    selectedSourceAccount = account;
                    SharedPreferenceUtils.getInstance(getActivity()).putInt(Constants.KEY_SELECTED_ACCOUNT_ID, selectedSourceAccount.getAccount_id());
                }
            });
            accountDialogFragment.show(fm, AccountDialogFragment.class.getName());

        });

        selectCategoryBtn.setOnClickListener(v -> {

            FragmentManager fm = getChildFragmentManager();
            final CategoryDialogFragment categoryDialogFragment = CategoryDialogFragment.newInstance("Select Category");
            categoryDialogFragment.setCategory(selectedCategory.getId());
            categoryDialogFragment.setCallback(category -> {
                categoryBtnIv.setImageResource(CategoryIcons.getIconId(category.getIcon()));
                categoryNameTv.setText(category.getName());
                categoryDialogFragment.dismiss();

                selectedCategory = category;
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
            double amount = expenseStr.length() > 0 ? Double.parseDouble(expenseStr) : 0;


            ExpenseModel expenseModel = new ExpenseModel();
            expenseModel.setAmount(amount);
            expenseModel.setDatetime(DateTimeUtils.getTimeInMillisFromDateStr(dateTv.getText().toString(), DateTimeUtils.dd_MM_yyyy));
            expenseModel.setCategoryId(selectedCategory.getId());
            expenseModel.setSource(selectedSourceAccount.getAccount_id());
            viewModel.addExpense(expenseModel);
        });
    }

    @Override
    public void onExpenseAdded() {
        expenseEdtxt.setText("");
        Toast.makeText(getActivity(), "Successfully added.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setSourceAccount(AccountIncome account) {
        selectedSourceAccount = account;
        //accountBtnIv.setImageResource(account.getIcon_name());
        accountBtnIv.setImageResource(R.drawable.ic_currency);
        accountNameTv.setText(account.getAccount_name());
        SharedPreferenceUtils.getInstance(getActivity()).putInt(Constants.KEY_SELECTED_ACCOUNT_ID, account.getAccount_id());
    }

    @Override
    public void showDefaultCategory(CategoryModel categoryModel) {
        selectedCategory = categoryModel;
        categoryBtnIv.setImageResource(CategoryIcons.getIconId(categoryModel.getIcon()));
        categoryNameTv.setText(categoryModel.getName());
    }

    @Override
    public Context getContext() {
        return getActivity();
    }
}
