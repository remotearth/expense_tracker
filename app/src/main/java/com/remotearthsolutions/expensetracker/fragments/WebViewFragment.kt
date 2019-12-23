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
import androidx.fragment.app.Fragment
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.activities.MainActivity
import com.remotearthsolutions.expensetracker.utils.Constants
import kotlinx.android.synthetic.main.fragment_webview.view.*

class WebViewFragment : Fragment() {
    private lateinit var mContext: Context
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    private lateinit var mView: View
    private var url: String? = null
    private var screen: String? = null
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_webview, container, false)
        if (arguments != null) {
            url = arguments!!.getString(Constants.KEY_URL)
            screen = arguments!!.getString(Constants.KEY_SCREEN)
        }
        if (screen == getString(R.string.license_details)) {
            val toolbar = (context as MainActivity).mToolbar
            toolbar.setNavigationOnClickListener { (context as Activity?)!!.onBackPressed() }
            (context as MainActivity?)!!.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }
        mView.progressBar.visibility = View.GONE
        mView.webview.settings.javaScriptEnabled = true
        mView.webview.settings.builtInZoomControls = true
        mView.webview.webViewClient = WebViewClient()
        mView.webview.loadUrl(url)
        return mView
    }


    inner class WebViewClient : android.webkit.WebViewClient() {
        override fun onPageStarted(
            view: WebView?,
            url: String?,
            favicon: Bitmap?
        ) {
            super.onPageStarted(view, url, favicon)
            mView.progressBar.visibility = View.VISIBLE
        }

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            url: String?
        ): Boolean {
            view?.loadUrl(url)
            return true
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            mView.progressBar.visibility = View.GONE
        }
    }
}