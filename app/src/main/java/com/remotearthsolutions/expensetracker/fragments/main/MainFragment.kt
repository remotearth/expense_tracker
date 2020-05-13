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
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.activities.main.MainActivity
import com.remotearthsolutions.expensetracker.databinding.FragmentMainBinding
import com.remotearthsolutions.expensetracker.fragments.*
import com.remotearthsolutions.expensetracker.utils.Constants
import com.remotearthsolutions.expensetracker.utils.SharedPreferenceUtils
import com.remotearthsolutions.expensetracker.utils.findViewPagerFragmentByTag
import com.remotearthsolutions.expensetracker.views.PeriodButton

class MainFragment : BaseFragment(),
    DateFilterButtonClickListener.Callback {
    private var binding: FragmentMainBinding? = null
    private var pagerAdapter: MainFragmentPagerAdapter? = null
    private var actionBar: ActionBar? = null
    private var tabTitles: Array<String>? = null
    private var dateContainerHeight = -1
    private var selectedPeriodBtn: PeriodButton? = null
    private lateinit var mContext: Context
    private lateinit var mResources: Resources
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        mResources = mContext.resources
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
        registerBackButton(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                (activity as MainActivity).onBackButtonPressed()
            }
        })

        tabTitles = arrayOf(
            getString(R.string.title_home),
            getString(R.string.title_transaction),
            getString(R.string.title_overview),
            getString(R.string.title_accounts)
        )

        pagerAdapter = MainFragmentPagerAdapter(childFragmentManager)

        binding!!.viewpager.offscreenPageLimit = 4
        binding!!.viewpager.adapter = pagerAdapter
        binding!!.viewpager.addOnPageChangeListener(viewPagerPageChangeListener)
        binding!!.navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        val dateFilterButtonClickListener = DateFilterButtonClickListener(this)
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
            selectedPeriodBtn?.performClick()
        }, 500)
    }

    private fun resetDateRangeBtns() {
        binding!!.dailyRangeBtn.setIsSelected(false)
        binding!!.weeklyRangeBtn.setIsSelected(false)
        binding!!.monthlyRangeBtn.setIsSelected(false)
        binding!!.yearlyRangeBtn.setIsSelected(false)
    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    binding!!.viewpager.setCurrentItem(0, true)
                    actionBar?.title = tabTitles?.get(0)
                }
                R.id.navigation_transaction -> {
                    binding!!.viewpager.setCurrentItem(1, true)
                    actionBar?.title = tabTitles?.get(1)
                }
                R.id.navigation_overview -> {
                    binding!!.viewpager.setCurrentItem(2, true)
                    actionBar?.title = tabTitles?.get(2)
                }
                R.id.navigation_accounts -> {
                    binding!!.viewpager.setCurrentItem(3, true)
                    actionBar?.title = tabTitles?.get(3)
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
            actionBar?.title = tabTitles?.get(position)
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
                                val homeFragment =
                                    childFragmentManager.findViewPagerFragmentByTag<HomeFragment>(
                                        R.id.viewpager,
                                        0
                                    )
                                homeFragment?.refresh()
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
                    binding!!.navigation.selectedItemId = R.id.navigation_overview
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
                    binding!!.navigation.selectedItemId = R.id.navigation_accounts
                }
            }
        }

        override fun onPageScrollStateChanged(state: Int) {}
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
        when (btnId) {
            R.id.dailyRangeBtn -> {
                selectedPeriodBtn = binding!!.dailyRangeBtn
            }
            R.id.weeklyRangeBtn -> {
                selectedPeriodBtn = binding!!.weeklyRangeBtn
            }
            R.id.monthlyRangeBtn -> {
                selectedPeriodBtn = binding!!.monthlyRangeBtn
            }
            R.id.yearlyRangeBtn -> {
                selectedPeriodBtn = binding!!.yearlyRangeBtn
            }
        }
        selectedPeriodBtn?.setIsSelected(true)

        binding!!.dateTv.text = date
        val homeFragment =
            childFragmentManager.findViewPagerFragmentByTag<HomeFragment>(R.id.viewpager, 0)
        val allExpenseFragment =
            childFragmentManager.findViewPagerFragmentByTag<AllExpenseFragment>(R.id.viewpager, 1)
        if (homeFragment != null && allExpenseFragment != null) {
            homeFragment.updateChartView(startTime, endTime)
            allExpenseFragment.updateFilterListWithDate(
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
        FragmentPagerAdapter(fm!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> {
                    val homeFragment = HomeFragment()
                    homeFragment
                }
                1 -> {
                    val allExpenseFragment = AllExpenseFragment()
                    allExpenseFragment
                }
                2 -> {
                    val overViewFragment = OverViewFragment()
                    overViewFragment
                }
                3 -> {
                    val accountsFragment = AccountsFragment()
                    accountsFragment
                }
                else -> {
                    Fragment()
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

//    private fun <T : Fragment> findViewPagerFragmentByTag(
//        fragmentManager: FragmentManager,
//        fragmentPosition: Int
//    ): T? {
//        val fragmentTag = "android:switcher:${R.id.viewpager}:${fragmentPosition}"
//        return fragmentManager.findFragmentByTag(fragmentTag) as T?
//    }

}