package com.remotearthsolutions.expensetracker.activities.main

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.activities.ApplicationObject
import com.remotearthsolutions.expensetracker.activities.BaseActivity
import com.remotearthsolutions.expensetracker.activities.InitialPreferenceActivity
import com.remotearthsolutions.expensetracker.activities.LoginActivity
import com.remotearthsolutions.expensetracker.activities.helpers.FragmentLoader
import com.remotearthsolutions.expensetracker.contracts.MainContract
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.CategoryExpense
import com.remotearthsolutions.expensetracker.databinding.ActivityMainBinding
import com.remotearthsolutions.expensetracker.fragments.HomeFragment
import com.remotearthsolutions.expensetracker.fragments.OverViewFragment
import com.remotearthsolutions.expensetracker.fragments.ViewShadeFragment
import com.remotearthsolutions.expensetracker.fragments.addexpensescreen.ExpenseFragment
import com.remotearthsolutions.expensetracker.fragments.addexpensescreen.Purpose
import com.remotearthsolutions.expensetracker.fragments.main.MainFragment
import com.remotearthsolutions.expensetracker.services.InternetCheckerServiceImpl
import com.remotearthsolutions.expensetracker.utils.*
import com.remotearthsolutions.expensetracker.utils.cloudbackup.CloudBackupManager
import com.remotearthsolutions.expensetracker.utils.workmanager.WorkManagerEnqueuer
import com.remotearthsolutions.expensetracker.utils.workmanager.WorkRequestType
import com.remotearthsolutions.expensetracker.viewmodels.mainview.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.parceler.Parcels

class MainActivity : BaseActivity(), MainContract.View {

    private var toggle: ActionBarDrawerToggle? = null
    private var backPressedTime: Long = 0
    private var preferencesChangeListener: PreferencesChangeListener? = null
    val viewModel: MainViewModel by viewModel { parametersOf(this, this) }
    private lateinit var binding: ActivityMainBinding
    private lateinit var playBillingUtils: PlayBillingUtils
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    lateinit var notificationPermissionUtils: NotificationPermissionUtils

    private var purchaseStatusChecked = MutableLiveData<Boolean>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onNewIntent(intent)

        observeState()
        playBillingUtils = PlayBillingUtils(this)
        preferencesChangeListener = PreferencesChangeListener(this)
        notificationPermissionUtils = NotificationPermissionUtils(this)

        val userStr = SharedPreferenceUtils.getInstance(this)?.getString(Constants.KEY_USER, "")
        viewModel.checkAuthectication(userStr!!)

        Handler(Looper.getMainLooper()).postDelayed({
            InAppUpdateUtils().requestUpdateApp(this@MainActivity)
        }, 2000)

        setPeriodicReminderToAskAddingExpense()

        Handler(Looper.getMainLooper()).postDelayed({
            CloudBackupManager.startBackupWithPrecondition(this)
        }, 5000)

