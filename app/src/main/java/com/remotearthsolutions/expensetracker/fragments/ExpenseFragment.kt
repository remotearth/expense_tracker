package com.remotearthsolutions.expensetracker.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.activities.MainActivity
import com.remotearthsolutions.expensetracker.contracts.BaseView
import com.remotearthsolutions.expensetracker.contracts.ExpenseFragmentContract
import com.remotearthsolutions.expensetracker.databaseutils.DatabaseClient
import com.remotearthsolutions.expensetracker.databaseutils.models.AccountModel
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryExpense
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel
import com.remotearthsolutions.expensetracker.databaseutils.models.ExpenseModel
import com.remotearthsolutions.expensetracker.utils.*
import com.remotearthsolutions.expensetracker.utils.DateTimeUtils.currentTime
import com.remotearthsolutions.expensetracker.utils.DateTimeUtils.getCalendarFromDateString
import com.remotearthsolutions.expensetracker.utils.DateTimeUtils.getCurrentDate
import com.remotearthsolutions.expensetracker.utils.DateTimeUtils.getDate
import com.remotearthsolutions.expensetracker.utils.DateTimeUtils.getTimeInMillisFromDateStr
import com.remotearthsolutions.expensetracker.utils.Utils.getCurrency
import com.remotearthsolutions.expensetracker.viewmodels.ExpenseFragmentViewModel
import com.remotearthsolutions.expensetracker.viewmodels.viewmodel_factory.BaseViewModelFactory
import kotlinx.android.synthetic.main.fragment_add_expense.view.*
import org.parceler.Parcels
import java.util.*

class ExpenseFragment : BaseFragment(), ExpenseFragmentContract.View {

    var purpose: Purpose? = null
    private lateinit var mView: View
    private var viewModel: ExpenseFragmentViewModel? = null
    private var categoryExpense: CategoryExpense? = null
    private var prevExpense: CategoryExpense? = null
    private lateinit var mContext: Context
    private lateinit var mResources: Resources
    private lateinit var format: String
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        mResources = context.resources
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        format = SharedPreferenceUtils.getInstance(activity!!)!!.getString(
            Constants.PREF_TIME_FORMAT,
            Constants.KEY_DATE_MONTH_YEAR_DEFAULT
        )

        mView = inflater.inflate(R.layout.fragment_add_expense, container, false)

        val currencySymbol = getCurrency(mContext)
        mView.inputdigit.hint = currencySymbol + getString(R.string.initially_zero)
        val numpadFragment =
            childFragmentManager.findFragmentById(R.id.numpadContainer) as NumpadFragment?
        val numpadManager = NumpadManager()
        numpadManager.attachDisplay(mView.inputdigit)
        numpadManager.attachDeleteButton(mView.deleteBtn)
        numpadFragment!!.setListener(numpadManager)
        val db = DatabaseClient.getInstance(mContext)?.appDatabase

        viewModel =
            ViewModelProviders.of(this, BaseViewModelFactory {
                ExpenseFragmentViewModel(
                    mContext,
                    this,
                    db?.expenseDao()!!,
                    db.accountDao(),
                    db.categoryDao()
                )
            }).get(ExpenseFragmentViewModel::class.java)
        viewModel!!.init()

        val args = arguments
        if (args != null) {
            categoryExpense =
                Parcels.unwrap<CategoryExpense>(args.getParcelable(Constants.CATEGORYEXPENSE_PARCEL))
            if (categoryExpense != null) {
                prevExpense = categoryExpense!!.copy()
                mView.toCategoryBtn.update(
                    categoryExpense?.categoryName!!,
                    categoryExpense?.categoryIcon!!
                )

                if (categoryExpense!!.totalAmount > 0) {
                    mView.inputdigit.setText(categoryExpense!!.totalAmount.toString())
                }
                mView.expenseNoteEdtxt.setText(categoryExpense!!.note)
                if (categoryExpense!!.datetime > 0) {
                    mView.dateTv.text = getDate(
                        categoryExpense!!.datetime,
                        format
                    )
                }
            } else {
                viewModel!!.setDefaultCategory()
            }
            if (categoryExpense != null && categoryExpense!!.accountIcon != null) {
                mView.fromAccountBtn.update(
                    categoryExpense?.accountName!!,
                    categoryExpense?.accountIcon!!
                )
            } else {
                val accountId = SharedPreferenceUtils.getInstance(mContext)!!.getInt(
                    Constants.KEY_SELECTED_ACCOUNT_ID,
                    1
                )
                viewModel!!.setDefaultSourceAccount(accountId)
            }
        }

