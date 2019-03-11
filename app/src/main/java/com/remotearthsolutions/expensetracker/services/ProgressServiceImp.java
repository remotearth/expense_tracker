package com.remotearthsolutions.expensetracker.services;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgressServiceImp implements ProgressService {

    private Context mContext;
    private ProgressDialog mProgressDialog;

    public ProgressServiceImp(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void showProgressBar(String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage(message);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }

        mProgressDialog.show();

    }

    @Override
    public void hideProgressBar() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
