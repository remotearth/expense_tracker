package com.remotearthsolutions.expensetracker.services

import android.app.ProgressDialog
import android.content.Context

class ProgressServiceImp(private val mContext: Context) : ProgressService {
    private var mProgressDialog: ProgressDialog? = null
    override fun showProgressBar(message: String?) {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog(mContext)
            mProgressDialog!!.setCancelable(false)
            mProgressDialog!!.setMessage(message)
            mProgressDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        }
        mProgressDialog!!.show()
    }

    override fun hideProgressBar() {
        if (mProgressDialog != null && mProgressDialog!!.isShowing) {
            mProgressDialog!!.dismiss()
        }
    }

}