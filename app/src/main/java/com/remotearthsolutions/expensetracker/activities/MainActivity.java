package com.remotearthsolutions.expensetracker.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;
import com.razerdp.widget.animatedpieview.AnimatedPieView;
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig;
import com.razerdp.widget.animatedpieview.data.SimplePieInfo;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.adapters.viewholder.Adapter_Home;
import com.remotearthsolutions.expensetracker.entities.HomeModel;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView;
    List<HomeModel> allcatlist;
    Adapter_Home adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Code for Navigation drawer
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Call bottom Navigation method
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        // Call Pie Chart
        drawPie();

        // Call Category Data
        recyclerView = findViewById(R.id.recyclearView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm= new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(llm);
        loadcategory();
        adapter = new Adapter_Home(allcatlist,this);
        recyclerView.setAdapter(adapter);



    }

    // method for pie chart operation
    private void drawPie() {


        AnimatedPieView mAnimatedPieView = findViewById(R.id.animatedpie);
        AnimatedPieViewConfig config = new AnimatedPieViewConfig();
        config.startAngle(-90)// Starting angle offset
                .addData(new SimplePieInfo(30, Color.parseColor("#00aaee"), "Medical"))
                .addData(new SimplePieInfo(18.0f, Color.parseColor("#000000"), "Health"))
                .canTouch(true)
                .duration(2000);

        mAnimatedPieView.applyConfig(config);
        mAnimatedPieView.start();

    }

    // method for category item
    private void loadcategory() {

        allcatlist = new ArrayList<>();
        allcatlist.add(new HomeModel(R.drawable.ic_food,"Food"));
        allcatlist.add(new HomeModel(R.drawable.ic_gift,"Gift"));
        allcatlist.add(new HomeModel(R.drawable.ic_bills,"Bills"));
        allcatlist.add(new HomeModel(R.drawable.ic_taxi,"Taxi"));
        allcatlist.add(new HomeModel(R.drawable.ic_delivery_truck,"Transport"));

    }



    // Method for bottom Navigation
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Toast.makeText(getApplicationContext(),"Clicked On Bottom menu 1",Toast.LENGTH_LONG).show();
                    return true;
                case R.id.navigation_dashboard:
                    Toast.makeText(getApplicationContext(),"Clicked On Bottom Menu 2",Toast.LENGTH_LONG).show();
                    return true;
                case R.id.navigation_notifications:
                    Toast.makeText(getApplicationContext(),"Clicked On Bottom Menu 3",Toast.LENGTH_LONG).show();
                    return true;
            }
            return false;
        }
    };


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // Method for Navigation Drawer menu action
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }
}
