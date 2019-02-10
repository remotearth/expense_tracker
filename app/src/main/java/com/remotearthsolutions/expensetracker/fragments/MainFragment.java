package com.remotearthsolutions.expensetracker.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.databinding.FragmentMainBinding;


public class MainFragment extends Fragment {

    private FragmentMainBinding binding;
    private MainFragmentPagerAdapter pagerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        View view = binding.getRoot();

        pagerAdapter = new MainFragmentPagerAdapter(getChildFragmentManager());
        binding.viewpager.setOffscreenPageLimit(2);
        binding.viewpager.setAdapter(pagerAdapter);
        binding.viewpager.addOnPageChangeListener(viewPagerPageChangeListener);
        binding.navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        return view;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                binding.viewpager.setCurrentItem(0, true);
                return true;
            case R.id.navigation_dashboard:
                binding.viewpager.setCurrentItem(1, true);
                return true;
            case R.id.navigation_transaction:
                binding.viewpager.setCurrentItem(2, true);
                return true;
        }
        return false;
    };

    private ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position){
                case 0:
                    binding.navigation.setSelectedItemId(R.id.navigation_home);
                    break;
                case 1:
                    binding.navigation.setSelectedItemId(R.id.navigation_dashboard);
                    break;
                case 2:
                    binding.navigation.setSelectedItemId(R.id.navigation_transaction);
                    break;
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public static class MainFragmentPagerAdapter extends FragmentPagerAdapter {

        private String[] tabTitles = new String[]{"Home", "Tab 2", "Transactions"};

        public MainFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new HomeFragment();
                case 1:
                    return new Tab2Fragment();
                case 2:
                    return new AllExpenseFragment();
                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            return 3;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }

    public interface Listener {
        void onPageScrolled(int position);
    }
}
