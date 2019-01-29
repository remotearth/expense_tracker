package com.remotearthsolutions.expensetracker.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.contracts.ExpenseFragmentContract;
import com.remotearthsolutions.expensetracker.databaseutils.DatabaseClient;
import com.remotearthsolutions.expensetracker.databaseutils.daos.ExpenseDao;
import com.remotearthsolutions.expensetracker.databaseutils.models.AccountModel;
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel;
import com.remotearthsolutions.expensetracker.databaseutils.models.ExpenseModel;
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.AccountIncome;
import com.remotearthsolutions.expensetracker.utils.DateTimeUtils;
import com.remotearthsolutions.expensetracker.utils.NumpadManager;
import com.remotearthsolutions.expensetracker.utils.SharedPreferenceUtils;
import com.remotearthsolutions.expensetracker.viewmodels.ExpenseFragmentViewModel;
import org.parceler.Parcels;

import java.util.Calendar;

public class ExpenseFragment extends Fragment implements ExpenseFragmentContract.View {

    private ImageView calenderBtnIv, categoryBtnIv, accountBtnIv, deleteBtn, okBtn;
    private TextView dateTv, categoryNameTv, accountNameTv;
    private LinearLayout selectAccount, selectCategory;
    private EditText expenseEdtxt;

    private ExpenseFragmentViewModel viewModel;

    private CategoryModel selectedCategory;
    private AccountIncome selectedSourceAccount;

    public ExpenseFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_expense, container, false);

        expenseEdtxt = view.findViewById(R.id.inputdigit);
        deleteBtn = view.findViewById(R.id.deleteBtn);
        categoryBtnIv = view.findViewById(R.id.showcatimage);
        categoryNameTv = view.findViewById(R.id.showcatname);
        accountNameTv = view.findViewById(R.id.accountNameTv);
        accountBtnIv = view.findViewById(R.id.accountImageIv);
        calenderBtnIv = view.findViewById(R.id.selectdate);
        selectAccount = view.findViewById(R.id.fromaccountselection);
        selectCategory = view.findViewById(R.id.categorylayout);
        dateTv = view.findViewById(R.id.dateTv);
        okBtn = view.findViewById(R.id.okBtn);

        NumpadFragment numpadFragment = (NumpadFragment) getChildFragmentManager().findFragmentById(R.id.numpadContainer);
        NumpadManager numpadManager = new NumpadManager();
        numpadManager.attachDisplay(expenseEdtxt);
        numpadManager.attachDeleteButton(deleteBtn);
        numpadFragment.setListener(numpadManager);

        Bundle args = getArguments();
        if (args != null) {
            selectedCategory = Parcels.unwrap(args.getParcelable("category_parcel"));
            //categoryBtnIv.setImageResource(category.getIcon());
            categoryBtnIv.setImageResource(R.drawable.ic_bills);
            categoryNameTv.setText(selectedCategory.getName());
        }

        ExpenseDao expenseDao = DatabaseClient.getInstance(getActivity()).getAppDatabase().expenseDao();
        viewModel = new ExpenseFragmentViewModel(this,expenseDao);
        viewModel.init();


        return view;
    }

    @Override
    public void defineClickListener() {

        selectAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    }
                });
                accountDialogFragment.show(fm, AccountDialogFragment.class.getName());

            }
        });

        selectCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm = getChildFragmentManager();
                final CategoryDialogFragment categoryDialogFragment = CategoryDialogFragment.newInstance("Select Category");
                categoryDialogFragment.setCallback(new CategoryDialogFragment.Callback() {
                    @Override
                    public void onSelectCategory(CategoryModel category) {
                        //categoryBtnIv.setImageResource(category.getIcon());
                        accountBtnIv.setImageResource(R.drawable.ic_currency);
                        categoryNameTv.setText(category.getName());
                        categoryDialogFragment.dismiss();

                        selectedCategory = category;
                    }
                });
                categoryDialogFragment.show(fm, CategoryDialogFragment.class.getName());
            }
        });

        dateTv.setText(DateTimeUtils.getCurrentDate(DateTimeUtils.dd_MM_yyyy));

        calenderBtnIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm = getChildFragmentManager();
                final DatePickerDialogFragment datePickerDialogFragment = DatePickerDialogFragment.newInstance("");

                Calendar cal = DateTimeUtils.getCalendarFromDateString(DateTimeUtils.dd_MM_yyyy, dateTv.getText().toString());
                datePickerDialogFragment.setInitialDate(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH), cal.get(Calendar.YEAR));

                datePickerDialogFragment.setCallback(new DatePickerDialogFragment.Callback() {
                    @Override
                    public void onSelectDate(String date) {
                        dateTv.setText(date);
                        datePickerDialogFragment.dismiss();
                    }
                });
                datePickerDialogFragment.show(fm, DatePickerDialogFragment.class.getName());
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String expenseStr = expenseEdtxt.getText().toString();
                double amount = expenseStr.length() > 0 ? Double.parseDouble(expenseStr) : 0;


                ExpenseModel expenseModel = new ExpenseModel();
                expenseModel.setAmount(amount);
                expenseModel.setDatetime(DateTimeUtils.getTimeInMillisFromDateStr(dateTv.getText().toString(), DateTimeUtils.dd_MM_yyyy));
                expenseModel.setCategoryId(selectCategory.getId());
                expenseModel.setSource(selectedSourceAccount.getAccount_id());
                SharedPreferenceUtils.getInstance(getContext()).getInt(SharedPreferenceUtils.KEY_SELECTED_ACCOUNT_ID,1);
                viewModel.addExpense(expenseModel);
            }
        });
    }

    @Override
    public void onExpenseAdded() {

    }
}
