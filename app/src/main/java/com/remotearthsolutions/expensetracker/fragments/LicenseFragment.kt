package com.remotearthsolutions.expensetracker.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.utils.Constants

class LicenseFragment : Fragment() {
    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<*>
    private lateinit var mContext: Context
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_license, container, false)
        listView = view.findViewById(R.id.licenseFileList)
        val licenseFileName =
            resources.getStringArray(R.array.license)
        adapter = ArrayAdapter<Any?>(
            mContext,
            R.layout.custom_license,
            R.id.custom_text_license,
            licenseFileName
        )
        listView.adapter = adapter
        listView.setOnItemClickListener { _: AdapterView<*>?, _: View?, position: Int, _: Long ->
            when (position) {
                0 -> sendLicenseFileToWebFragment(Constants.RAZERDPANIMATEDPIEVIEW_LICENSE_FILE)
                1 -> sendLicenseFileToWebFragment(Constants.ROOM_LICENSE_FILE)
                2 -> sendLicenseFileToWebFragment(Constants.PURCHASEDCHECKOUT_LICENSE_FILE)
                3 -> sendLicenseFileToWebFragment(Constants.DEXTER_LICENSE_FILE)
                4 -> sendLicenseFileToWebFragment(Constants.RXJAVA_LICENSE_FILE)
                5 -> sendLicenseFileToWebFragment(Constants.PERCELER_LICENSE_FILE)
                6 -> sendLicenseFileToWebFragment(Constants.GSON_LICENSE_FILE)
                7 -> sendLicenseFileToWebFragment(Constants.FACEBOOK_LICENSE_FILE)
                8 -> sendLicenseFileToWebFragment(Constants.FIREBASE_LICENSE_FILE)
                9 -> sendLicenseFileToWebFragment(Constants.RXANROID_LICENSE_FILE)
                10 -> sendLicenseFileToWebFragment(Constants.MULTIDEX_LICENSE_FILE)
                11 -> sendLicenseFileToWebFragment(Constants.ADMOB_LICENSE_FILE)
            }
        }
        return view
    }

    private fun sendLicenseFileToWebFragment(filepath: String) {
        val actionBar =
            (mContext as AppCompatActivity?)!!.supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_back)
        }
        val webViewFragment =
            WebViewFragment()
        val bundle = Bundle()
        bundle.putString(
            Constants.KEY_SCREEN,
            getString(R.string.license_details)
        )
        bundle.putString(Constants.KEY_URL, filepath)
        webViewFragment.arguments = bundle
        val fragmentTransaction =
            (mContext as FragmentActivity?)!!.supportFragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        fragmentTransaction.replace(
            R.id.framelayout,
            webViewFragment,
            WebViewFragment::class.java.name
        )
        fragmentTransaction.addToBackStack(WebViewFragment::class.java.name)
        fragmentTransaction.commit()
    }
}