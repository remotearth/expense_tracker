package com.remotearthsolutions.expensetracker.contracts

interface BaseView {
    fun showAlert(
        title: String?,
        message: String?,
        btnOk: String?,
        btnCancel: String?,
        btnNeutral: String?,
        callback: Callback?
    )

    fun showToast(message: String?)
    val isDeviceOnline: Boolean
    fun showProgress(message: String?)
    fun hideProgress()
    interface Callback {
        fun onOkBtnPressed()
        fun onCancelBtnPressed() {
        }

        fun onNeutralBtnPressed() {
        }
    }
}