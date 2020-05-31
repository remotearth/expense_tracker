package com.remotearthsolutions.expensetracker.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.remotearthsolutions.expensetracker.BuildConfig
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.activities.ApplicationObject
import kotlinx.android.synthetic.main.fragment_about.view.*

class AboutFragment : BaseFragment() {
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_about, container, false)
        val getVersion =
            BuildConfig.VERSION_NAME
        view.versionno.text = "${requireContext().getString(R.string.version)} $getVersion"
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerBackButton()
        var versionNumberClickCount = 0
        view.versionno.setOnClickListener {
            versionNumberClickCount++
            if (versionNumberClickCount == 10) {
                versionNumberClickCount = 0
                (activity?.application as ApplicationObject).isPremium = true
            }
        }
    }
}