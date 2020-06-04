package com.remotearthsolutions.expensetracker.utils

import android.content.Context
import android.graphics.Typeface
import com.rahman.dialog.Utilities.SmartDialogBuilder
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.contracts.BaseView

object AlertDialogUtils {
    fun show(
        context: Context?,
        title: String?,
        message: String?,
        btnOk: String?,
        btnCancel: String?,
        callback: BaseView.Callback?,
        cancellable: Boolean = false
    ) {

        SmartDialogBuilder(context!!).apply {

            setTitle(title)
            setSubTitle(message)
            setCancalable(cancellable)
            setPositiveButton(btnOk) {
                it.dismiss()
                callback?.onOkBtnPressed()

            }
            if (btnCancel != null) {
                setNegativeButtonHide(false)
                setNegativeButton(btnCancel) {
                    it.dismiss()
                    callback?.onCancelBtnPressed()
                }
            } else {
                setNegativeButtonHide(true)
            }
            build().show()
        }
    }
}