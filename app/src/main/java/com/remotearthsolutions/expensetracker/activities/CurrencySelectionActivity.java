package com.remotearthsolutions.expensetracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.entities.User;
import com.remotearthsolutions.expensetracker.fragments.CurrencyFragment;
import com.remotearthsolutions.expensetracker.fragments.SettingsFragment;
import com.remotearthsolutions.expensetracker.utils.Constants;
import com.remotearthsolutions.expensetracker.utils.SharedPreferenceUtils;

public class CurrencySelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_selection);

        loadcurrencyfragment();
        Button button = findViewById(R.id.gotomainactivity);
        button.setOnClickListener(v -> gohome());
    }

    private void loadcurrencyfragment() {
        CurrencyFragment currencyFragment = new CurrencyFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.currencyfragment,
                        currencyFragment, SettingsFragment.class.getName()).commit();
    }

    private void gohome() {
        User user = new User();
        SharedPreferenceUtils.getInstance(this).putString(Constants.KEY_USER, new Gson().toJson(user));
        SharedPreferenceUtils.getInstance(this).putBoolean(Constants.PREF_ISFIRSTTIMEVISITED, true);
        startActivity(new Intent(CurrencySelectionActivity.this, MainActivity.class));
        finish();
    }
}
