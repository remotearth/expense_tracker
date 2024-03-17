package com.remotearthsolutions.expensetracker.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.activities.main.MainActivity
import com.remotearthsolutions.expensetracker.databinding.ActivityInitialSettingsBinding
import com.remotearthsolutions.expensetracker.fragments.InitialSettingsFragment
import com.remotearthsolutions.expensetracker.utils.AnalyticsManager
import com.remotearthsolutions.expensetracker.utils.Constants
import com.remotearthsolutions.expensetracker.utils.SharedPreferenceUtils

class InitialPreferenceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInitialSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState ?: Bundle())
        binding = ActivityInitialSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadInitialSettingsFragment()
        binding.gotomainactivity.setOnClickListener { gohome() }
    }

    private fun loadInitialSettingsFragment() {
        val initialSettingsFragment = InitialSettingsFragment()
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragmentContainer,
                initialSettingsFragment, InitialSettingsFragment::class.java.name
            ).commit()
    }

    private fun gohome() {
        with(AnalyticsManager) { logEvent(NEW_USER) }
        val sharedPreferenceUtils = SharedPreferenceUtils.getInstance(this)
        sharedPreferenceUtils?.putBoolean(Constants.PREF_ISFIRSTTIMEVISITED, true)
        startActivity(Intent(this@InitialPreferenceActivity, MainActivity::class.java))
        finish()
    }
}