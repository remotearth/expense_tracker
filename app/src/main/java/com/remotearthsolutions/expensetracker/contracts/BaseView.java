package com.remotearthsolutions.expensetracker.contracts;

public interface BaseView {
    void showAlert(String title,String message,String btnOk,String btnCancel,Callback callback);


    interface Callback{
        void onOkBtnPressed();
        void onCancelBtnPressed();
    }
}
