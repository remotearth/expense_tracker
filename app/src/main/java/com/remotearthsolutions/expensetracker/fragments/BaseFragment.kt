package com.remotearthsolutions.expensetracker.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.activities.helpers.FragmentLoader
import com.remotearthsolutions.expensetracker.activities.main.MainActivity
import com.remotearthsolutions.expensetracker.contracts.BaseView
import com.remotearthsolutions.expensetracker.services.InternetCheckerService
import com.remotearthsolutions.expensetracker.services.InternetCheckerServiceImpl
import com.remotearthsolutions.expensetracker.services.ProgressService
import com.remotearthsolutions.expensetracker.services.ProgressServiceImp
import com.remotearthsolutions.expensetracker.utils.AlertDialogUtils.show

abstract class BaseFragment : Fragment(), BaseView, Observer {
    private var internetCheckerService: InternetCheckerService? = null
    private var progressService: ProgressService? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        internetCheckerService = InternetCheckerServiceImpl(requireActivity())
        progressService = ProgressServiceImp(requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()
    }

    override fun showAlert(
        title: String?,
        message: String?,
        btnOk: String?,
        btnCancel: String?,
        btnNeutral: String?,
        callback: BaseView.Callback?
    ) {
        show(requireActivity(), title, message, btnOk, btnCancel, btnNeutral, callback)
    }

    override fun showToast(message: String?) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

    override val isDeviceOnline: Boolean
        get() = internetCheckerService!!.isConnected

    override fun showProgress(message: String?) {
        progressService!!.showProgressBar(message)
    }

    override fun hideProgress() {
        progressService!!.hideProgressBar()
    }

    fun registerBackButton(callBack: OnBackPressedCallback? = null, animationType: Int = 0) {
        val activity = requireActivity()
        val defaultCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                FragmentLoader.remove(
                    activity as AppCompatActivity,
                    this@BaseFragment,
                    getString(R.string.title_home),
                    animationType
                )
                (activity as MainActivity).hideBackButton()
            }
        }
        activity.onBackPressedDispatcher.addCallback(this, callBack ?: defaultCallback)
    }

    fun getResourceString(id: Int): String {
        return requireActivity().resources.getString(id)
    }

    fun getResourceStringArray(id: Int): Array<String> {
        return requireActivity().resources.getStringArray(id)
    }

}

interface Observer {
    fun observe() {
    }
}