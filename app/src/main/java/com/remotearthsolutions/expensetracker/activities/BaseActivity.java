package com.remotearthsolutions.expensetracker.activities;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.remotearthsolutions.expensetracker.contracts.BaseView;
import com.remotearthsolutions.expensetracker.services.InternetCheckerService;
import com.remotearthsolutions.expensetracker.services.InternetCheckerServiceImpl;
import com.remotearthsolutions.expensetracker.services.ProgressService;
import com.remotearthsolutions.expensetracker.services.ProgressServiceImp;
import com.remotearthsolutions.expensetracker.utils.AlertDialogUtils;

public abstract class BaseActivity extends AppCompatActivity implements BaseView {

    private InternetCheckerService internetCheckerService;
    private ProgressService progressService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        internetCheckerService = new InternetCheckerServiceImpl(this);
        progressService = new ProgressServiceImp(this);
    }

    @Override
    public void showAlert(String title, String message, String btnOk, String btnCancel, BaseView.Callback callback) {
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

    public abstract Context getContext();

    @Override
    public void showProgress(String message) {
        progressService.showProgressBar(message);
    }

    @Override
    public void hideProgress() {
        progressService.hideProgressBar();
    }
}
