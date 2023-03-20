package com.remotearthsolutions.expensetracker.activities.main

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.activities.ApplicationObject
import com.remotearthsolutions.expensetracker.activities.helpers.FragmentLoader
import com.remotearthsolutions.expensetracker.contracts.BaseView
import com.remotearthsolutions.expensetracker.databinding.ActivityMainBinding
import com.remotearthsolutions.expensetracker.fragments.AboutFragment
import com.remotearthsolutions.expensetracker.fragments.CategoryFragment
import com.remotearthsolutions.expensetracker.fragments.ScheduledExpenseFragment
import com.remotearthsolutions.expensetracker.fragments.WebViewFragment
import com.remotearthsolutions.expensetracker.fragments.main.MainFragment
import com.remotearthsolutions.expensetracker.fragments.settings.SettingsFragment
import com.remotearthsolutions.expensetracker.utils.*
import com.remotearthsolutions.expensetracker.utils.billing.BillingClientWrapper


class NavigationItemSelectionListener(
    private val mainActivity: MainActivity,
    private val binding: ActivityMainBinding
) :
    NavigationView.OnNavigationItemSelectedListener {

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        with(mainActivity) {
            when (item.itemId) {
                R.id.nav_home -> {
                    val fragment =
                        supportFragmentManager.findFragmentByTag(MainFragment::class.java.name)
                    if (fragment == null) {
                        val mainFragment = MainFragment()
                        FragmentLoader.load(
                            this, mainFragment, getString(R.string.menu_home),
                            MainFragment::class.java.name, 1
                        )
                    } else {
                        refreshChart()
                    }
                }
                R.id.nav_categories -> {
                    FragmentLoader.load(
                        this, CategoryFragment(), getString(R.string.menu_categories),
                        CategoryFragment::class.java.name
                    )
                    showBackButton()
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    return false
                }
                R.id.nav_scheduled_expense -> {
                    FragmentLoader.load(
                        this,
                        ScheduledExpenseFragment(),
                        getString(R.string.menu_scheduled_expense),
                        ScheduledExpenseFragment::class.java.name
                    )
                    showBackButton()
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    return false
                }
                R.id.nav_import_data -> {

                    showAlert(
                        resources.getString(R.string.attention),
                        resources.getString(R.string.will_replace_your_current_entries),
                        resources.getString(R.string.yes),
                        resources.getString(R.string.cancel), null,
                        object : BaseView.Callback {
                            override fun onOkBtnPressed() {
                                PermissionUtils().writeExternalStoragePermission(this@with,
                                    object : PermissionListener {
                                        override fun onPermissionGranted(response: PermissionGrantedResponse) {
                                            val intent = Intent(Intent.ACTION_GET_CONTENT)
                                            intent.type = "*/*"
                                            startActivityForResult(
                                                Intent.createChooser(
                                                    intent,
                                                    "Choose a .csv file"
                                                ), 101
                                            )
                                        }

                                        override fun onPermissionDenied(response: PermissionDeniedResponse) {
                                            showAlert(
                                                "",
                                                resources.getString(R.string.read_write_permission_is_needed),
                                                resources.getString(R.string.ok), null, null, null
                                            )
                                        }

                                        override fun onPermissionRationaleShouldBeShown(
                                            permission: PermissionRequest, token: PermissionToken
                                        ) {
                                            Toast.makeText(
                                                this@with,
                                                resources.getString(R.string.read_write_permission_is_needed_enable_from_settings),
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    })
                            }

                            override fun onCancelBtnPressed() {}
                        })
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    return false
                }
                R.id.nav_export_data -> {
                    viewModel.saveExpenseToCSV(this)
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    return false
                }
                R.id.nav_backup_sync -> {
                    val user =
                        SharedPreferenceUtils.getInstance(this)?.getString(Constants.KEY_USER, "")!!
                    val isLoggedIn = user != "guest" && user.isNotEmpty()

                    viewModel.backupOrSync(
                        this, (application as ApplicationObject).isPremium,
                        isDeviceOnline, isLoggedIn
                    ) {
                        AlertDialogUtils.show(
                            this,
                            "",
                            getString(R.string.you_can_backup) + "\n\n" +
                                    getString(R.string.what_you_want_to_do),
                            getString(R.string.backup_sync),
                            getString(R.string.download), null,
                            object : BaseView.Callback {
                                override fun onOkBtnPressed() {
                                    viewModel.backupToCloud(this@with, user)
                                }

                                override fun onCancelBtnPressed() {
                                    viewModel.downloadFromCloud(this@with, user)
                                }
                            },
                            true
                        )
                    }

                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    return false
                }
                R.id.nav_settings -> {
                    FragmentLoader.load(
                        this,
                        SettingsFragment(), getString(R.string.menu_settings),
                        SettingsFragment::class.java.name
                    )
                    showBackButton()
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    with(AnalyticsManager){
                        logEvent(SETTINGS_PAGE_VIEWED)
                    }
                    return false
                }
                R.id.nav_logout -> {
                    showAlert(
                        "",
                        getString(R.string.are_you_sure_you_want_to_logout),
                        getString(R.string.yes),
                        getString(R.string.no), null,
                        object : BaseView.Callback {
                            override fun onOkBtnPressed() {
                                viewModel.performLogout()
                            }

                            override fun onCancelBtnPressed() {}
                        })
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    return false
                }
                R.id.nav_purchase -> {
                    showAlert(resources.getString(R.string.whatsinpremium),
                        resources.getString(R.string.premium_features),
                        resources.getString(R.string.ok), null, null,
                        object : BaseView.Callback {
                            override fun onOkBtnPressed() {

                                if (!isDeviceOnline) {
                                    showAlert(
                                        "",
                                        resources.getString(R.string.internet_connection_needed),
                                        resources.getString(R.string.ok),
                                        null,
                                        null, null
                                    )
                                    return
                                }

                                mainActivity.getPlayBillingUtils().showAvailableProducts()

//                                BillingClientWrapper(mainActivity).startBillingConnection()

//                                checkoutUtils.checkout.whenReady(object : Checkout.EmptyListener() {
//                                    override fun onReady(@Nonnull requests: BillingRequests) {
//                                        requests.purchase(
//                                            ProductTypes.IN_APP,
//                                            productId,
//                                            null,
//                                            checkoutUtils.purchaseFlow
//                                        )
//                                    }
//                                })
                            }
                        })
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    return false
                }
                R.id.nav_how -> {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    val status = showWebUrl(
                        Constants.URL_HOW_TO_USE,
                        getString(R.string.menu_how_to)
                    )
                    showBackButton()
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    return status
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
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    return false
                }
                R.id.nav_share -> {
                    ShareCompat.IntentBuilder.from(this)
                        .setType("text/plain")
                        .setText(getString(R.string.share_app_text) + "\n\nhttps://bit.ly/2SOTaQj")
                        .setChooserTitle("Share with...")
                        .startChooser()
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    with(AnalyticsManager) { logEvent(APP_SHARE_INTENT) }
                    return false
                }
                R.id.nav_rate_us -> {
                    RequestReviewUtils.openApplinkForReview(this)
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    return false
                }
                R.id.nav_about -> {
                    FragmentLoader.load(
                        this, AboutFragment(), getString(R.string.menu_about),
                        AboutFragment::class.java.name
                    )
                    showBackButton()
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    with(AnalyticsManager){
                        logEvent(ABOUT_PAGE_VIEWED)
                    }
                    return false
                }
                R.id.nav_privacypolicy -> {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    val status = showWebUrl(
                        Constants.URL_PRIVACY_POLICY,
                        getString(R.string.privacy_policy)
                    )
                    showBackButton()
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    with(AnalyticsManager){
                        logEvent(PRIVACY_PAGE_VIEWED)
                    }
                    return status
                }
                R.id.nav_licenses -> {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    val status = showWebUrl(
                        Constants.URL_THIRD_PARTY_LICENSES,
                        getString(R.string.menu_licenses)
                    )
                    showBackButton()
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    with(AnalyticsManager){
                        logEvent(LICENSE_PAGE_VIEWED)
                    }
                    return status
                }
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
        return true
    }

    private fun showWebUrl(url: String, title: String): Boolean {
        with(mainActivity) {
            if (!isDeviceOnline) {
                showAlert(
                    getString(R.string.warning),
                    getString(R.string.internet_connection_needed),
                    getString(R.string.ok), null, null, null
                )
                return false
            }
            val webViewFragment = WebViewFragment()
            val bundle = Bundle().apply {
                putString(Constants.KEY_URL, url)
            }
            webViewFragment.arguments = bundle
            FragmentLoader.load(this, webViewFragment, title, WebViewFragment::class.java.name)
            return false
        }
    }
}
