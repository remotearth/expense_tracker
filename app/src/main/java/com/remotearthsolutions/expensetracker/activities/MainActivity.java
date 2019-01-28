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
import com.google.android.material.navigation.NavigationView;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.contracts.MainContract;
import com.remotearthsolutions.expensetracker.entities.Category;
import com.remotearthsolutions.expensetracker.fragments.CategoryFragment;
import com.remotearthsolutions.expensetracker.fragments.ExpenseFragment;
import com.remotearthsolutions.expensetracker.fragments.HomeFragment;
import com.remotearthsolutions.expensetracker.presenters.MainPresenter;
import com.remotearthsolutions.expensetracker.services.FirebaseServiceImpl;
import org.parceler.Parcels;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MainContract.View {

    private MainPresenter presenter;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private HomeFragment homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new MainPresenter(this);
        presenter.init();

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
        presenter.checkAuthectication(new FirebaseServiceImpl(this));
    }

    @Override
    public void onBackPressed() {
        
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
                CategoryFragment categoryFragment = new CategoryFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.framelayout, categoryFragment, CategoryFragment.class.getName());
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

    public void openAddExpenseScreen(Category category) {

        ExpenseFragment expenseFragment = new ExpenseFragment();
        Parcelable wrappedCategory = Parcels.wrap(category);
        Bundle bundle = new Bundle();
        bundle.putParcelable("category_parcel", wrappedCategory);
        expenseFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_up, 0, 0, R.anim.slide_out_down);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.add(R.id.framelayout, expenseFragment, ExpenseFragment.class.getName());
        fragmentTransaction.commit();
    }
}
