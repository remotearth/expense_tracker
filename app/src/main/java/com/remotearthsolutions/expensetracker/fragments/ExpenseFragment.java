package com.remotearthsolutions.expensetracker.fragments;

import android.os.Bundle;
import android.os.Handler;
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
import com.remotearthsolutions.expensetracker.entities.Account;
import com.remotearthsolutions.expensetracker.entities.Category;
import com.remotearthsolutions.expensetracker.utils.DateTimeUtils;
import com.remotearthsolutions.expensetracker.utils.NumpadManager;
import com.wunderlist.slidinglayer.SlidingLayer;
import com.wunderlist.slidinglayer.transformer.SlideJoyTransformer;
import org.parceler.Parcels;

import java.util.Calendar;

public class ExpenseFragment extends Fragment {

    public ExpenseFragment() {
    }

    private Handler handler;
    private ImageView calenderBtnIv, categoryBtnIv, accountBtnIv;
    private TextView selectedDateTv, categoryNameTv, accountNameTv;
    private SlidingLayer mSlidingLayer;
    private LinearLayout selectAccount, selectCategory;
    private EditText inputdigit;
    private ImageView deleteBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_expense, container, false);

        inputdigit = view.findViewById(R.id.inputdigit);
        deleteBtn = view.findViewById(R.id.deleteBtn);

        NumpadFragment numpadFragment = (NumpadFragment) getChildFragmentManager().findFragmentById(R.id.numpadContainer);
        NumpadManager numpadManager = new NumpadManager();
        numpadManager.attachDisplay(inputdigit);
        numpadManager.attachDeleteButton(deleteBtn);
        numpadFragment.setListener(numpadManager);

        categoryBtnIv = view.findViewById(R.id.showcatimage);
        categoryNameTv = view.findViewById(R.id.showcatname);
        accountNameTv = view.findViewById(R.id.accountNameTv);
        accountBtnIv = view.findViewById(R.id.accountImageIv);
        calenderBtnIv = view.findViewById(R.id.selectdata);
        selectAccount = view.findViewById(R.id.fromaccountselection);
        selectCategory = view.findViewById(R.id.categorylayout);
        mSlidingLayer = view.findViewById(R.id.slidingDrawer);
        selectedDateTv = view.findViewById(R.id.ShowDate);

        Bundle args = getArguments();
        if (args != null) {

            Category category = Parcels.unwrap(args.getParcelable("category_parcel"));
            categoryBtnIv.setImageResource(category.getCategoryImage());
            categoryNameTv.setText(category.getCategoryName());
        }

        selectAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getChildFragmentManager();
                final AccountDialogFragment accountDialogFragment = AccountDialogFragment.newInstance("Select Account");
                accountDialogFragment.setCallback(new AccountDialogFragment.Callback() {
                    @Override
                    public void onSelectAccount(Account account) {
                        accountBtnIv.setImageResource(account.getAccountImage());
                        accountNameTv.setText(account.getAccountName());
                        accountDialogFragment.dismiss();
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
                    public void onSelectCategory(Category category) {
                        categoryBtnIv.setImageResource(category.getCategoryImage());
                        categoryNameTv.setText(category.getCategoryName());
                        categoryDialogFragment.dismiss();
                    }
                });
                categoryDialogFragment.show(fm, CategoryDialogFragment.class.getName());
            }
        });

        mSlidingLayer.setLayerTransformer(new SlideJoyTransformer());
        selectedDateTv.setText(DateTimeUtils.getCurrentDate(DateTimeUtils.dd_MM_yyyy));

        calenderBtnIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm = getChildFragmentManager();
                final DatePickerDialogFragment datePickerDialogFragment = DatePickerDialogFragment.newInstance("");

                Calendar cal = DateTimeUtils.getCalendarFromDateString(DateTimeUtils.dd_MM_yyyy, selectedDateTv.getText().toString());
                datePickerDialogFragment.setInitialDate(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH), cal.get(Calendar.YEAR));

                datePickerDialogFragment.setCallback(new DatePickerDialogFragment.Callback() {
                    @Override
                    public void onSelectDate(String date) {
                        selectedDateTv.setText(date);
                        datePickerDialogFragment.dismiss();
                    }
                });
                datePickerDialogFragment.show(fm, DatePickerDialogFragment.class.getName());
            }
        });

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toggleDrawer();
            }
        }, 50);

        return view;
    }

    public boolean isDrawerOpened() {
        if (mSlidingLayer == null) return false;
        return mSlidingLayer.isOpened();
    }

    public void toggleDrawer() {
        if (mSlidingLayer == null) return;

        if (mSlidingLayer.isOpened()) {
            mSlidingLayer.closeLayer(true);
        } else {
            mSlidingLayer.openLayer(true);
        }
    }
}
