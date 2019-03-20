package com.remotearthsolutions.expensetracker.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.callbacks.InAppBillingCallback;
import com.remotearthsolutions.expensetracker.contracts.MainContract;
import com.remotearthsolutions.expensetracker.databaseutils.AppDatabase;
import com.remotearthsolutions.expensetracker.databaseutils.DatabaseClient;
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.CategoryExpense;
import com.remotearthsolutions.expensetracker.databinding.ActivityMainBinding;
import com.remotearthsolutions.expensetracker.entities.User;
import com.remotearthsolutions.expensetracker.fragments.*;
import com.remotearthsolutions.expensetracker.fragments.main.MainFragment;
import com.remotearthsolutions.expensetracker.services.FirebaseServiceImpl;
import com.remotearthsolutions.expensetracker.services.PurchaseListener;
import com.remotearthsolutions.expensetracker.utils.AdmobUtils;
import com.remotearthsolutions.expensetracker.utils.CheckoutUtils;
import com.remotearthsolutions.expensetracker.utils.Constants;
import com.remotearthsolutions.expensetracker.utils.SharedPreferenceUtils;
import com.remotearthsolutions.expensetracker.viewmodels.MainViewModel;
import com.remotearthsolutions.expensetracker.viewmodels.viewmodel_factory.MainViewModelFactory;
import org.parceler.Parcels;
import org.solovyev.android.checkout.Inventory;
import org.solovyev.android.checkout.ProductTypes;
import org.solovyev.android.checkout.Purchase;

