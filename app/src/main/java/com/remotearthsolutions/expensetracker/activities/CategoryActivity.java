package com.remotearthsolutions.expensetracker.activities;

import android.widget.FrameLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.fragments.CategoryFragment;
import com.remotearthsolutions.expensetracker.fragments.ExpenseFragment;

public class CategoryActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        getSupportActionBar().setTitle("Category Fragment");
        CategoryFragment fragment = new CategoryFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.catframe,fragment,"fragmenthome");
        fragmentTransaction.commit();

    }
}
