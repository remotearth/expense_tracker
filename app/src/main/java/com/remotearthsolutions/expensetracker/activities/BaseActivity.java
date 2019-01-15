package com.remotearthsolutions.expensetracker.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.remotearthsolutions.expensetracker.contracts.BaseView;

public class BaseActivity extends AppCompatActivity implements BaseView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void showAlert(String title, String message, String btnOk, String btnCancel, Callback callback) {

    }
}
