package com.remotearthsolutions.expensetracker.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.remotearthsolutions.expensetracker.contracts.BaseView
import com.remotearthsolutions.expensetracker.services.InternetCheckerService
import com.remotearthsolutions.expensetracker.services.InternetCheckerServiceImpl
import com.remotearthsolutions.expensetracker.services.ProgressService
import com.remotearthsolutions.expensetracker.services.ProgressServiceImp
import com.remotearthsolutions.expensetracker.utils.AlertDialogUtils

abstract class BaseActivity : AppCompatActivity(), BaseView {
    private lateinit var internetCheckerService: InternetCheckerService
    private lateinit var progressService: ProgressService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState ?: Bundle())
        internetCheckerService = InternetCheckerServiceImpl(this)
        progressService = ProgressServiceImp(this)
    }

    override fun showAlert(
        title: String?,
        message: String?,
        btnOk: String?,
        btnCancel: String?,
        callback: BaseView.Callback?
    ) {
        AlertDialogUtils.show(this, title, message, btnOk, btnCancel, callback)
    }

    override fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override val isDeviceOnline: Boolean
        get() = internetCheckerService.isConnected

    override fun showProgress(message: String?) {
        progressService.showProgressBar(message)
    }

    override fun hideProgress() {
        progressService.hideProgressBar()
    }
}