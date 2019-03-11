package com.remotearthsolutions.expensetracker.contracts;

public interface BaseView {
    void showAlert(String title,String message,String btnOk,String btnCancel,BaseView.Callback callback);
    void showToast(String message);
    boolean isDeviceOnline();

    void showProgress(String message);
    void hideProgress();

    interface Callback{
        void onOkBtnPressed();
        void onCancelBtnPressed();
    }
}
