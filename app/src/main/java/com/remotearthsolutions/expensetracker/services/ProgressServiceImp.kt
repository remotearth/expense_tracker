package com.remotearthsolutions.expensetracker.services

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.remotearthsolutions.expensetracker.R
import com.wang.avi.AVLoadingIndicatorView

class ProgressServiceImp(private val mContext: Context) : ProgressService {
    private var mProgressDialog: Dialog? = null
    override fun showProgressBar(message: String?) {

        hideProgressBar()
        mProgressDialog = Dialog(mContext, R.style.ProgressViewTheme)
        val view: View =
            (mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
                .inflate(R.layout.progress_indicator, null)
        val progressView: AVLoadingIndicatorView = view.findViewById(R.id.progressView)
        val progressMessageTv = view.findViewById<TextView>(R.id.progressMessageTv)

        progressMessageTv.text = message
        progressView.show()

        mProgressDialog?.setContentView(view)
        mProgressDialog?.setCancelable(false)

        try {
            mProgressDialog?.show()
        } catch (e: Exception) {
            e.printStackTrace()
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }

    override fun hideProgressBar() {
        try {
            if (mProgressDialog != null) {
                mProgressDialog?.dismiss()
                mProgressDialog = null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            FirebaseCrashlytics.getInstance().recordException(e)
        }

        if (mProgressDialog != null && mProgressDialog!!.isShowing) {
            mProgressDialog!!.dismiss()
        }
    }
}
