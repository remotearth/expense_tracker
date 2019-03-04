package com.remotearthsolutions.expensetracker.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.remotearthsolutions.expensetracker.R;

public class OptionBottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    private OptionBottomSheetFragment.Callback callback;
    private OptionBottomSheetFragment.OptionsFor optionsFor;

    public void setCallback(Callback callback, OptionBottomSheetFragment.OptionsFor optionsFor) {
        this.callback = callback;
        this.optionsFor = optionsFor;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_optionbottomsheet, container, false);
        LinearLayout addAmountBtn = view.findViewById(R.id.addAmountBtn);
        addAmountBtn.setOnClickListener(this);
        view.findViewById(R.id.editBtn).setOnClickListener(this);
        view.findViewById(R.id.deleteBtn).setOnClickListener(this);

        switch (optionsFor){
            case CATEGORY:
                addAmountBtn.setVisibility(View.GONE);
                break;
            case ACCOUNT:
                addAmountBtn.setVisibility(View.VISIBLE);
                break;
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        if (callback == null)
            return;

        switch (v.getId()) {
            case R.id.addAmountBtn:
                callback.onClickAddAmountBtn();
                break;
            case R.id.editBtn:
                callback.onClickEditBtn();
                break;
            case R.id.deleteBtn:
                callback.onClickDeleteBtn();
                break;
        }

        dismiss();
    }

    public interface Callback {
        void onClickAddAmountBtn();

        void onClickEditBtn();

        void onClickDeleteBtn();
    }

    public enum OptionsFor{
        CATEGORY,ACCOUNT
    }
}
