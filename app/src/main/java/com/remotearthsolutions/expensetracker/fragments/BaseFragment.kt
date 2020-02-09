package com.remotearthsolutions.expensetracker.fragments

import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.activities.MainActivity
import com.remotearthsolutions.expensetracker.contracts.BaseView
import com.remotearthsolutions.expensetracker.services.InternetCheckerService
import com.remotearthsolutions.expensetracker.services.InternetCheckerServiceImpl
import com.remotearthsolutions.expensetracker.services.ProgressService
import com.remotearthsolutions.expensetracker.services.ProgressServiceImp
import com.remotearthsolutions.expensetracker.utils.AlertDialogUtils.show
import kotlinx.android.synthetic.main.activity_main.*

abstract class BaseFragment : Fragment(), BaseView {
    private var internetCheckerService: InternetCheckerService? = null
    private var progressService: ProgressService? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        internetCheckerService = InternetCheckerServiceImpl(context!!)
        progressService = ProgressServiceImp(context!!)
    }

    override fun showAlert(
        title: String?,
        message: String?,
        btnOk: String?,
        btnCancel: String?,
        callback: BaseView.Callback?
    ) {
        show(context, title, message, btnOk, btnCancel, callback)
    }

    override fun showToast(message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override val isDeviceOnline: Boolean
        get() = internetCheckerService!!.isConnected

    override fun showProgress(message: String?) {
        progressService!!.showProgressBar(message)
    }

    override fun hideProgress() {
        progressService!!.hideProgressBar()
    }


    fun registerBackButton(callBack: OnBackPressedCallback? = null) {
        val activity = requireActivity()
        val defaultCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val fragmentManager = activity.supportFragmentManager
                val ft = fragmentManager.beginTransaction()
                ft.setCustomAnimations(R.anim.slide_in_up, 0, 0, R.anim.slide_out_down)
                ft.remove(this@BaseFragment)
                fragmentManager.popBackStack()
                ft.commit()
                activity.toolbar!!.title = getString(R.string.title_home)
                (activity as MainActivity).hideBackButton()
            }
        }
        activity.onBackPressedDispatcher.addCallback(this, callBack ?: defaultCallback)
    }
}