        with(FirebaseUtils) {
            subscribeToTopic(TOPIC_GENERAL_USER)
        }

        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    val intent = it.data
                    if (intent != null && intent.data != null) {
                        viewModel.importDataFromFile(intent.data!!)
                    } else {
                        showAlert(
                            null,
                            getString(R.string.something_went_wrong),
                            getString(R.string.ok),
                            null,
                            null,
                            null
                        )
                    }
                }
            }
    }

    private fun observeState() {

        purchaseStatusChecked.observe(this) {
            if (!it) {
                showAlert(
                    null,
                    "Could not retrieve purchase information. Premium features may not be available for now",
                    "Ok",
                    null,
                    null,
                    null
                )
            }
        }
    }

    override fun onStart() {
        super.onStart()
        AnalyticsManager.logEvent(AnalyticsManager.APP_RESUMED)

        if (InternetCheckerServiceImpl(this).isConnected) {
            playBillingUtils.checkIfAdFreeVersionPurchased(purchaseStatusChecked)
        } else {
            showAlert(
                null,
                getString(R.string.could_not_retrieve_purchase),
                getString(R.string.ok),
                null,
                null,
                null
            )
        }


        if (preferencesChangeListener == null) {
            preferencesChangeListener = PreferencesChangeListener(this)
        }
        PreferenceManager.getDefaultSharedPreferences(this)
            .registerOnSharedPreferenceChangeListener(preferencesChangeListener)
    }

    override fun onStop() {
        super.onStop()
        playBillingUtils.stopBillingClientConnection()
        PreferenceManager.getDefaultSharedPreferences(this)
            .unregisterOnSharedPreferenceChangeListener(preferencesChangeListener)
    }

    override fun onResume() {
        super.onResume()
        (application as ApplicationObject).activityResumed()
    }

    override fun onPause() {
        super.onPause()
        (application as ApplicationObject).activityPaused()
    }

    override fun initializeView() {
        setupActionBar()
        val navigationItemSelectionListener = NavigationItemSelectionListener(
            this,
            binding,
        )
        binding.navView.setNavigationItemSelectedListener(navigationItemSelectionListener)
        val homeNavItem = binding.navView.menu.findItem(R.id.nav_home)
        val howToUseNavItem = binding.navView.menu.findItem(R.id.nav_how)
        navigationItemSelectionListener.onNavigationItemSelected(homeNavItem)
        homeNavItem.isChecked = true

        val shouldShow = SharedPreferenceUtils.getInstance(this)!!
            .getInt(Constants.KEY_SHOW_HOW_TO_USE_NAV_MENU, 0)
        howToUseNavItem.isVisible = shouldShow > 0
    }

    private fun setupActionBar() {
        toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        setSupportActionBar(mToolbar)
        setupDrawer()
    }

    override fun goBackToLoginScreen() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onLogoutSuccess() {
        SharedPreferenceUtils.getInstance(this)?.putString(Constants.KEY_USER, "")
        goBackToLoginScreen()
    }

    override fun startLoadingApp() {
        viewModel.init(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionUtils.startNotificationPermissionLauncher()
        } else {
            notificationPermissionUtils.hasNotificationPermissionGranted = true
        }
    }

    override fun showAllTimeTotalExpense(amount: String?) {
        val str = "${getString(R.string.expense)}: $amount"
        binding.totalExpenseAmountTv.text = str
    }

    override fun showTotalBalance(amount: String?) {
        val str = "${getString(R.string.balance)}: $amount"
        binding.totalAccountAmountTv.text = str
    }

    override fun stayOnCurrencyScreen() {
        val intent = Intent(this, InitialPreferenceActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun setBalanceTextColor(colorId: Int) {
        binding.totalAccountAmountTv.setTextColor(ContextCompat.getColor(this, colorId))
    }

    override fun onDataUpdated() {
        updateSummary()
        refreshChart()
    }

    fun onBackButtonPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START)
        } else {
            val t = System.currentTimeMillis()
            if (t - backPressedTime > 2000) {
                backPressedTime = t
                Utils.showToast(this, getString(R.string.press_once_again_to_close_app))
            } else {
                finish()
            }
        }
    }

    fun showBackButton() {
        toggle?.isDrawerIndicatorEnabled = false
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        toggle?.syncState()
        mToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setupDrawer() {
        toggle?.isDrawerIndicatorEnabled = true
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        toggle?.syncState()
        mToolbar.setNavigationOnClickListener {
            mDrawerLayout.openDrawer(GravityCompat.START)
        }
    }

    fun hideBackButton() {
        setupDrawer()
        //hide background blur
        val fragment = supportFragmentManager.findFragmentByTag(ViewShadeFragment::class.java.name)
        if (fragment != null) {
            FragmentLoader.remove(this, fragment, null, 1)
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toggle?.syncState()
    }

    fun openAddExpenseScreen(
        categoryExpense: CategoryExpense?,
        title: String = getString(R.string.add_expense),
        purpose: Purpose = Purpose.ADD
    ) {
        //blur back screen
        FragmentLoader.load(this, ViewShadeFragment(), null, ViewShadeFragment::class.java.name, 1)

        supportActionBar?.title = title
        val expenseFragment = ExpenseFragment()
        expenseFragment.purpose = purpose
        val wrappedCategoryExpense = Parcels.wrap(categoryExpense)
        val bundle = Bundle()
        bundle.putParcelable(Constants.CATEGORYEXPENSE_PARCEL, wrappedCategoryExpense)
        expenseFragment.arguments = bundle
        FragmentLoader.load(this, expenseFragment, title, ExpenseFragment::class.java.name)
        showBackButton()
    }

    val mToolbar: Toolbar
        get() = binding.toolbar

    val mDrawerLayout: DrawerLayout
        get() = binding.drawerLayout

    fun updateTitle() {
        when (selectedTabPosition) {
            0 -> mToolbar.title = getString(R.string.title_home)
            1 -> mToolbar.title = getString(R.string.title_transaction)
            2 -> mToolbar.title = getString(R.string.title_overview)
            3 -> mToolbar.title = getString(R.string.title_accounts)
        }
    }

    var selectedTabPosition = 0
    override fun setTitle(titleId: Int) {
        mToolbar.title = getString(titleId)
    }

    fun updateSummary() {
        viewModel.updateAllTimeExpense()
        val mainFragment =
            supportFragmentManager.findFragmentByTag(MainFragment::class.java.name) as MainFragment?
        mainFragment?.viewModel?.gerCurrentExpense(viewModel.startTime, viewModel.endTime)
    }

    fun refreshChart() {
        val fragment =
            supportFragmentManager.findFragmentByTag(MainFragment::class.java.name) as MainFragment?
        fragment?.refreshChart()
    }

    fun onUpdateCategory() {
        updateSummary()
        val mainFragment =
            supportFragmentManager.findFragmentByTag(MainFragment::class.java.name) as MainFragment?
        val homeFragment =
            mainFragment?.childFragmentManager?.findViewPagerFragmentByTag<HomeFragment>(
                R.id.viewpager, 0
            )
        homeFragment?.init()
        val overViewFragment =
            mainFragment?.childFragmentManager?.findViewPagerFragmentByTag<OverViewFragment>(
                R.id.viewpager, 2
            )
        overViewFragment?.refreshPage()
        mainFragment?.refreshChart()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val message = intent?.getStringExtra(Constants.KEY_MESSAGE)
        if (!message.isNullOrEmpty()) {
            with(AnalyticsManager) { logEvent("${PN_VIEWED}: $message") }
            AlertDialogUtils.show(this, null, message, getString(R.string.ok), null, null, null)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == FilePickerUtils.REQ_CODE_READ_EXTERNAL_STORAGE_PERMISSION && grantResults.isNotEmpty()
            && grantResults[0]
            == PackageManager.PERMISSION_GRANTED
        ) {
            FilePickerUtils().openFilePicker(this)
        }
    }

    private fun setPeriodicReminderToAskAddingExpense() {
        val sharedPreferenceUtils = SharedPreferenceUtils.getInstance(this)
        if (sharedPreferenceUtils?.getString(
                Constants.KEY_PERIODIC_ADD_EXPENSE_REMINDER_WORKER_ID, ""
            )!!.isEmpty()
        ) {
            val requestId = WorkManagerEnqueuer().enqueue<AskToAddEntryWorker>(
                this,
                WorkRequestType.PERIODIC,
                Constants.DELAY_PERIODIC_REMINDER_TO_ADD_EXPENSE,
                null
            )
            sharedPreferenceUtils.putString(
                Constants.KEY_PERIODIC_ADD_EXPENSE_REMINDER_WORKER_ID, requestId
            )
        }
    }

    fun getPlayBillingUtils() = playBillingUtils

    fun getResultListener() = resultLauncher

    companion object {
        @JvmField
        var addedExpenseCount = 1
    }
}
