package com.remotearthsolutions.expensetracker.utils;

import android.content.Context;
import androidx.appcompat.app.AlertDialog;
import com.remotearthsolutions.expensetracker.contracts.BaseView;

public class AlertDialogUtils {

    public static void show(Context context, String title, String message, String btnOk, String btnCancel, BaseView.Callback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setCancelable(false);
        builder.setMessage(message);
        builder.setPositiveButton(btnOk, (dialog, which) -> {
            if (callback != null) {
                callback.onOkBtnPressed();
            }
        });
        if (btnCancel != null) {
            builder.setNegativeButton(btnCancel, (dialog, which) -> {
                if (callback != null) {
                    callback.onCancelBtnPressed();
                }
            });
        }
        builder.create();
        builder.show();


    }
}
