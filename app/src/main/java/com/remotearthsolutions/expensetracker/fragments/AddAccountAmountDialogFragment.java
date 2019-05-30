package com.remotearthsolutions.expensetracker.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.databaseutils.models.AccountModel;
import com.remotearthsolutions.expensetracker.utils.CategoryIcons;

public class AddAccountAmountDialogFragment extends DialogFragment {

    private AddAccountAmountDialogFragment.Callback callback;
    private AccountModel accountIncome;
    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void setAccountIncome(AccountModel accountIncome) {
        this.accountIncome = accountIncome;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_addaccountamount, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView accountNameTv = view.findViewById(R.id.accountNameTv);
        ImageView accountImageIv = view.findViewById(R.id.accountImageIv);
        final EditText amountEdtxt = view.findViewById(R.id.amountEdtxt);

        if (accountIncome != null) {
            accountNameTv.setText(accountIncome.getName());
            accountImageIv.setImageResource(CategoryIcons.getIconId(accountIncome.getIcon()));
            amountEdtxt.setText(Double.toString(accountIncome.getAmount()));
            amountEdtxt.setSelection(amountEdtxt.getText().toString().length());
        }

        view.findViewById(R.id.okBtn).setOnClickListener(v -> {
            String amount = amountEdtxt.getText().toString();
            if (amount == null || amount.length() == 0) {
                Toast.makeText(getActivity(), getString(R.string.you_have_to_enter_an_amount), Toast.LENGTH_LONG).show();
                return;
            }

            double accountAmount = Double.parseDouble(amount);
            accountIncome.setAmount(accountAmount);
            if (callback != null) {
                callback.onAmountAdded(accountIncome);
            }
        });


    }

    public interface Callback {
        void onAmountAdded(AccountModel accountIncome);
    }
}
