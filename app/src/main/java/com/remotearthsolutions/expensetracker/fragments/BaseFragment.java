package com.remotearthsolutions.expensetracker.fragments;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.remotearthsolutions.expensetracker.contracts.BaseView;
import com.remotearthsolutions.expensetracker.services.InternetCheckerService;
import com.remotearthsolutions.expensetracker.services.InternetCheckerServiceImpl;
import com.remotearthsolutions.expensetracker.services.ProgressService;
import com.remotearthsolutions.expensetracker.services.ProgressServiceImp;
import com.remotearthsolutions.expensetracker.utils.AlertDialogUtils;

public abstract class BaseFragment extends Fragment implements BaseView {

    private InternetCheckerService internetCheckerService;
    private ProgressService progressService;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        internetCheckerService = new InternetCheckerServiceImpl(getContext());
        progressService = new ProgressServiceImp(getContext());
    }

    @Override
    public void showAlert(String title, String message, String btnOk, String btnCancel, Callback callback) {
        AlertDialogUtils.show(getContext(),title,message,btnOk,btnCancel,callback);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean isDeviceOnline() {
        return internetCheckerService.isConnected();
    }

    @Override
    public void showProgress(String message) {
        progressService.showProgressBar(message);
    }

    @Override
    public void hideProgress() {
        progressService.hideProgressBar();
    }

    public abstract Context getContext();
}
