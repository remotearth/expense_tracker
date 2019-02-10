package com.remotearthsolutions.expensetracker.activities;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.remotearthsolutions.expensetracker.contracts.BaseView;
import com.remotearthsolutions.expensetracker.utils.AlertDialogUtils;

public abstract class BaseActivity extends AppCompatActivity implements BaseView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void showAlert(String title, String message, String btnOk, String btnCancel, BaseView.Callback callback) {
        AlertDialogUtils.show(getContext(),title,message,btnOk,btnCancel,callback);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
    }

    public abstract Context getContext();
}
