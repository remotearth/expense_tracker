package com.remotearthsolutions.expensetracker.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.databinding.FragmentMainBinding;
import com.remotearthsolutions.expensetracker.fragments.home.DateFilterButtonClickListener;
import com.remotearthsolutions.expensetracker.fragments.home.HomeFragment;
import com.remotearthsolutions.expensetracker.utils.Constants;
import com.remotearthsolutions.expensetracker.utils.SharedPreferenceUtils;


public class MainFragment extends Fragment implements DateFilterButtonClickListener.Callback {

    private FragmentMainBinding binding;
    private MainFragmentPagerAdapter pagerAdapter;
    private ActionBar actionBar;
    private String[] tabTitles = new String[]{"Home", "Transactions", "Tab 2",};

    private static HomeFragment homeFragment;
    private static AllExpenseFragment allExpenseFragment;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);

        pagerAdapter = new MainFragmentPagerAdapter(getChildFragmentManager());
        binding.viewpager.setOffscreenPageLimit(2);
        binding.viewpager.setAdapter(pagerAdapter);
        binding.viewpager.addOnPageChangeListener(viewPagerPageChangeListener);
        binding.navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        DateFilterButtonClickListener dateFilterButtonClickListener = new DateFilterButtonClickListener(this);
        binding.nextDateBtn.setOnClickListener(dateFilterButtonClickListener);
        binding.previousDateBtn.setOnClickListener(dateFilterButtonClickListener);
        binding.dailyRangeBtn.setOnClickListener(dateFilterButtonClickListener);
        binding.weeklyRangeBtn.setOnClickListener(dateFilterButtonClickListener);
        binding.monthlyRangeBtn.setOnClickListener(dateFilterButtonClickListener);
        binding.yearlyRangeBtn.setOnClickListener(dateFilterButtonClickListener);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String period = SharedPreferenceUtils.getInstance(getActivity()).getString(Constants.PREF_PERIOD, Constants.KEY_DAILY);
                switch (period) {
                    case Constants.KEY_DAILY:
                        binding.dailyRangeBtn.performClick();
                        break;
                    case Constants.KEY_WEEKLY:
                        binding.weeklyRangeBtn.performClick();
                        break;
                    case Constants.KEY_MONTHLY:
                        binding.monthlyRangeBtn.performClick();
                        break;
                    case Constants.KEY_YEARLY:
                        binding.yearlyRangeBtn.performClick();
                        break;
                }
            }
        }, 500);

        return binding.getRoot();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {

        switch (item.getItemId()) {
            case R.id.navigation_home:
                binding.viewpager.setCurrentItem(0, true);
                actionBar.setTitle(tabTitles[0]);
                return true;
            case R.id.navigation_transaction:
                binding.viewpager.setCurrentItem(1, true);
                actionBar.setTitle(tabTitles[1]);
                return true;
            case R.id.navigation_dashboard:
                binding.viewpager.setCurrentItem(2, true);
                actionBar.setTitle(tabTitles[2]);
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
            actionBar.setTitle(tabTitles[position]);
            switch (position) {
                case 0:
                    binding.dateRangeContainer.animate().alpha(1.0f).translationY(0).setDuration(200);
                    binding.navigation.setSelectedItemId(R.id.navigation_home);
                    break;
                case 1:
                    binding.dateRangeContainer.animate().alpha(1.0f).translationY(0).setDuration(200);
                    binding.navigation.setSelectedItemId(R.id.navigation_transaction);
                    break;
                case 2:
                    binding.dateRangeContainer.animate().alpha(0).translationY(-binding.dateRangeContainer.getHeight()).setDuration(200);
                    binding.navigation.setSelectedItemId(R.id.navigation_dashboard);
                    break;
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public void setActionBar(ActionBar supportActionBar) {
        this.actionBar = supportActionBar;
        actionBar.setTitle("Home");
    }

    @Override
    public void onDateChanged(String date, long startTime, long endTime) {
        binding.dateTv.setText(date);
        if (homeFragment != null && allExpenseFragment != null) {
            homeFragment.updateChartView(startTime, endTime);
            allExpenseFragment.updateFilterListWithDate(startTime, endTime);
        }
    }

    public static class MainFragmentPagerAdapter extends FragmentPagerAdapter {

        public MainFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    homeFragment = new HomeFragment();
                    return homeFragment;
                case 1:
                    allExpenseFragment = new AllExpenseFragment();
                    return allExpenseFragment;
                case 2:
                    return new Tab2Fragment();
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
            return null;
        }
    }
}
