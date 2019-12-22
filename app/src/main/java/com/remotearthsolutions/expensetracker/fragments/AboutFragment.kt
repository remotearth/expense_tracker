package com.remotearthsolutions.expensetracker.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.remotearthsolutions.expensetracker.BuildConfig
import com.remotearthsolutions.expensetracker.R

class AboutFragment : Fragment() {
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_about, container, false)
        val versionNumber = view.findViewById<TextView>(R.id.versionno)
        val getVersion =
            BuildConfig.VERSION_NAME
        versionNumber.text = "${getString(R.string.version)} $getVersion"
        return view
    }
}