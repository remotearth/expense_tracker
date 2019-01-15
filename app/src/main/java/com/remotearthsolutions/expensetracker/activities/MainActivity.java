package com.remotearthsolutions.expensetracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.razerdp.widget.animatedpieview.AnimatedPieView;
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.adapters.CategoryListAdapter;
import com.remotearthsolutions.expensetracker.contracts.MainContract;
import com.remotearthsolutions.expensetracker.entities.Category;
import com.remotearthsolutions.expensetracker.entities.ExpeneChartData;
import com.remotearthsolutions.expensetracker.fragments.ExpenseFragment;
import com.remotearthsolutions.expensetracker.presenters.MainPresenter;
import com.remotearthsolutions.expensetracker.services.FirebaseServiceImpl;
import com.remotearthsolutions.expensetracker.utils.ChartManagerImpl;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MainContract.View, ChartManagerImpl.ChartView {

    private MainPresenter presenter;
    private RecyclerView recyclerView;
    private List<Category> categoryList;
    private CategoryListAdapter adapter;
    private AnimatedPieView mAnimatedPieView;
    private DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new MainPresenter(this, this, new ChartManagerImpl());
        presenter.init();

        List<ExpeneChartData> data = new ArrayList<>();
        ExpeneChartData data1 = new ExpeneChartData(17.3f, "#F0F0F0", "data1");
        ExpeneChartData data2 = new ExpeneChartData(40.6f, "#A0E0D0", "data2");
        ExpeneChartData data3 = new ExpeneChartData(42.1f, "#AAADD0", "data3");
        data.add(data1);
        data.add(data2);
        data.add(data3);

        presenter.loadChart(data);

        loadcategory();
        adapter = new CategoryListAdapter(categoryList);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void initializeView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        recyclerView = findViewById(R.id.recyclearView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(llm);

        ExpenseFragment fragment = new ExpenseFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.framelayout, fragment, "expensefragment");
        fragmentTransaction.commit();


        mAnimatedPieView = findViewById(R.id.animatedpie);

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

    private void loadcategory() {

        categoryList = new ArrayList<>();
        categoryList.add(new Category(R.drawable.ic_food, "Food"));
        categoryList.add(new Category(R.drawable.ic_gift, "Gift"));
        categoryList.add(new Category(R.drawable.ic_bills, "Bills"));
        categoryList.add(new Category(R.drawable.ic_taxi, "Taxi"));
        categoryList.add(new Category(R.drawable.ic_delivery_truck, "Transport"));

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Toast.makeText(getApplicationContext(), "Clicked On Bottom menu 1", Toast.LENGTH_LONG).show();
                    return true;
                case R.id.navigation_dashboard:
                    Toast.makeText(getApplicationContext(), "Clicked On Bottom Menu 2", Toast.LENGTH_LONG).show();
                    return true;
                case R.id.navigation_notifications:
                    Toast.makeText(getApplicationContext(), "Clicked On Bottom Menu 3", Toast.LENGTH_LONG).show();
                    return true;
            }
            return false;
        }
    };


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

        int id = item.getItemId();

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void loadChartConfig(AnimatedPieViewConfig config) {
        mAnimatedPieView.applyConfig(config).start();
    }
}
