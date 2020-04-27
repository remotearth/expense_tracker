package com.remotearthsolutions.expensetracker.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.activities.main.MainActivity
import com.remotearthsolutions.expensetracker.fragments.CurrencyFragment
import com.remotearthsolutions.expensetracker.fragments.SettingsFragment
import com.remotearthsolutions.expensetracker.utils.Constants
import com.remotearthsolutions.expensetracker.utils.SharedPreferenceUtils
import kotlinx.android.synthetic.main.activity_currency_selection.*

class CurrencySelectionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState ?: Bundle())
        setContentView(R.layout.activity_currency_selection)
        loadCurrencyFragment()
        gotomainactivity.setOnClickListener { gohome() }
    }

    private fun loadCurrencyFragment() {
        val currencyFragment = CurrencyFragment()
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.currencyfragment,
                currencyFragment, SettingsFragment::class.java.name
            ).commit()
    }

    private fun gohome() {
        SharedPreferenceUtils.getInstance(this)?.putBoolean(Constants.PREF_ISFIRSTTIMEVISITED, true)
        startActivity(Intent(this@CurrencySelectionActivity, MainActivity::class.java))
        finish()
    }
}