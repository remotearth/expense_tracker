package com.remotearthsolutions.expensetracker.activities

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.preference.PreferenceManager
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.navigation.NavigationView
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.remotearthsolutions.expensetracker.BuildConfig
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.callbacks.InAppBillingCallback
import com.remotearthsolutions.expensetracker.contracts.BaseView
import com.remotearthsolutions.expensetracker.contracts.MainContract
import com.remotearthsolutions.expensetracker.databaseutils.DatabaseClient
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryExpense
import com.remotearthsolutions.expensetracker.entities.User
import com.remotearthsolutions.expensetracker.fragments.*
import com.remotearthsolutions.expensetracker.fragments.main.MainFragment
import com.remotearthsolutions.expensetracker.services.FileProcessingServiceImp
import com.remotearthsolutions.expensetracker.services.FirebaseServiceImpl
import com.remotearthsolutions.expensetracker.services.PurchaseListener
import com.remotearthsolutions.expensetracker.utils.*
import com.remotearthsolutions.expensetracker.viewmodels.MainViewModel
import com.remotearthsolutions.expensetracker.viewmodels.viewmodel_factory.BaseViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import org.parceler.Parcels
import org.solovyev.android.checkout.*
import org.solovyev.android.checkout.Inventory.Products
import java.io.File
import javax.annotation.Nonnull

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener,
    MainContract.View, InAppBillingCallback, Inventory.Callback,
    SharedPreferences.OnSharedPreferenceChangeListener {

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
        setContentView(R.layout.activity_main)
        val db = DatabaseClient.getInstance(this)?.appDatabase

        viewModel =
            ViewModelProviders.of(this, BaseViewModelFactory {
                MainViewModel(
                    this,
                    FirebaseServiceImpl(this),
                    db?.accountDao()!!,
                    db.expenseDao(),
                    db.categoryDao(),
                    db.categoryExpenseDao(),
                    FileProcessingServiceImp()
                )
            }).get(MainViewModel::class.java)

        val userStr = SharedPreferenceUtils.getInstance(this)?.getString(Constants.KEY_USER, "")
        val user =
            Gson().fromJson(
                userStr,
                User::class.java
            )
        viewModel.checkAuthectication(user)

        Handler().postDelayed({
            InAppUpdateUtils().requestUpdateApp(this@MainActivity)
        }, 2000)

        if (BuildConfig.DEBUG) {
            FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w(MainActivity::class.java.name, "getInstanceId failed", task.exception)
                        return@OnCompleteListener
                    }
                    // Get new Instance ID token
                    val token = task.result?.token
                    Log.d(MainActivity::class.java.name, "Firebase Token: $token")
                })
        }
    }

    override fun onStart() {
        super.onStart()
        checkoutUtils.start()
        checkoutUtils.createPurchaseFlow(purchaseListener)
        PreferenceManager.getDefaultSharedPreferences(this)
            .registerOnSharedPreferenceChangeListener(this)
    }

    override fun onStop() {
        super.onStop()
        checkoutUtils.stop()
        PreferenceManager.getDefaultSharedPreferences(this)
            .unregisterOnSharedPreferenceChangeListener(this)
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
        nav_view.setNavigationItemSelectedListener(this)
        val homeNavItem = nav_view.menu.getItem(0)
        onNavigationItemSelected(homeNavItem)
        homeNavItem.isChecked = true
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar)
        toggle = ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

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
        checkoutUtils.start()
        checkoutUtils.load(this, productId)
    }

    override fun showTotalExpense(amount: String?) {
        val str = "${getString(R.string.expense)}: $amount"
        totalExpenseAmountTv.text = str
    }

    override fun showTotalBalance(amount: String?) {
        val str = "${getString(R.string.balance)}: $amount"
        totalAccountAmountTv.text = str
    }

    override fun stayOnCurrencyScreen() {
        val intent = Intent(this, CurrencySelectionActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun setBalanceTextColor(colorId: Int) {
        totalAccountAmountTv.setTextColor(ContextCompat.getColor(this, colorId))
    }

    fun onBackButtonPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
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
                finish()
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                val fragment =
                    supportFragmentManager.findFragmentByTag(MainFragment::class.java.name)
                if (fragment == null) {
                    mainFragment = MainFragment()
                    mainFragment!!.setActionBar(supportActionBar, getString(R.string.title_home))
                    val fragmentTransaction =
                        supportFragmentManager.beginTransaction()
                    fragmentTransaction.replace(
                        R.id.framelayout,
                        mainFragment!!,
                        MainFragment::class.java.name
                    )
                    fragmentTransaction.commit()
                } else {
                    refreshChart()
                }
            }
            R.id.nav_categories -> {
                val tag = CategoryFragment::class.java.name
                supportActionBar!!.title = getString(R.string.menu_categories)
                val categoryFragment = CategoryFragment()
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(
                    R.anim.slide_in_up,
                    0,
                    0,
                    R.anim.slide_out_down
                )
                fragmentTransaction.add(
                    R.id.framelayout,
                    categoryFragment,
                    tag
                )
                fragmentTransaction.addToBackStack(tag)
                fragmentTransaction.commit()

                showBackButton()
                drawer_layout.closeDrawer(GravityCompat.START)
                return false
            }
            R.id.nav_import_data -> {

                showAlert(
                    resources.getString(R.string.attention),
                    resources.getString(R.string.will_replace_your_current_entries),
                    resources.getString(R.string.yes),
                    resources.getString(R.string.cancel),
                    object : BaseView.Callback {
                        override fun onOkBtnPressed() {
                            PermissionUtils().writeExternalStoragePermission(
                                this@MainActivity,
                                object : PermissionListener {
                                    override fun onPermissionGranted(response: PermissionGrantedResponse) {
                                        val allCsvFile =
                                            viewModel.allCsvFile
                                        if (allCsvFile == null || allCsvFile.isEmpty()) {
                                            Toast.makeText(
                                                this@MainActivity,
                                                resources.getString(R.string.no_supported_file),
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            return
                                        }
                                        val csvList: Array<String> =
                                            allCsvFile.toTypedArray()
                                        val dialogBuilder =
                                            AlertDialog.Builder(this@MainActivity)
                                        dialogBuilder.setTitle(resources.getString(R.string.select_csv))
                                        dialogBuilder.setItems(
                                            csvList
                                        ) { _: DialogInterface?, item: Int ->
                                            val selectedText =
                                                csvList[item]
                                            val filePath = File(
                                                Environment.getExternalStorageDirectory().absolutePath,
                                                selectedText
                                            ).absolutePath
                                            viewModel.importDataFromFile(filePath)
                                            FirebaseEventLogUtils.logCustom(
                                                this@MainActivity,
                                                "Data Imported"
                                            )
                                            showProgress(resources.getString(R.string.please_wait))
                                            Handler()
                                                .postDelayed({
                                                    updateSummary()
                                                    refreshChart()
                                                    hideProgress()
                                                }, 3000)
                                        }
                                        val alertDialogObject =
                                            dialogBuilder.create()
                                        alertDialogObject.show()
                                    }

                                    override fun onPermissionDenied(response: PermissionDeniedResponse) {
                                        showAlert(
                                            "",
                                            resources.getString(R.string.read_write_permission_is_needed),
                                            resources.getString(R.string.ok),
                                            null,
                                            null
                                        )
                                    }

                                    override fun onPermissionRationaleShouldBeShown(
                                        permission: PermissionRequest,
                                        token: PermissionToken
                                    ) {
                                        Toast.makeText(
                                            this@MainActivity,
                                            resources.getString(R.string.read_write_permission_is_needed_enable_from_settings),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                })
                        }

                        override fun onCancelBtnPressed() {}
                    })
                drawer_layout.closeDrawer(GravityCompat.START)
                return false
            }
            R.id.nav_export_data -> {
                viewModel.saveExpenseToCSV(this)
                drawer_layout.closeDrawer(GravityCompat.START)
                return false
            }
            R.id.nav_settings -> {
                val settingsFragment = SettingsFragment()
                supportActionBar!!.title = getString(R.string.menu_settings)
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(
                    R.anim.slide_in_up,
                    0,
                    0,
                    R.anim.slide_out_down
                )
                fragmentTransaction.addToBackStack(SettingsFragment::class.java.name)
                fragmentTransaction.add(
                    R.id.framelayout,
                    settingsFragment,
                    SettingsFragment::class.java.name
                ).commit()

                showBackButton()
                drawer_layout.closeDrawer(GravityCompat.START)
                return false
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
                drawer_layout.closeDrawer(GravityCompat.START)
                return false
            }
            R.id.nav_purchase -> {
                showAlert(resources.getString(R.string.whatsinpremium),
                    resources.getString(R.string.premium_features),
                    resources.getString(R.string.ok),
                    null,
                    object : BaseView.Callback {
                        override fun onOkBtnPressed() {

                            if (!isDeviceOnline) {
                                showAlert(
                                    "",
                                    resources.getString(R.string.internet_connection_needed),
                                    resources.getString(R.string.ok),
                                    null,
                                    null
                                )
                                return
                            }
                            checkoutUtils.checkout.whenReady(object : Checkout.EmptyListener() {
                                override fun onReady(@Nonnull requests: BillingRequests) {
                                    requests.purchase(
                                        ProductTypes.IN_APP,
                                        productId!!,
                                        null,
                                        checkoutUtils.purchaseFlow
                                    )
                                }
                            })
                        }

                        override fun onCancelBtnPressed() {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }
                    })
                drawer_layout.closeDrawer(GravityCompat.START)
                return false
            }
            R.id.nav_contact_us -> {

                ShareCompat.IntentBuilder.from(this)
                    .setType("message/rfc822")
                    .addEmailTo("remotearth.solutions@gmail.com")
                    .setSubject("About Expense Tracker")
                    //.setText(body)
                    //.setHtmlText(body) //If you are using HTML in your body text
                    //.setChooserTitle()
                    .startChooser()
                drawer_layout.closeDrawer(GravityCompat.START)
                return false
            }
            R.id.nav_rate_us -> {
                RequestReviewUtils.openApplinkForReview(this)
                drawer_layout.closeDrawer(GravityCompat.START)
                return false
            }
            R.id.nav_about -> {
                supportActionBar!!.title = getString(R.string.menu_about)
                val aboutFragment = AboutFragment()
                val fragmentTransaction =
                    supportFragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(
                    R.anim.slide_in_up,
                    0,
                    0,
                    R.anim.slide_out_down
                )
                fragmentTransaction.add(
                    R.id.framelayout,
                    aboutFragment,
                    AboutFragment::class.java.name
                )
                fragmentTransaction.addToBackStack(AboutFragment::class.java.name)
                fragmentTransaction.commit()

                showBackButton()
                drawer_layout.closeDrawer(GravityCompat.START)
                return false
            }
            R.id.nav_privacypolicy -> {
                drawer_layout.closeDrawer(GravityCompat.START)
                if (!isDeviceOnline) {
                    showAlert(
                        getString(R.string.warning),
                        getString(R.string.internet_connection_needed),
                        getString(R.string.ok), null, null
                    )
                    return false
                }

                supportActionBar!!.title = getString(R.string.privacy_policy)
                val webViewFragment = WebViewFragment()
                val bundle = Bundle().apply {
                    putString(Constants.KEY_URL, Constants.URL_PRIVACY_POLICY)
                }
                webViewFragment.arguments = bundle

                val fragmentTransaction =
                    supportFragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(
                    R.anim.slide_in_up,
                    0,
                    0,
                    R.anim.slide_out_down
                )
                fragmentTransaction.addToBackStack(AboutFragment::class.java.name)
                fragmentTransaction.add(
                    R.id.framelayout, webViewFragment, WebViewFragment::class.java.name
                ).commit()

                showBackButton()
                drawer_layout.closeDrawer(GravityCompat.START)
                return false
            }
            R.id.nav_licenses -> {
                drawer_layout.closeDrawer(GravityCompat.START)
                if (!isDeviceOnline) {
                    showAlert(
                        getString(R.string.warning),
                        getString(R.string.internet_connection_needed),
                        getString(R.string.ok), null, null
                    )
                    return false
                }

                supportActionBar!!.title = getString(R.string.menu_licenses)
                val webViewFragment = WebViewFragment()
                val bundle = Bundle().apply {
                    putString(Constants.KEY_URL, Constants.URL_THIRD_PARTY_LICENSES)
                }
                webViewFragment.arguments = bundle

                val fragmentTransaction =
                    supportFragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(
                    R.anim.slide_in_up,
                    0,
                    0,
                    R.anim.slide_out_down
                )
                fragmentTransaction.addToBackStack(AboutFragment::class.java.name)
                fragmentTransaction.add(
                    R.id.framelayout, webViewFragment, WebViewFragment::class.java.name
                ).commit()
                showBackButton()
                drawer_layout.closeDrawer(GravityCompat.START)
                return false
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun showBackButton() {
        toggle.isDrawerIndicatorEnabled = false
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        toggle.syncState()
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    fun hideBackButton() {
        toggle.isDrawerIndicatorEnabled = true
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        toggle.syncState()
        toolbar.setNavigationOnClickListener {
            mDrawerLayout.openDrawer(GravityCompat.START)
        }
    }


    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }

    fun openAddExpenseScreen(
        categoryExpense: CategoryExpense?,
        title: String = getString(R.string.add_expense),
        purpose: ExpenseFragment.Purpose = ExpenseFragment.Purpose.ADD
    ) {
        supportActionBar!!.title = title
        val expenseFragment = ExpenseFragment()
        expenseFragment.purpose = purpose
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

        showBackButton()
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

    val mToolbar: Toolbar
        get() = toolbar

    val mDrawerLayout: DrawerLayout
        get() = drawer_layout

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
        var expenseAddededCount = 1
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            Constants.PREF_CURRENCY, Constants.PREF_TIME_FORMAT -> {
                refreshChart()
            }
        }
    }
}