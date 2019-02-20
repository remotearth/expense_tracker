package com.remotearthsolutions.expensetracker.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.contracts.MainContract;
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel;
import com.remotearthsolutions.expensetracker.databinding.ActivityMainBinding;
import com.remotearthsolutions.expensetracker.entities.User;
import com.remotearthsolutions.expensetracker.fragments.*;
import com.remotearthsolutions.expensetracker.services.FirebaseServiceImpl;
import com.remotearthsolutions.expensetracker.utils.AdmobUtils;
import com.remotearthsolutions.expensetracker.utils.Constants;
import com.remotearthsolutions.expensetracker.utils.SharedPreferenceUtils;
import com.remotearthsolutions.expensetracker.viewmodels.MainViewModel;
import com.remotearthsolutions.expensetracker.viewmodels.viewmodel_factory.MainViewModelFactory;
import org.parceler.Parcels;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, MainContract.View {

    private ActivityMainBinding binding;
    private MainViewModel viewModel;
    private ActionBarDrawerToggle toggle;
    private MainFragment mainFragment;
    private long backPressedTime = 0;
    private InterstitialAd interstitialAd;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        viewModel = ViewModelProviders.of(this,
                new MainViewModelFactory(this, new FirebaseServiceImpl(this))).
                get(MainViewModel.class);
        viewModel.init();

        AdmobUtils admobUtils = new AdmobUtils(this);
        interstitialAd = new InterstitialAd(this);
        admobUtils.showInterstitialAds(interstitialAd);


    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void initializeView() {

        setSupportActionBar(binding.toolbar);

        toggle = new ActionBarDrawerToggle(
                this, binding.drawerLayout, binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        binding.navView.setNavigationItemSelectedListener(this);

        mainFragment = new MainFragment();
        mainFragment.setActionBar(getSupportActionBar());
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.framelayout, mainFragment, MainFragment.class.getName());
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void goBackToLoginScreen() {

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onLogoutSuccess() {
        SharedPreferenceUtils.getInstance(getContext()).clear();
        goBackToLoginScreen();
    }

    @Override
    protected void onStart() {
        super.onStart();
        String userStr = SharedPreferenceUtils.getInstance(this).getString(Constants.KEY_USER, "");
        User user = new Gson().fromJson(userStr, User.class);
        viewModel.checkAuthectication(user);
    }

    @Override
    public void onBackPressed() {
        Fragment expenseFragment = getSupportFragmentManager().findFragmentByTag(ExpenseFragment.class.getName());
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else if (expenseFragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft =  fragmentManager.beginTransaction();
            ft.setCustomAnimations(R.anim.slide_in_up, 0, 0, R.anim.slide_out_down);
            ft.remove(expenseFragment);
            fragmentManager.popBackStack();
        } else {
            long t = System.currentTimeMillis();
            if (t - backPressedTime > 2000) {
                backPressedTime = t;
                Toast.makeText(this, "Press once again to close app", Toast.LENGTH_SHORT).show();
            } else {
                super.onBackPressed();
            }

        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_home: {
                mainFragment = new MainFragment();
                mainFragment.setActionBar(getSupportActionBar());
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.framelayout, mainFragment, MainFragment.class.getName());
                fragmentTransaction.commit();
                break;
            }

            case R.id.nav_categories: {

                getSupportActionBar().setTitle("Category");
                CategoryFragment categoryFragment = new CategoryFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.framelayout, categoryFragment, CategoryFragment.class.getName());
                fragmentTransaction.commit();
                break;
            }
            case R.id.nav_settings: {
                SettingsFragment settingsFragment = new SettingsFragment();
                getSupportActionBar().setTitle("Settings");
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, settingsFragment, SettingsFragment.class.getName()).commit();
                break;
            }

            case R.id.nav_logout: {

                showAlert("", "Are you sure want to logout?", "Yes", "No", new Callback() {
                    @Override
                    public void onOkBtnPressed() {

                        viewModel.performLogout();
                    }

                    @Override
                    public void onCancelBtnPressed() {

                    }
                });
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }

            case R.id.nav_about: {
                getSupportActionBar().setTitle("About Us");
                AboutFragment aboutFragment = new AboutFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.framelayout, aboutFragment, AboutFragment.class.getName());
                fragmentTransaction.commit();
                break;
            }

            case R.id.nav_privacypolicy: {
                WebViewFragment webViewFragment = new WebViewFragment();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.KEY_URL, Constants.URL_PRIVACY_POLICY);
                webViewFragment.setArguments(bundle);
                getSupportActionBar().setTitle("Privacy Policy");
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.framelayout, webViewFragment, Constants.URL_PRIVACY_POLICY_TAG);
                fragmentTransaction.commit();
                break;
            }

            case R.id.nav_licenses: {
                WebViewFragment webViewFragment = new WebViewFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putString(Constants.KEY_URL, Constants.URL_LICENSES);
                webViewFragment.setArguments(bundle1);
                getSupportActionBar().setTitle("Licenses Agreement");
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.framelayout, webViewFragment, Constants.URL_lICENSES_TAG);
                fragmentTransaction.commit();
                break;
            }

        }
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }


    public void openAddExpenseScreen(CategoryModel category) {

        ExpenseFragment expenseFragment = new ExpenseFragment();
        Parcelable wrappedCategory = Parcels.wrap(category);
        Bundle bundle = new Bundle();
        bundle.putParcelable("category_parcel", wrappedCategory);
        expenseFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_up, 0, 0, R.anim.slide_out_down);
        fragmentTransaction.add(R.id.framelayout, expenseFragment, ExpenseFragment.class.getName());
        fragmentTransaction.addToBackStack(ExpenseFragment.class.getName());
        fragmentTransaction.commit();
    }
}
