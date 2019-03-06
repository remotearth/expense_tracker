package com.remotearthsolutions.expensetracker.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.FrameLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.gson.Gson;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.entities.User;
import com.remotearthsolutions.expensetracker.fragments.CurrencyFragment;
import com.remotearthsolutions.expensetracker.fragments.SettingsFragment;
import com.remotearthsolutions.expensetracker.utils.Constants;
import com.remotearthsolutions.expensetracker.utils.SharedPreferenceUtils;

public class CurrencySelection extends AppCompatActivity {

    private FrameLayout frameLayout;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_selection);

        SharedPreferenceUtils.getInstance(this);
        if (SharedPreferenceUtils.getInstance(this).getString(Constants.isFirstTimevisited,"").equals(Constants.firstTimevisitedValue))
        {
            startActivity(new Intent(CurrencySelection.this,MainActivity.class));
            finish();
        }

        frameLayout = findViewById(R.id.currencyfragment);
        loadcurrencyfragment();
        button = findViewById(R.id.gotomainactivity);
        button.setOnClickListener(v -> gohome());

     }

    private void loadcurrencyfragment()
    {
        CurrencyFragment currencyFragment = new CurrencyFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.currencyfragment,
                currencyFragment,SettingsFragment.class.getName()).commit();
    }

    private void gohome()
    {
        User user = new User();
        user.setAuthType("guestuser");
        SharedPreferenceUtils.getInstance(this).putString(Constants.KEY_USER, new Gson().toJson(user));
        SharedPreferenceUtils.getInstance(this).putString(Constants.isFirstTimevisited, Constants.firstTimevisitedValue);
        startActivity(new Intent(CurrencySelection.this, MainActivity.class));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
