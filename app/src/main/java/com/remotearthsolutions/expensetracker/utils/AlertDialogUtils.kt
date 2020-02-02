package com.remotearthsolutions.expensetracker.utils

import android.content.Context
import com.rahman.dialog.Utilities.SmartDialogBuilder
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

        SmartDialogBuilder(context!!).apply {
            setTitle(title)
            setSubTitle(message)
            setCancalable(false)
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