import javax.annotation.Nonnull;
import java.util.Random;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, MainContract.View, InAppBillingCallback, Inventory.Callback {

    private ActivityMainBinding binding;
    private MainViewModel viewModel;
    private ActionBarDrawerToggle toggle;
    private MainFragment mainFragment;
    private long backPressedTime = 0;

    private CheckoutUtils checkoutUtils;
    private PurchaseListener purchaseListener;
    private String productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productId = ((ApplicationObject) getApplication()).getAdProductId();
        checkoutUtils = CheckoutUtils.getInstance(this);

        checkoutUtils.start();
        purchaseListener = new PurchaseListener(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        AppDatabase db = DatabaseClient.getInstance(getContext()).getAppDatabase();
        viewModel = ViewModelProviders.of(this,
                new MainViewModelFactory(this, new FirebaseServiceImpl(this), db.accountDao(), db.expenseDao())).
                get(MainViewModel.class);

        String userStr = SharedPreferenceUtils.getInstance(this).getString(Constants.KEY_USER, "");
        User user = new Gson().fromJson(userStr, User.class);
        viewModel.checkAuthectication(user);
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkoutUtils.start();
        checkoutUtils.createPurchaseFlow(purchaseListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        checkoutUtils.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((ApplicationObject) getApplication()).activityResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ((ApplicationObject) getApplication()).activityPaused();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void initializeView() {
        setupActionBar();
        binding.navView.setNavigationItemSelectedListener(this);
        MenuItem homeNavItem = binding.navView.getMenu().getItem(0);
        onNavigationItemSelected(homeNavItem);
        homeNavItem.setChecked(true);
    }

    private void setupActionBar() {
        setSupportActionBar(binding.toolbar);
        toggle = new ActionBarDrawerToggle(
                this, binding.drawerLayout, binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void goBackToLoginScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onLogoutSuccess() {
        goBackToLoginScreen();
    }

    @Override
    public void startLoadingApp() {
        viewModel.init(this);
        checkoutUtils.start();
        checkoutUtils.load(this, productId);
    }

    @Override
    public void showTotalExpense(String amount) {
        binding.totalExpenseAmountTv.setText(amount);
    }

    @Override
    public void showTotalBalance(String amount) {
        binding.totalAccountAmountTv.setText(amount);
    }

    @Override
    public void stayOnCurrencyScreen() {

        Intent intent = new Intent(this, CurrencySelectionActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void setBalanceTextColor(int colorId) {
        binding.totalAccountAmountTv.setTextColor(getResources().getColor(colorId));
    }

    @Override
    public void onBackPressed() {
        Fragment expenseFragment = getSupportFragmentManager().findFragmentByTag(ExpenseFragment.class.getName());
        Fragment webViewFragment = getSupportFragmentManager().findFragmentByTag(WebViewFragment.class.getName());

        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else if (expenseFragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.setCustomAnimations(R.anim.slide_in_up, 0, 0, R.anim.slide_out_down);
            ft.remove(expenseFragment);
            fragmentManager.popBackStack();
            ft.commit();

            getSupportActionBar().setTitle("Home");

        } else if (webViewFragment != null) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            ft.remove(webViewFragment);
            ft.commit();
            fragmentManager.popBackStack();

            setupActionBar();
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } else {
            long t = System.currentTimeMillis();
            if (t - backPressedTime > 2000) {
                backPressedTime = t;
                Toast.makeText(this, "Press once again to close app", Toast.LENGTH_SHORT).show();
            } else {
                CheckoutUtils.clearInstance();
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

                getSupportActionBar().setTitle(getString(R.string.menu_categories));
                CategoryFragment categoryFragment = new CategoryFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.framelayout, categoryFragment, CategoryFragment.class.getName());
                fragmentTransaction.commit();
                break;
            }
            case R.id.nav_settings: {
                SettingsFragment settingsFragment = new SettingsFragment();
                getSupportActionBar().setTitle(getString(R.string.menu_settings));
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, settingsFragment, SettingsFragment.class.getName()).commit();
                break;
            }

            case R.id.nav_logout: {

                showAlert("", "Are you sure you want to logout?", "Yes", "No", new Callback() {
                    @Override
                    public void onOkBtnPressed() {

                        viewModel.performLogout();
                    }

                    @Override
                    public void onCancelBtnPressed() {

                    }
                });
                getDrawerLayout().closeDrawer(GravityCompat.START);
                return false;
            }

            case R.id.nav_about: {
                getSupportActionBar().setTitle(getString(R.string.menu_about));
                AboutFragment aboutFragment = new AboutFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.framelayout, aboutFragment, AboutFragment.class.getName());
                fragmentTransaction.commit();
                break;
            }

            case R.id.nav_privacypolicy: {
                getDrawerLayout().closeDrawer(GravityCompat.START);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.URL_PRIVACY_POLICY));
                startActivity(browserIntent);
                return false;
            }

            case R.id.nav_licenses: {
                getSupportActionBar().setTitle(getString(R.string.menu_licenses));
                LicenseFragment licenseFragment = new LicenseFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.framelayout, licenseFragment, AboutFragment.class.getName());
                fragmentTransaction.commit();
                break;
            }

        }
        getDrawerLayout().closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }


    public void openAddExpenseScreen(CategoryExpense categoryExpense) {

        getSupportActionBar().setTitle(getString(R.string.add_expense));
        ExpenseFragment expenseFragment = new ExpenseFragment();
        Parcelable wrappedCategoryExpense = Parcels.wrap(categoryExpense);
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.CATEGORYEXPENSE_PARCEL, wrappedCategoryExpense);
        expenseFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_up, 0, 0, R.anim.slide_out_down);
        fragmentTransaction.add(R.id.framelayout, expenseFragment, ExpenseFragment.class.getName());
        fragmentTransaction.addToBackStack(ExpenseFragment.class.getName());
        fragmentTransaction.commit();

    }

    @Override
    public void onPurchaseSuccessListener(Purchase purchase) {
        ((ApplicationObject) getApplication()).setPremium(true);
        if (purchase.sku.equals(productId)) {
            AdmobUtils.getInstance(MainActivity.this).appShouldShowAds(false);
        }
    }

    @Override
    public void onPurchaseFailedListener(String error) {
        showToast(error);
    }

    @Override
    public void onLoaded(@Nonnull Inventory.Products products) {
        String productId = ((ApplicationObject) getApplication()).getAdProductId();
        if (!products.get(ProductTypes.IN_APP).isPurchased(productId)) {
            ((ApplicationObject) getApplication()).setPremium(false);

            int delay = new Random().nextInt(5000 - 2000) + 2000;
            AdmobUtils.getInstance(MainActivity.this).appShouldShowAds(true);
            new Handler().postDelayed(() -> AdmobUtils.getInstance(MainActivity.this).showInterstitialAds(), delay);

        } else {
            ((ApplicationObject) getApplication()).setPremium(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CheckoutUtils.getInstance(this).getCheckout().onActivityResult(requestCode, resultCode, data);
    }

    public Toolbar getToolbar() {
        return binding.toolbar;
    }

    public DrawerLayout getDrawerLayout() {
        return binding.drawerLayout;
    }

    public void updateSummary(long startTime, long endTime) {
        viewModel.updateSummary(startTime, endTime);
    }

    public void updateSummary() {
        viewModel.updateSummary();
    }

    public void refreshChart() {
        mainFragment.refreshChart();
    }
}
