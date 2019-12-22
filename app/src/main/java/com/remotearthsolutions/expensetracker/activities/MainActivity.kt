package com.remotearthsolutions.expensetracker.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.callbacks.InAppBillingCallback
import com.remotearthsolutions.expensetracker.contracts.BaseView
import com.remotearthsolutions.expensetracker.contracts.MainContract
import com.remotearthsolutions.expensetracker.databaseutils.DatabaseClient
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.CategoryExpense
import com.remotearthsolutions.expensetracker.databinding.ActivityMainBinding
import com.remotearthsolutions.expensetracker.entities.User
import com.remotearthsolutions.expensetracker.fragments.*
import com.remotearthsolutions.expensetracker.fragments.main.MainFragment
import com.remotearthsolutions.expensetracker.services.FirebaseServiceImpl
import com.remotearthsolutions.expensetracker.services.PurchaseListener
import com.remotearthsolutions.expensetracker.utils.CheckoutUtils
import com.remotearthsolutions.expensetracker.utils.Constants
import com.remotearthsolutions.expensetracker.utils.SharedPreferenceUtils
import com.remotearthsolutions.expensetracker.viewmodels.MainViewModel
import com.remotearthsolutions.expensetracker.viewmodels.viewmodel_factory.BaseViewModelFactory
import org.parceler.Parcels
import org.solovyev.android.checkout.Inventory
import org.solovyev.android.checkout.Inventory.Products
import org.solovyev.android.checkout.ProductTypes
import org.solovyev.android.checkout.Purchase
import javax.annotation.Nonnull

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener,
    MainContract.View, InAppBillingCallback, Inventory.Callback {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var toggle: ActionBarDrawerToggle
    private var mainFragment: MainFragment? = null
    private lateinit var checkoutUtils: CheckoutUtils

    private var backPressedTime: Long = 0
    private var purchaseListener: PurchaseListener? = null
    private var productId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productId = (application as ApplicationObject).adProductId
        checkoutUtils = CheckoutUtils.getInstance(this)!!
        checkoutUtils.start()
        purchaseListener = PurchaseListener(this, this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val db = DatabaseClient.getInstance(this)?.appDatabase

        viewModel =
            ViewModelProviders.of(this, BaseViewModelFactory {
                MainViewModel(this, FirebaseServiceImpl(this), db?.accountDao()!!, db.expenseDao())
            }).get(MainViewModel::class.java)

        val userStr = SharedPreferenceUtils.getInstance(this)?.getString(Constants.KEY_USER, "")
        val user =
            Gson().fromJson(
                userStr,
                User::class.java
            )
        viewModel.checkAuthectication(user)
    }

    override fun onStart() {
        super.onStart()
        checkoutUtils.start()
        checkoutUtils.createPurchaseFlow(purchaseListener)
    }

    override fun onStop() {
        super.onStop()
        checkoutUtils.stop()
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
        binding.navView.setNavigationItemSelectedListener(this)
        val homeNavItem = binding.navView.menu.getItem(0)
        onNavigationItemSelected(homeNavItem)
        homeNavItem.isChecked = true
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbar)
        toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    override fun goBackToLoginScreen() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onLogoutSuccess() {
        goBackToLoginScreen()
    }

    override fun startLoadingApp() {
        viewModel.init(this)
        checkoutUtils.start()
        checkoutUtils.load(this, productId)
    }

    override fun showTotalExpense(amount: String?) {
        val str = "${getString(R.string.expense)}: $amount"
        binding.totalExpenseAmountTv.text = str
    }

    override fun showTotalBalance(amount: String?) {
        val str = "${getString(R.string.balance)}: $amount"
        binding.totalAccountAmountTv.text = str
    }

    override fun stayOnCurrencyScreen() {
        val intent = Intent(this, CurrencySelectionActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun setBalanceTextColor(colorId: Int) {
        binding.totalAccountAmountTv.setTextColor(ContextCompat.getColor(this, colorId))
    }

    override fun onBackPressed() {
        val expenseFragment =
            supportFragmentManager.findFragmentByTag(
                ExpenseFragment::class.java.name
            )
        val webViewFragment =
            supportFragmentManager.findFragmentByTag(
                WebViewFragment::class.java.name
            )
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else if (expenseFragment != null) {
            val fragmentManager = supportFragmentManager
            val ft = fragmentManager.beginTransaction()
            ft.setCustomAnimations(R.anim.slide_in_up, 0, 0, R.anim.slide_out_down)
            ft.remove(expenseFragment)
            fragmentManager.popBackStack()
            ft.commit()
            supportActionBar!!.title = getString(R.string.home)
        } else if (webViewFragment != null) {
            val fragmentManager = supportFragmentManager
            val ft = fragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            ft.remove(webViewFragment)
            ft.commit()
            fragmentManager.popBackStack()
            setupActionBar()
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        } else {
            val t = System.currentTimeMillis()
            if (t - backPressedTime > 2000) {
                backPressedTime = t
                Toast.makeText(
                    this,
                    getString(R.string.press_once_again_to_close_app),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                CheckoutUtils.clearInstance()
                super.onBackPressed()
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                mainFragment = MainFragment()
                mainFragment!!.setActionBar(supportActionBar, getString(R.string.home))
                val fragmentTransaction =
                    supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(
                    R.id.framelayout,
                    mainFragment!!,
                    MainFragment::class.java.name
                )
                fragmentTransaction.commit()
            }
            R.id.nav_categories -> {
                supportActionBar!!.title = getString(R.string.menu_categories)
                val categoryFragment = CategoryFragment()
                val fragmentTransaction =
                    supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(
                    R.id.framelayout,
                    categoryFragment,
                    CategoryFragment::class.java.name
                )
                fragmentTransaction.commit()
            }
            R.id.nav_settings -> {
                val settingsFragment = SettingsFragment()
                supportActionBar!!.title = getString(R.string.menu_settings)
                supportFragmentManager.beginTransaction().replace(
                    R.id.framelayout,
                    settingsFragment,
                    SettingsFragment::class.java.name
                ).commit()
            }
            R.id.nav_logout -> {
                showAlert(
                    "",
                    getString(R.string.are_you_sure_you_want_to_logout),
                    getString(R.string.yes),
                    getString(R.string.no),
                    object : BaseView.Callback {
                        override fun onOkBtnPressed() {
                            viewModel.performLogout()
                        }

                        override fun onCancelBtnPressed() {}
                    })
                drawerLayout.closeDrawer(GravityCompat.START)
                return false
            }
            R.id.nav_about -> {
                supportActionBar!!.title = getString(R.string.menu_about)
                val aboutFragment = AboutFragment()
                val fragmentTransaction =
                    supportFragmentManager.beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                fragmentTransaction.replace(
                    R.id.framelayout,
                    aboutFragment,
                    AboutFragment::class.java.name
                )
                fragmentTransaction.commit()
            }
            R.id.nav_privacypolicy -> {
                supportActionBar!!.title = getString(R.string.privacy_policy)
                drawerLayout.closeDrawer(GravityCompat.START)

                val webViewFragment = WebViewFragment()
                val bundle = Bundle().apply {
                    putString(Constants.KEY_URL, Constants.URL_PRIVACY_POLICY)
                }
                webViewFragment.arguments = bundle

                val fragmentTransaction =
                    supportFragmentManager.beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                fragmentTransaction.replace(
                    R.id.framelayout, webViewFragment, "privacy_screen"
                ).commit()

            }
            R.id.nav_licenses -> {
                supportActionBar!!.title = getString(R.string.menu_licenses)
                val licenseFragment = LicenseFragment()
                val fragmentTransaction =
                    supportFragmentManager.beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                fragmentTransaction.replace(
                    R.id.framelayout,
                    licenseFragment,
                    AboutFragment::class.java.name
                )
                fragmentTransaction.commit()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }

    fun openAddExpenseScreen(categoryExpense: CategoryExpense?) {
        supportActionBar!!.title = getString(R.string.add_expense)
        val expenseFragment = ExpenseFragment()
        val wrappedCategoryExpense = Parcels.wrap(categoryExpense)
        val bundle = Bundle()
        bundle.putParcelable(
            Constants.CATEGORYEXPENSE_PARCEL,
            wrappedCategoryExpense
        )
        expenseFragment.arguments = bundle
        val fragmentTransaction =
            supportFragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_up, 0, 0, R.anim.slide_out_down)
        fragmentTransaction.add(
            R.id.framelayout,
            expenseFragment,
            ExpenseFragment::class.java.name
        )
        fragmentTransaction.addToBackStack(ExpenseFragment::class.java.name)
        fragmentTransaction.commit()
    }

    override fun onPurchaseSuccessListener(purchase: Purchase?) {
        (application as ApplicationObject).isPremium = true
        if (purchase?.sku == productId) {
            (application as ApplicationObject).appShouldShowAds(false)
        }
    }

    override fun onPurchaseFailedListener(error: String?) {
        showToast(error)
    }

    override fun onLoaded(@Nonnull products: Products) {
        val productId = (application as ApplicationObject).adProductId
        if (!products[ProductTypes.IN_APP].isPurchased(productId)) {
            (application as ApplicationObject).isPremium = false
            (application as ApplicationObject).appShouldShowAds(true)
        } else {
            (application as ApplicationObject).isPremium = true
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        CheckoutUtils.getInstance(this)?.checkout?.onActivityResult(requestCode, resultCode, data)
    }

    val toolbar: Toolbar
        get() = binding.toolbar

    val drawerLayout: DrawerLayout
        get() = binding.drawerLayout

    fun updateSummary(startTime: Long, endTime: Long) {
        viewModel.updateSummary(startTime, endTime)
    }

    fun updateSummary() {
        viewModel.updateSummary()
    }

    fun refreshChart() {
        mainFragment?.refreshChart()
    }

    companion object {
        @JvmField
        var expenseAddededCount = 0
    }
}