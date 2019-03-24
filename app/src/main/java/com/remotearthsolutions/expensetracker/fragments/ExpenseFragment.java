package com.remotearthsolutions.expensetracker.fragments;

import android.app.Activity;
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
import com.remotearthsolutions.expensetracker.activities.MainActivity;
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
    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

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
        if (context != null) {
            currencySymbol = Utils.getCurrency(context);
        }
        expenseEdtxt.setHint(currencySymbol + getString(R.string.initially_zero));

        NumpadFragment numpadFragment = (NumpadFragment) getChildFragmentManager().findFragmentById(R.id.numpadContainer);
        NumpadManager numpadManager = new NumpadManager();
        numpadManager.attachDisplay(expenseEdtxt);
        numpadManager.attachDeleteButton(deleteBtn);
        numpadFragment.setListener(numpadManager);

        AppDatabase db = DatabaseClient.getInstance(context).getAppDatabase();
        viewModel = ViewModelProviders.of(this, new ExpenseFragmentViewModelFactory(context, this, db.expenseDao(), db.accountDao(), db.categoryDao()))
                .get(ExpenseFragmentViewModel.class);
        viewModel.init();

        Bundle args = getArguments();
        if (args != null) {
            categoryExpense = Parcels.unwrap(args.getParcelable(Constants.CATEGORYEXPENSE_PARCEL));

            if (categoryExpense != null) {
                categoryBtnIv.setImageResource(CategoryIcons.getIconId(categoryExpense.getCategoryIcon()));
                categoryNameTv.setText(categoryExpense.getCategoryName());
                if (categoryExpense.getTotalAmount() > 0) {
                    expenseEdtxt.setText(Double.toString(categoryExpense.getTotalAmount()));
                }
                expenseNoteEdtxt.setText(categoryExpense.getNote());
                if (categoryExpense.getDatetime() > 0) {
                    dateTv.setText(DateTimeUtils.getDate(categoryExpense.getDatetime(), DateTimeUtils.dd_MM_yyyy));
                }
            } else {
                viewModel.setDefaultCategory();
            }

            if (categoryExpense != null && categoryExpense.getAccountIcon() != null) {
                accountBtnIv.setImageResource(CategoryIcons.getIconId(categoryExpense.getAccountIcon()));
                accountNameTv.setText(categoryExpense.getAccountName());
            } else {
                int accountId = SharedPreferenceUtils.getInstance(context).getInt(Constants.KEY_SELECTED_ACCOUNT_ID, 1);
                viewModel.setDefaultSourceAccount(accountId);
            }

        }

        return view;
    }

    @Override
    public void defineClickListener() {

        selectAccountBtn.setOnClickListener(v -> {
            FragmentManager fm = getChildFragmentManager();
            final AccountDialogFragment accountDialogFragment = AccountDialogFragment.newInstance(getString(R.string.select_account));
            accountDialogFragment.setCallback(account -> {

                categoryExpense.setAccount(account);
                accountBtnIv.setImageResource(CategoryIcons.getIconId(account.getIcon()));
                accountNameTv.setText(account.getName());
                accountDialogFragment.dismiss();

                SharedPreferenceUtils.getInstance(context).putInt(Constants.KEY_SELECTED_ACCOUNT_ID, account.getId());
            });
            accountDialogFragment.show(fm, AccountDialogFragment.class.getName());

        });

        selectCategoryBtn.setOnClickListener(v -> {

            FragmentManager fm = getChildFragmentManager();
            final CategoryDialogFragment categoryDialogFragment = CategoryDialogFragment.newInstance(getString(R.string.select_category));
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
            if (expenseStr.equals(getString(R.string.point))) {
                expenseStr = "";
            }
            double amount = expenseStr.length() > 0 ? Double.parseDouble(expenseStr) : 0;

            ExpenseModel expenseModel = new ExpenseModel();
            if (categoryExpense.getExpenseId() > 0) {
                expenseModel.setId(categoryExpense.getExpenseId());
            }
            expenseModel.setAmount(amount);
            expenseModel.setDatetime(DateTimeUtils.getTimeInMillisFromDateStr(dateTv.getText().toString()
                    + " " + DateTimeUtils.getCurrentTime(), DateTimeUtils.dd_MM_yyyy_h_mm));
            expenseModel.setCategoryId(categoryExpense.getCategoryId());
            expenseModel.setSource(categoryExpense.getAccountId());
            expenseModel.setNote(expenseNoteEdtxt.getText().toString());
            viewModel.addExpense(expenseModel);
        });

        expenseNoteEdtxt.setOnClickListener(v -> {

            AlertDialog builder = new AlertDialog.Builder(context).create();
            View dialogView = getLayoutInflater().inflate(R.layout.view_add_note, null);
            final EditText noteEdtxt = dialogView.findViewById(R.id.noteEdtxt);
            String note = categoryExpense.getNote();
            if(note!=null){
                noteEdtxt.setText(categoryExpense.getNote());
                noteEdtxt.setSelection(categoryExpense.getNote().length());
            }
            dialogView.findViewById(R.id.okBtn).setOnClickListener(v1 -> {
                String str = noteEdtxt.getText().toString();
                categoryExpense.setNote(str);
                expenseNoteEdtxt.setText(str != null ? str : "");
                builder.dismiss();
            });
            builder.setView(dialogView);
            builder.show();
        });

        expenseDeleteBtn.setOnClickListener(v -> {
            if ((categoryExpense.getExpenseId() > 0)) {

                showAlert(getString(R.string.attention),
                        getString(R.string.are_you_sure_you_want_to_delete_this_expense_entry),
                        getString(R.string.yes),
                        getString(R.string.not_now),
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
                ((Activity) context).onBackPressed();
            }
        });
    }

    @Override
    public void onExpenseAdded(double amount) {
        expenseEdtxt.setText("");
        Toast.makeText(getActivity(), getString(R.string.successfully_added), Toast.LENGTH_SHORT).show();
        viewModel.updateAccountAmount(categoryExpense.getAccount_id(), amount);
        MainActivity mainActivity = (MainActivity) context;
        mainActivity.updateSummary();
        mainActivity.refreshChart();
    }

    @Override
    public void onExpenseDeleted(CategoryExpense categoryExpense) {
        Toast.makeText(getActivity(), getString(R.string.successfully_deleted_expense_entry), Toast.LENGTH_SHORT).show();
        ((Activity) context).onBackPressed();
        viewModel.updateAccountAmount(this.categoryExpense.getAccount_id(), categoryExpense.getTotal_amount() * -1);
        MainActivity mainActivity = (MainActivity) context;
        mainActivity.updateSummary();
    }

    @Override
    public void setSourceAccount(AccountModel account) {
        if (categoryExpense == null) {
            categoryExpense = new CategoryExpense();
        }

        categoryExpense.setAccount(account);
        accountBtnIv.setImageResource(CategoryIcons.getIconId(account.getIcon()));
        accountNameTv.setText(account.getName());
        SharedPreferenceUtils.getInstance(context).putInt(Constants.KEY_SELECTED_ACCOUNT_ID, account.getId());
    }

    @Override
    public void showDefaultCategory(CategoryModel categoryModel) {
        if (categoryExpense == null) {
            categoryExpense = new CategoryExpense();
        }

        categoryExpense.setCategory(categoryModel);
        categoryBtnIv.setImageResource(CategoryIcons.getIconId(categoryModel.getIcon()));
        categoryNameTv.setText(categoryModel.getName());
    }

    @Override
    public Context getContext() {
        return context;
    }
}
