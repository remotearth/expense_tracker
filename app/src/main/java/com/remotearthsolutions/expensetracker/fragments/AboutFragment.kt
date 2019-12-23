package com.remotearthsolutions.expensetracker.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.remotearthsolutions.expensetracker.BuildConfig
import com.remotearthsolutions.expensetracker.R
import kotlinx.android.synthetic.main.fragment_about.view.*

class AboutFragment : Fragment() {
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_about, container, false)
        val getVersion =
            BuildConfig.VERSION_NAME
        view.versionno.text = "${getString(R.string.version)} $getVersion"
        return view
    }
}