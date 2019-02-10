package com.remotearthsolutions.expensetracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.contracts.MainContract;
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel;
import com.remotearthsolutions.expensetracker.entities.User;
import com.remotearthsolutions.expensetracker.fragments.*;
import com.remotearthsolutions.expensetracker.services.FirebaseServiceImpl;
import com.remotearthsolutions.expensetracker.utils.Constants;
import com.remotearthsolutions.expensetracker.utils.SharedPreferenceUtils;
import com.remotearthsolutions.expensetracker.viewmodels.MainViewModel;
import com.remotearthsolutions.expensetracker.viewmodels.viewmodel_factory.MainViewModelFactory;
import org.parceler.Parcels;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MainContract.View {

    private MainViewModel viewModel;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private HomeFragment homeFragment;
    private long backPressedTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = ViewModelProviders.of(this,
                new MainViewModelFactory(this)).
                get(MainViewModel.class);
        viewModel.init();

        homeFragment = new HomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.framelayout, homeFragment, HomeFragment.class.getName());
        fragmentTransaction.commit();
    }

    @Override
    public void initializeView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void openLoginScreen() {

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        String userStr = SharedPreferenceUtils.getInstance(this).getString(Constants.KEY_USER, "");
        User user = new Gson().fromJson(userStr, User.class);
        viewModel.checkAuthectication(new FirebaseServiceImpl(this), user);
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if (getSupportFragmentManager().getBackStackEntryCount() == 1)
        {

            super.onBackPressed();
        }
        else
        {
            long t = System.currentTimeMillis();
            if (t - backPressedTime > 2000)
            {
                backPressedTime = t;
                Toast.makeText(this, "Press once again to close app", Toast.LENGTH_SHORT).show();

            }
            else
            {
                finishAffinity();
            }


        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_home: {
                homeFragment = new HomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.framelayout, homeFragment, HomeFragment.class.getName());
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

            case R.id.nav_logout: {

                SharedPreferenceUtils.getInstance(this).logOut();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }

            case R.id.nav_settings: {

                getSupportActionBar().setTitle("Settings");
                getFragmentManager().beginTransaction().replace(R.id.framelayout, new SettingsFragment()).commit();
                break;
            }

            case R.id.nav_about:
            {
                getSupportActionBar().setTitle("About Us");
                AboutFragment aboutFragment = new AboutFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.framelayout, aboutFragment, AboutFragment.class.getName());
                fragmentTransaction.commit();
                break;
            }

            case R.id.nav_privacypolicy:
            {
                WebViewFragment webViewFragment = new WebViewFragment();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.KEY_URL,Constants.URL_PRIVACY_POLICY);
                webViewFragment.setArguments(bundle);
                getSupportActionBar().setTitle("Privacy Policy");
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.framelayout, webViewFragment, Constants.URL_PRIVACY_POLICY_TAG);
                fragmentTransaction.commit();
                break;

            }

            case R.id.nav_licenses:
            {
                WebViewFragment webViewFragment = new WebViewFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putString(Constants.KEY_URL,Constants.URL_LICENSES);
                webViewFragment.setArguments(bundle1);
                getSupportActionBar().setTitle("Licenses Agreement");
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.framelayout, webViewFragment, Constants.URL_lICENSES_TAG);
                fragmentTransaction.commit();
                break;
            }

        }
        drawer.closeDrawer(GravityCompat.START);
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
