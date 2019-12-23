package com.remotearthsolutions.expensetracker.fragments

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.res.Resources
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.activities.ApplicationObject
import com.remotearthsolutions.expensetracker.activities.MainActivity
import com.remotearthsolutions.expensetracker.adapters.DashboardAdapter
import com.remotearthsolutions.expensetracker.contracts.BaseView
import com.remotearthsolutions.expensetracker.contracts.DashboardContract
import com.remotearthsolutions.expensetracker.databaseutils.DatabaseClient
import com.remotearthsolutions.expensetracker.entities.DashboardModel
import com.remotearthsolutions.expensetracker.services.FileProcessingServiceImp
import com.remotearthsolutions.expensetracker.utils.CheckoutUtils
import com.remotearthsolutions.expensetracker.utils.FabricAnswersUtils.logCustom
import com.remotearthsolutions.expensetracker.utils.PermissionUtils
import com.remotearthsolutions.expensetracker.viewmodels.DashboardViewModel
import com.remotearthsolutions.expensetracker.viewmodels.viewmodel_factory.BaseViewModelFactory
import kotlinx.android.synthetic.main.fragment_share.view.*
import org.solovyev.android.checkout.BillingRequests
import org.solovyev.android.checkout.Checkout.EmptyListener
import org.solovyev.android.checkout.ProductTypes
import java.io.File
import java.util.*
import javax.annotation.Nonnull

class DashboardFragment : BaseFragment(),
    DashboardContract.View {
    private lateinit var mView: View
    private var dashboardViewModel: DashboardViewModel? = null
    private var productId: String? = null
    private var checkoutUtils: CheckoutUtils? = null
    private var mContext: Context? = null
    private lateinit var mResources: Resources
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        mResources = context.resources
        checkoutUtils = CheckoutUtils.getInstance((mContext as Activity))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productId = ((mContext as Activity?)!!.application as ApplicationObject).adProductId
        val db = DatabaseClient.getInstance(mContext!!)?.appDatabase

        dashboardViewModel =
            ViewModelProviders.of(this, BaseViewModelFactory {
                DashboardViewModel(
                    this, db?.categoryExpenseDao()!!, db.expenseDao(),
                    db.categoryDao(), db.accountDao(), FileProcessingServiceImp()
                )
            }).get(DashboardViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_share, container, false)
        loaddashboarddata()
        return mView
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        checkoutUtils = CheckoutUtils.getInstance((mContext as Activity?)!!)
    }

    private fun loaddashboarddata() {
        val dashBoardList =
            ArrayList<DashboardModel>()
        dashBoardList.add(
            DashboardModel(
                R.drawable.ic_share,
                mResources.getString(R.string.export_data)
            )
        )
        dashBoardList.add(
            DashboardModel(
                R.drawable.ic_import,
                resources.getString(R.string.import_data)
            )
        )
        dashBoardList.add(
            DashboardModel(
                R.drawable.ic_cart,
                resources.getString(R.string.buy_product)
            )
        )
        val adapter = DashboardAdapter(mContext!!, dashBoardList)
        mView.dashboardlist.adapter = adapter
        mView.dashboardlist.onItemClickListener =
            AdapterView.OnItemClickListener setOnItemClickListener@{ _: AdapterView<*>?, _: View?, position: Int, _: Long ->
                when (position) {
                    0 -> dashboardViewModel!!.saveExpenseToCSV(context as Activity)
                    1 -> showAlert(
                        resources.getString(R.string.attention),
                        resources.getString(R.string.will_replace_your_current_entries),
                        resources.getString(R.string.yes),
                        resources.getString(R.string.cancel),
                        object : BaseView.Callback {
                            override fun onOkBtnPressed() {
                                PermissionUtils().writeExternalStoragePermission(
                                    context as Activity?,
                                    object : PermissionListener {
                                        override fun onPermissionGranted(response: PermissionGrantedResponse) {
                                            val allCsvFile =
                                                dashboardViewModel!!.allCsvFile
                                            if (allCsvFile == null || allCsvFile.isEmpty()) {
                                                Toast.makeText(
                                                    context,
                                                    resources.getString(R.string.no_supported_file),
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                return
                                            }
                                            val csvList: Array<String> =
                                                allCsvFile.toTypedArray()
                                            val dialogBuilder =
                                                AlertDialog.Builder(mContext!!)
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
                                                dashboardViewModel!!.importDataFromFile(filePath)
                                                logCustom("Data Imported")
                                                showProgress(resources.getString(R.string.please_wait))
                                                Handler()
                                                    .postDelayed({
                                                        (mContext as MainActivity?)!!.updateSummary()
                                                        (mContext as MainActivity?)!!.refreshChart()
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
                                                mContext,
                                                resources.getString(R.string.read_write_permission_is_needed_enable_from_settings),
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    })
                            }

                            override fun onCancelBtnPressed() {}
                        })
                    2 -> {
                        if (!isDeviceOnline) {
                            showAlert(
                                "",
                                resources.getString(R.string.internet_connection_needed),
                                resources.getString(R.string.ok),
                                null,
                                null
                            )
                            return@setOnItemClickListener
                        }
                        checkoutUtils!!.checkout.whenReady(object : EmptyListener() {
                            override fun onReady(@Nonnull requests: BillingRequests) {
                                requests.purchase(
                                    ProductTypes.IN_APP,
                                    productId!!,
                                    null,
                                    checkoutUtils!!.purchaseFlow
                                )
                            }
                        })
                    }
                }
            }
    }

}