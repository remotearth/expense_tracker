package com.remotearthsolutions.expensetracker.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.entities.User
import com.remotearthsolutions.expensetracker.fragments.CurrencyFragment
import com.remotearthsolutions.expensetracker.fragments.SettingsFragment
import com.remotearthsolutions.expensetracker.utils.Constants
import com.remotearthsolutions.expensetracker.utils.SharedPreferenceUtils

class CurrencySelectionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState ?: Bundle())
        setContentView(R.layout.activity_currency_selection)
        loadcurrencyfragment()
        val button =
            findViewById<Button>(R.id.gotomainactivity)
        button.setOnClickListener { gohome() }
    }

    private fun loadcurrencyfragment() {
        val currencyFragment = CurrencyFragment()
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.currencyfragment,
                currencyFragment, SettingsFragment::class.java.name
            ).commit()
    }

    private fun gohome() {
        val user = User()
        SharedPreferenceUtils.getInstance(this)?.putString(Constants.KEY_USER, Gson().toJson(user))
        SharedPreferenceUtils.getInstance(this)?.putBoolean(Constants.PREF_ISFIRSTTIMEVISITED, true)
        startActivity(Intent(this@CurrencySelectionActivity, MainActivity::class.java))
        finish()
    }
}