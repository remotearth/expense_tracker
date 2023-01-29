package com.remotearthsolutions.expensetracker.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.drawerlayout.widget.DrawerLayout
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.activities.main.MainActivity
import com.remotearthsolutions.expensetracker.databinding.FragmentWebviewBinding
import com.remotearthsolutions.expensetracker.utils.Constants

class WebViewFragment : BaseFragment() {
    private lateinit var binding: FragmentWebviewBinding
    private lateinit var mContext: Context
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    private var url: String? = null
    private var screen: String? = null

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWebviewBinding.inflate(layoutInflater, container, false)
        if (arguments != null) {
            url = requireArguments().getString(Constants.KEY_URL)
            screen = requireArguments().getString(Constants.KEY_SCREEN)
        }
        if (screen == getString(R.string.license_details)) {
            val toolbar = (context as MainActivity).mToolbar
            toolbar.setNavigationOnClickListener { (context as Activity?)!!.onBackPressed() }
            (context as MainActivity?)!!.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }
        binding.progressBar.visibility = View.GONE
        binding.webview.settings.javaScriptEnabled = true
        binding.webview.settings.builtInZoomControls = true
        binding.webview.webViewClient = WebViewClient()
        url?.let { binding.webview.loadUrl(url!!) }

        registerBackButton()
        return binding.root
    }


    inner class WebViewClient : android.webkit.WebViewClient() {
        override fun onPageStarted(
            view: WebView?,
            url: String?,
            favicon: Bitmap?
        ) {
            super.onPageStarted(view, url, favicon)
            binding.progressBar.visibility = View.VISIBLE
        }

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            url: String?
        ): Boolean {
            url?.let { view?.loadUrl(url) }
            return true
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            binding.progressBar.visibility = View.GONE
        }
    }
}