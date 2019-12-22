package com.remotearthsolutions.expensetracker.utils

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import com.remotearthsolutions.expensetracker.contracts.BaseView

object AlertDialogUtils {
    fun show(
        context: Context?,
        title: String?,
        message: String?,
        btnOk: String?,
        btnCancel: String?,
        callback: BaseView.Callback?
    ) {

        AlertDialog.Builder(context!!).apply {
            setTitle(title)
            setCancelable(false)
            setMessage(message)
            setPositiveButton(
                btnOk
            ) { _: DialogInterface?, _: Int ->
                callback?.onOkBtnPressed()
            }
            if (btnCancel != null) {
                setNegativeButton(
                    btnCancel
                ) { _: DialogInterface?, _: Int ->
                    callback?.onCancelBtnPressed()
                }
            }
            create()
            show()
        }
    }
}