        registerBackButton()
        return mView
    }

    @SuppressLint("InflateParams")
    override fun defineClickListener() {
        mView.fromAccountBtn.setOnClickListener {
            val fm = childFragmentManager
            val accountDialogFragment: AccountDialogFragment =
                AccountDialogFragment.newInstance(getString(R.string.select_account))
            accountDialogFragment.setCallback(object : AccountDialogFragment.Callback {
                override fun onSelectAccount(accountIncome: AccountModel) {
                    categoryExpense!!.setAccount(accountIncome)
                    mView.fromAccountBtn.update(
                        accountIncome.name!!,
                        accountIncome.icon!!
                    )
                    accountDialogFragment.dismiss()
                    SharedPreferenceUtils.getInstance(mContext)!!.putInt(
                        Constants.KEY_SELECTED_ACCOUNT_ID,
                        accountIncome.id
                    )
                }
            })
            accountDialogFragment.show(fm, AccountDialogFragment::class.java.name)
        }
        mView.toCategoryBtn.setOnClickListener {
            val fm = childFragmentManager
            val categoryDialogFragment: CategoryDialogFragment =
                CategoryDialogFragment.newInstance(getString(R.string.select_category))
            categoryDialogFragment.setCategory(categoryExpense?.categoryId!!)
            categoryDialogFragment.setCallback(object : CategoryDialogFragment.Callback {
                override fun onSelectCategory(category: CategoryModel?) {
                    mView.toCategoryBtn.update(
                        category?.name!!,
                        category.icon!!
                    )
                    categoryDialogFragment.dismiss()
                    categoryExpense!!.setCategory(category)
                }
            })
            categoryDialogFragment.show(fm, CategoryDialogFragment::class.java.name)
        }
        mView.dateTv.text = getCurrentDate(format)
        mView.selectdate.setOnClickListener {
            val fm = childFragmentManager
            val datePickerDialogFragment: DatePickerDialogFragment =
                DatePickerDialogFragment.newInstance("")
            val cal = getCalendarFromDateString(
                format,
                mView.dateTv.text.toString()
            )
            datePickerDialogFragment.setInitialDate(
                cal[Calendar.DAY_OF_MONTH],
                cal[Calendar.MONTH],
                cal[Calendar.YEAR]
            )
            datePickerDialogFragment.setCallback(object : DatePickerDialogFragment.Callback {
                override fun onSelectDate(date: String?) {
                    mView.dateTv.text = date
                    datePickerDialogFragment.dismiss()
                }

            })

            datePickerDialogFragment.show(fm, DatePickerDialogFragment::class.java.name)
        }
        mView.okBtn.setOnClickListener {
            var expenseStr = mView.inputdigit.text.toString()
            if (expenseStr == getString(R.string.point)) {
                expenseStr = ""
            }
            val amount: Double
            amount = try {
                if (expenseStr.isNotEmpty()) expenseStr.toDouble() else 0.0
            } catch (e: NumberFormatException) {
                Toast.makeText(
                    mContext,
                    resources.getString(R.string.make_sure_enter_valid_number),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            val expenseModel = ExpenseModel()
            expenseModel.id = categoryExpense?.expenseId!!
            expenseModel.amount = amount
            expenseModel.datetime = getTimeInMillisFromDateStr(
                mView.dateTv.text.toString()
                        + " " + currentTime, format + " " + Constants.KEY_HOUR_MIN_SEC
            )
            expenseModel.categoryId = categoryExpense!!.categoryId
            expenseModel.source = categoryExpense!!.accountId
            expenseModel.note = mView.expenseNoteEdtxt.text.toString()
            viewModel!!.addExpense(expenseModel)
        }

        mView.expenseNoteEdtxt.setOnClickListener {
            val builder =
                AlertDialog.Builder(mContext).create()
            val dialogView =
                layoutInflater.inflate(R.layout.view_add_note, null)
            val noteEdtxt = dialogView.findViewById<EditText>(R.id.noteEdtxt)
            val note = categoryExpense!!.note
            if (note != null) {
                noteEdtxt.setText(categoryExpense!!.note)
                noteEdtxt.setSelection(categoryExpense!!.note!!.length)
            }
            dialogView.findViewById<View>(R.id.okBtn)
                .setOnClickListener {
                    val str = noteEdtxt.text.toString()
                    categoryExpense!!.note = str
                    mView.expenseNoteEdtxt.setText(str)
                    builder.dismiss()
                }
            builder.setView(dialogView)
            builder.show()
        }
        mView.expenseDeleteBtn.setOnClickListener {
            if (categoryExpense!!.expenseId > 0) {
                showAlert(getString(R.string.attention),
                    getString(R.string.are_you_sure_you_want_to_delete_this_expense_entry),
                    getString(R.string.yes),
                    getString(R.string.not_now),
                    object : BaseView.Callback {
                        override fun onOkBtnPressed() {
                            viewModel!!.deleteExpense(categoryExpense)
                        }

                        override fun onCancelBtnPressed() {}
                    })
            } else {
                (mContext as Activity?)!!.onBackPressed()
            }
        }
    }

    override fun onExpenseAdded(amount: Double) {
        mView.inputdigit.setText("")
        Toast.makeText(activity, getString(R.string.successfully_added), Toast.LENGTH_SHORT)
            .show()
        var mutableAmount = amount

        purpose?.let {
            if (it == Purpose.UPDATE) {
                if (categoryExpense?.accountId == prevExpense!!.accountId) {
                    mutableAmount += (prevExpense!!.totalAmount * -1)
                } else {
                    viewModel!!.updateAccountAmount(
                        prevExpense!!.accountId,
                        prevExpense!!.totalAmount * -1
                    )
                }
            }
        }
        viewModel!!.updateAccountAmount(categoryExpense?.accountId!!, mutableAmount)


        val mainActivity = mContext as MainActivity?
        mainActivity!!.updateSummary()
        mainActivity.refreshChart()
        if (MainActivity.expenseAddededCount % 3 == 0) {
            val delay = Random().nextInt(3000 - 1000) + 1000
            Handler().postDelayed({
                AdmobUtils.getInstance((mContext as Activity))?.showInterstitialAds()
            }, delay.toLong())
        } else {
            if (!SharedPreferenceUtils.getInstance(mainActivity)?.getBoolean(
                    Constants.ASKED_TO_REVIEW,
                    false
                )!!
            ) {
                viewModel!!.requestToReviewApp {
                    mainActivity.onBackPressed()
                    SharedPreferenceUtils.getInstance(mainActivity)
                        ?.putBoolean(Constants.ASKED_TO_REVIEW, true)
                    RequestReviewUtils.request(mainActivity)
                }
            }

        }
        MainActivity.expenseAddededCount++
        FirebaseEventLogUtils.logCustom(mainActivity, "Expense_Added")
    }

    override fun onExpenseDeleted(categoryExpense: CategoryExpense?) {
        Toast.makeText(
            activity,
            getString(R.string.successfully_deleted_expense_entry),
            Toast.LENGTH_SHORT
        ).show()
        (mContext as Activity?)!!.onBackPressed()
        viewModel!!.updateAccountAmount(
            this.categoryExpense?.accountId!!,
            categoryExpense?.totalAmount!! * -1
        )
        val mainActivity = mContext as MainActivity?
        mainActivity!!.updateSummary()
    }

    override fun setSourceAccount(account: AccountModel?) {
        if (categoryExpense == null) {
            categoryExpense =
                CategoryExpense()
        }
        categoryExpense!!.setAccount(account!!)
        mView.fromAccountBtn.update(
            account.name!!,
            account.icon!!
        )
        SharedPreferenceUtils.getInstance(mContext)!!.putInt(
            Constants.KEY_SELECTED_ACCOUNT_ID,
            account.id
        )
    }

    override fun showDefaultCategory(categoryModel: CategoryModel?) {
        if (categoryExpense == null) {
            categoryExpense =
                CategoryExpense()
        }
        categoryExpense!!.setCategory(categoryModel!!)
        mView.toCategoryBtn.update(
            categoryModel.name!!,
            categoryModel.icon!!
        )
    }

    enum class Purpose {
        ADD, UPDATE
    }

}