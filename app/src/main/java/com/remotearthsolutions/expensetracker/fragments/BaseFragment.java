package com.remotearthsolutions.expensetracker.fragments;

import android.content.Context;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.remotearthsolutions.expensetracker.contracts.BaseView;
import com.remotearthsolutions.expensetracker.utils.AlertDialogUtils;

public abstract class BaseFragment extends Fragment implements BaseView {

    @Override
    public void showAlert(String title, String message, String btnOk, String btnCancel, Callback callback) {
        AlertDialogUtils.show(getContext(),title,message,btnOk,btnCancel,callback);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
    }

    public abstract Context getContext();
}
