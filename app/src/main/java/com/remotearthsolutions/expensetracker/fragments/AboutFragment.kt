package com.remotearthsolutions.expensetracker.fragments

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.remotearthsolutions.expensetracker.BuildConfig
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.activities.ApplicationObject
import com.remotearthsolutions.expensetracker.databinding.FragmentAboutBinding
import com.remotearthsolutions.expensetracker.utils.AlertDialogUtils
import com.remotearthsolutions.expensetracker.utils.Constants
import com.remotearthsolutions.expensetracker.utils.SharedPreferenceUtils

class AboutFragment : BaseFragment() {

    private lateinit var binding: FragmentAboutBinding

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAboutBinding.inflate(layoutInflater, container, false)

        val getVersion =
            BuildConfig.VERSION_NAME
        binding.versionno.text = "${requireContext().getString(R.string.version)} $getVersion"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerBackButton()
        var clickOnVersionNumberCount = 0
        binding.versionno.setOnClickListener {
            clickOnVersionNumberCount++
            if (clickOnVersionNumberCount == 10) {
                clickOnVersionNumberCount = 0
                (activity?.application as ApplicationObject).isPremium = true
            }
        }

        var clickOnLogoCount = 0
        binding.logo.setOnClickListener {
            clickOnLogoCount++
            if (clickOnLogoCount == 5) {
                clickOnLogoCount = 0
                val clipboard =
                    requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val userId = SharedPreferenceUtils.getInstance(requireContext())
                    ?.getString(Constants.KEY_USER, "")!!
                clipboard.setPrimaryClip(ClipData.newPlainText("userid", "UserID: $userId"))
                AlertDialogUtils.show(
                    requireContext(), null,
                    "Your UserID is copied to clipboard. Please paste it on your email client and send to remotearth.solutions@gmail.com",
                    "Ok", null, null, null
                )
            }
        }
    }
}
