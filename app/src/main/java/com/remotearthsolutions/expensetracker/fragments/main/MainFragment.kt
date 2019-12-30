package com.remotearthsolutions.expensetracker.fragments.main

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.ActionBar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.activities.MainActivity
import com.remotearthsolutions.expensetracker.databinding.FragmentMainBinding
import com.remotearthsolutions.expensetracker.fragments.AccountsFragment
import com.remotearthsolutions.expensetracker.fragments.AllExpenseFragment
import com.remotearthsolutions.expensetracker.fragments.DashboardFragment
import com.remotearthsolutions.expensetracker.fragments.HomeFragment
import com.remotearthsolutions.expensetracker.utils.Constants
import com.remotearthsolutions.expensetracker.utils.SharedPreferenceUtils

class MainFragment : Fragment(),
    DateFilterButtonClickListener.Callback {
    private var binding: FragmentMainBinding? = null
    private var pagerAdapter: MainFragmentPagerAdapter? = null
    private var actionBar: ActionBar? = null
    private lateinit var tabTitles: Array<String>
    private var dateContainerHeight = -1
    private var selectedPeriodBtn: Button? = null
    private lateinit var mContext: Context
    private lateinit var mResources: Resources
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        mResources = mContext.resources
        tabTitles = arrayOf(
            getString(R.string.home),
            getString(R.string.transactions),
            getString(R.string.accounts),
            getString(R.string.dashboard)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_main,
            container,
            false
        )
        return binding?.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        pagerAdapter = MainFragmentPagerAdapter(childFragmentManager)
        binding!!.viewpager.offscreenPageLimit = tabTitles.size
        binding!!.viewpager.adapter = pagerAdapter
        binding!!.viewpager.addOnPageChangeListener(viewPagerPageChangeListener)
        binding!!.navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        val dateFilterButtonClickListener =
            DateFilterButtonClickListener(this)
        binding!!.nextDateBtn.setOnClickListener(dateFilterButtonClickListener)
        binding!!.previousDateBtn.setOnClickListener(dateFilterButtonClickListener)
        binding!!.dailyRangeBtn.setOnClickListener(dateFilterButtonClickListener)
        binding!!.weeklyRangeBtn.setOnClickListener(dateFilterButtonClickListener)
        binding!!.monthlyRangeBtn.setOnClickListener(dateFilterButtonClickListener)
        binding!!.yearlyRangeBtn.setOnClickListener(dateFilterButtonClickListener)
        Handler().postDelayed({
            val period = SharedPreferenceUtils.getInstance(mContext)!!.getString(
                Constants.PREF_PERIOD,
                mResources.getString(R.string.daily)
            )
            val pos =
                listOf(*mResources.getStringArray(R.array.TimePeriod))
                    .indexOf(period)
            when (pos) {
                0 -> {
                    selectedPeriodBtn = binding!!.dailyRangeBtn
                }
                1 -> {
                    selectedPeriodBtn = binding!!.weeklyRangeBtn
                }
                2 -> {
                    selectedPeriodBtn = binding!!.monthlyRangeBtn
                }
                3 -> {
                    selectedPeriodBtn = binding!!.yearlyRangeBtn
                }
                else -> {
                    selectedPeriodBtn = binding!!.dailyRangeBtn
                    SharedPreferenceUtils.getInstance(mContext)!!.putString(
                        Constants.PREF_PERIOD,
                        resources.getString(R.string.daily)
                    )
                }
            }
            selectedPeriodBtn?.setBackgroundResource(R.drawable.bg_date_range_btn_selected)
            selectedPeriodBtn?.performClick()
        }, 500)
    }

    private fun resetDateRangeBtns() {
        binding!!.dailyRangeBtn.setBackgroundResource(R.drawable.bg_date_range_btn_unselected)
        binding!!.weeklyRangeBtn.setBackgroundResource(R.drawable.bg_date_range_btn_unselected)
        binding!!.monthlyRangeBtn.setBackgroundResource(R.drawable.bg_date_range_btn_unselected)
        binding!!.yearlyRangeBtn.setBackgroundResource(R.drawable.bg_date_range_btn_unselected)
        binding!!.dailyBtnSelectionIndicatior.visibility = View.INVISIBLE
        binding!!.weeklyBtnSelectionIndicatior.visibility = View.INVISIBLE
        binding!!.monthlyBtnSelectionIndicatior.visibility = View.INVISIBLE
        binding!!.yearlyBtnSelectionIndicatior.visibility = View.INVISIBLE
    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    binding!!.viewpager.setCurrentItem(0, true)
                    actionBar!!.title = tabTitles[0]
                }
                R.id.navigation_transaction -> {
                    binding!!.viewpager.setCurrentItem(1, true)
                    actionBar!!.title = tabTitles[1]
                }
                R.id.navigation_accounts -> {
                    binding!!.viewpager.setCurrentItem(2, true)
                    actionBar!!.title = tabTitles[2]
                }
                R.id.navigation_dashboard -> {
                    binding!!.viewpager.setCurrentItem(3, true)
                    actionBar!!.title = tabTitles[3]
                }
            }
            true
        }

    private val viewPagerPageChangeListener: OnPageChangeListener = object : OnPageChangeListener {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }

        override fun onPageSelected(position: Int) {
            actionBar!!.title = tabTitles[position]
            if (dateContainerHeight == -1) {
                dateContainerHeight = binding!!.dateRangeContainer.measuredHeight
            }
            when (position) {
                0 -> {
                    if (binding!!.dateRangeContainer.measuredHeight == 0) {
                        val anim = ValueAnimator.ofInt(0, dateContainerHeight)
                        anim.addUpdateListener { valueAnimator: ValueAnimator ->
                            val `val` = valueAnimator.animatedValue as Int
                            val layoutParams =
                                binding!!.dateRangeContainer.layoutParams
                            layoutParams.height = `val`
                            binding!!.dateRangeContainer.layoutParams = layoutParams
                        }
                        anim.duration = 200
                        anim.start()
                        anim.addListener(object : Animator.AnimatorListener {
                            override fun onAnimationStart(animation: Animator) {}
                            override fun onAnimationEnd(animation: Animator) {
                                homeFragment!!.refresh()
                            }

                            override fun onAnimationCancel(animation: Animator) {}
                            override fun onAnimationRepeat(animation: Animator) {}
                        })
                        Handler().postDelayed({
                            binding!!.dateRangeContainer.animate().alpha(1.0f).translationY(0f)
                                .duration = 200
                        }, 100)
                    }
                    binding!!.navigation.selectedItemId = R.id.navigation_home
                }
                1 -> {
                    if (binding!!.dateRangeContainer.measuredHeight == 0) {
                        val anim = ValueAnimator.ofInt(0, dateContainerHeight)
                        anim.addUpdateListener { valueAnimator: ValueAnimator ->
                            val `val` = valueAnimator.animatedValue as Int
                            val layoutParams =
                                binding!!.dateRangeContainer.layoutParams
                            layoutParams.height = `val`
                            binding!!.dateRangeContainer.layoutParams = layoutParams
                        }
                        anim.duration = 200
                        anim.start()
                        Handler().postDelayed({
                            binding!!.dateRangeContainer.animate().alpha(1.0f).translationY(0f)
                                .duration = 200
                        }, 100)
                    }
                    binding!!.navigation.selectedItemId = R.id.navigation_transaction
                }
                2 -> {
                    if (binding!!.dateRangeContainer.measuredHeight == dateContainerHeight) {
                        binding!!.dateRangeContainer.animate().alpha(0f)
                            .translationY(-binding!!.dateRangeContainer.height.toFloat()).duration =
                            200
                        Handler().postDelayed({
                            val anim = ValueAnimator.ofInt(dateContainerHeight, 0)
                            anim.addUpdateListener { valueAnimator: ValueAnimator ->
                                val `val` = valueAnimator.animatedValue as Int
                                val layoutParams =
                                    binding!!.dateRangeContainer.layoutParams
                                layoutParams.height = `val`
                                binding!!.dateRangeContainer.layoutParams = layoutParams
                            }
                            anim.duration = 200
                            anim.start()
                        }, 300)
                    }
                    binding!!.navigation.selectedItemId = R.id.navigation_accounts
                }
                3 -> {
                    if (binding!!.dateRangeContainer.measuredHeight == dateContainerHeight) {
                        binding!!.dateRangeContainer.animate().alpha(0f)
                            .translationY(-binding!!.dateRangeContainer.height.toFloat()).duration =
                            200
                        Handler().postDelayed({
                            val anim = ValueAnimator.ofInt(dateContainerHeight, 0)
                            anim.addUpdateListener { valueAnimator: ValueAnimator ->
                                val `val` = valueAnimator.animatedValue as Int
                                val layoutParams =
                                    binding!!.dateRangeContainer.layoutParams
                                layoutParams.height = `val`
                                binding!!.dateRangeContainer.layoutParams = layoutParams
                            }
                            anim.duration = 200
                            anim.start()
                        }, 300)
                    }
                    binding!!.navigation.selectedItemId = R.id.navigation_dashboard
                }
            }
        }

        override fun onPageScrollStateChanged(state: Int) {}
    }

    fun setActionBar(
        supportActionBar: ActionBar?,
        name: String?
    ) {
        actionBar = supportActionBar
        actionBar!!.title = name
    }

    override fun onDateChanged(
        btnId: Int,
        date: String?,
        startTime: Long,
        endTime: Long
    ) {
        if (btnId != R.id.nextDateBtn && btnId != R.id.previousDateBtn) {
            resetDateRangeBtns()
        }
        var periodSelector: Button? = null
        when (btnId) {
            R.id.dailyRangeBtn -> {
                selectedPeriodBtn = binding!!.dailyRangeBtn
                periodSelector = binding!!.dailyBtnSelectionIndicatior
            }
            R.id.weeklyRangeBtn -> {
                selectedPeriodBtn = binding!!.weeklyRangeBtn
                periodSelector = binding!!.weeklyBtnSelectionIndicatior

            }
            R.id.monthlyRangeBtn -> {
                selectedPeriodBtn = binding!!.monthlyRangeBtn
                periodSelector = binding!!.monthlyBtnSelectionIndicatior

            }
            R.id.yearlyRangeBtn -> {
                selectedPeriodBtn = binding!!.yearlyRangeBtn
                periodSelector = binding!!.yearlyBtnSelectionIndicatior
            }
        }
        selectedPeriodBtn?.setBackgroundResource(R.drawable.bg_date_range_btn_selected)
        periodSelector?.visibility = View.VISIBLE

        binding!!.dateTv.text = date
        if (homeFragment != null && allExpenseFragment != null) {
            homeFragment!!.updateChartView(startTime, endTime)
            allExpenseFragment!!.updateFilterListWithDate(
                startTime,
                endTime,
                btnId
            )
        }
        (mContext as MainActivity).updateSummary(startTime, endTime)
    }

    fun refreshChart() {
        if (selectedPeriodBtn != null) {
            selectedPeriodBtn!!.performClick()
        }
    }

    class MainFragmentPagerAdapter(fm: FragmentManager?) :
        FragmentPagerAdapter(fm!!) {
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> {
                    homeFragment = HomeFragment()
                    homeFragment!!
                }
                1 -> {
                    allExpenseFragment = AllExpenseFragment()
                    allExpenseFragment!!
                }
                2 -> {
                    accountsFragment = AccountsFragment()
                    accountsFragment!!
                }
                3 -> {
                    dashboardFragment = DashboardFragment()
                    dashboardFragment!!
                }
                else -> {
                    homeFragment = HomeFragment()
                    homeFragment!!
                }
            }
        }

        override fun getCount(): Int {
            return 4
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return null
        }
    }

    companion object {
        private var homeFragment: HomeFragment? = null
        private var allExpenseFragment: AllExpenseFragment? = null
        private var dashboardFragment: DashboardFragment? = null
        private var accountsFragment: AccountsFragment? = null
    }
}