package com.remotearthsolutions.expensetracker.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.activities.main.MainActivity
import com.remotearthsolutions.expensetracker.fragments.InitialSettingsFragment
import com.remotearthsolutions.expensetracker.utils.AnalyticsManager
import com.remotearthsolutions.expensetracker.utils.Constants
import com.remotearthsolutions.expensetracker.utils.SharedPreferenceUtils
import kotlinx.android.synthetic.main.activity_initial_settings.*

class InitialPreferenceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState ?: Bundle())
        setContentView(R.layout.activity_initial_settings)
        loadInitialSettingsFragment()
        gotomainactivity.setOnClickListener { gohome() }
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

    override fun onBackPressed() {

    }
}