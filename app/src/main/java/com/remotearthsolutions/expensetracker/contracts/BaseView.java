package com.remotearthsolutions.expensetracker.contracts;

public interface BaseView {
    void showAlert(String title,String message,String btnOk,String btnCancel,BaseView.Callback callback);
    void showToast(String message);

    interface Callback{
        void onOkBtnPressed();
        void onCancelBtnPressed();
    }
}
