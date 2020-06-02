package com.remotearthsolutions.expensetracker.fragments.addexpensescreen

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.work.Data
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.activities.main.MainActivity
import com.remotearthsolutions.expensetracker.contracts.BaseView
import com.remotearthsolutions.expensetracker.contracts.ExpenseFragmentContract
import com.remotearthsolutions.expensetracker.databaseutils.DatabaseClient
import com.remotearthsolutions.expensetracker.databaseutils.models.*
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.CategoryExpense
import com.remotearthsolutions.expensetracker.fragments.*
import com.remotearthsolutions.expensetracker.utils.*
import com.remotearthsolutions.expensetracker.utils.DateTimeUtils.currentTime
import com.remotearthsolutions.expensetracker.utils.DateTimeUtils.getCalendarFromDateString
import com.remotearthsolutions.expensetracker.utils.DateTimeUtils.getCurrentDate
import com.remotearthsolutions.expensetracker.utils.DateTimeUtils.getTimeInMillisFromDateStr
import com.remotearthsolutions.expensetracker.utils.Utils.getCurrency
import com.remotearthsolutions.expensetracker.utils.workmanager.WorkManagerEnqueuer
import com.remotearthsolutions.expensetracker.utils.workmanager.WorkRequestType
import com.remotearthsolutions.expensetracker.viewmodels.ExpenseFragmentViewModel
import com.remotearthsolutions.expensetracker.viewmodels.viewmodel_factory.BaseViewModelFactory
import kotlinx.android.synthetic.main.fragment_add_expense.view.*
import org.parceler.Parcels
import java.util.*
import kotlin.math.abs

class ExpenseFragment : BaseFragment(), ExpenseFragmentContract.View {

    var purpose: Purpose? = null
    private lateinit var mView: View
    private var viewModel: ExpenseFragmentViewModel? = null
    private var categoryExpense: CategoryExpense? = null
    private var prevExpense: CategoryExpense? = null
    private lateinit var mContext: Context
    private lateinit var mResources: Resources
    private lateinit var format: String
    private var isRepeatEnabled = false
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
        format = SharedPreferenceUtils.getInstance(requireActivity())!!.getString(
            Constants.PREF_TIME_FORMAT,
            Constants.KEY_DATE_MONTH_YEAR_DEFAULT
        )

        mView = inflater.inflate(R.layout.fragment_add_expense, container, false)
        val repeatTypes = getResourceStringArray(R.array.repeatType)
        mView.repeatTypeSpnr.adapter =
            ArrayAdapter(requireContext(), R.layout.repeat_type_spinner_item, repeatTypes)

        val currencySymbol = getCurrency(mContext)
        mView.inputdigit.hint = "$currencySymbol 0"
        val numpadFragment =
            childFragmentManager.findFragmentById(R.id.numpadContainer) as NumpadFragment?
        val numpadManager = NumpadManager()
        numpadManager.attachDisplay(mView.inputdigit)
        numpadManager.attachDeleteButton(mView.deleteBtn)
        numpadFragment!!.setListener(numpadManager)
        val db = DatabaseClient.getInstance(mContext).appDatabase

        viewModel =
            ViewModelProviders.of(this, BaseViewModelFactory {
                ExpenseFragmentViewModel(
                    this,
                    db.expenseDao(),
                    db.accountDao(),
                    db.categoryDao(),
                    db.scheduleExpenseDao(),
                    db.workerIdDao()
                )
            }).get(ExpenseFragmentViewModel::class.java)
        viewModel!!.init()

        val args = arguments
        if (args != null) {
            categoryExpense =
                Parcels.unwrap<CategoryExpense>(args.getParcelable(Constants.CATEGORYEXPENSE_PARCEL))
            if (categoryExpense != null) {
                prevExpense = categoryExpense!!.copy()
                Helpers.updateUI(mView, categoryExpense, format)
            } else {
                viewModel!!.setDefaultCategory()
            }

            if (categoryExpense != null && categoryExpense!!.accountIcon != null) {
                Helpers.updateAccountBtn(mView, categoryExpense)
            } else {
                val accountId = SharedPreferenceUtils.getInstance(mContext)!!.getInt(
                    Constants.KEY_SELECTED_ACCOUNT_ID,
                    1
                )
                viewModel!!.setDefaultSourceAccount(accountId)
            }
        }

        if (purpose?.equals(Purpose.UPDATE)!!) {
            mView.enableRepeatBtn.visibility = View.GONE
        }

        registerBackButton()
        return mView
    }

    @SuppressLint("InflateParams")
    override fun defineClickListener() {
        mView.fromAccountBtn.setOnClickListener {
            val fm = childFragmentManager
            val accountDialogFragment: AccountDialogFragment =
                AccountDialogFragment.newInstance(getResourceString(R.string.select_account))
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
                CategoryDialogFragment.newInstance(getResourceString(R.string.select_category))
            categoryDialogFragment.setCategory(categoryExpense?.categoryId!!)
            categoryDialogFragment.setCallback(object :
                CategoryDialogFragment.Callback {
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
        mView.singleEntryView.setOnClickListener {
            val datePickerDialogFragment: DatePickerDialogFragment =
                DatePickerDialogFragment.newInstance("")
            val cal = getCalendarFromDateString(format, mView.dateTv.text.toString())
            datePickerDialogFragment.setInitialDate(
                cal[Calendar.DAY_OF_MONTH],
                cal[Calendar.MONTH],
                cal[Calendar.YEAR]
            )
            datePickerDialogFragment.setCallback(object :
                DatePickerDialogFragment.Callback {
                override fun onSelectDate(date: String?) {
                    mView.dateTv.text = date
                    datePickerDialogFragment.dismiss()
                }
            })
            datePickerDialogFragment.show(
                childFragmentManager,
                DatePickerDialogFragment::class.java.name
            )
        }
        mView.okBtn.setOnClickListener {
            var expenseStr = mView.inputdigit.text.toString()
            if (expenseStr == getResourceString(R.string.point)) {
                expenseStr = ""
            }
            val amount = try {
                if (expenseStr.isNotEmpty()) expenseStr.toDouble() else 0.0
            } catch (e: NumberFormatException) {
                Toast.makeText(
                    mContext,
                    getResourceString(R.string.make_sure_enter_valid_number),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (abs(amount) == 0.0) {
                showToast(getResourceString(R.string.please_enter_an_amount))
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

            if (mView.repeatEntryView.visibility != View.VISIBLE) {
                viewModel!!.addExpense(expenseModel)
            } else {
                val period = mView.numberTv.text.toString().toInt()
                val repeatType = mView.repeatTypeSpnr.selectedItemPosition
                val repeatCount = mView.timesTv.text.toString().toInt()
                val nextDate = ExpenseScheduler.nextOcurrenceDate(
                    Calendar.getInstance().timeInMillis, period, repeatType
                )
                viewModel!!.scheduleExpense(expenseModel, period, repeatType, repeatCount, nextDate)
                with(AnalyticsManager) { logEvent(EXPENSE_TYPE_SCHEDULED) }
            }
        }

        mView.expenseNoteEdtxt.setOnClickListener {
            DialogHelper.showExpenseNoteInput(mContext, layoutInflater, mView, categoryExpense)
        }
        mView.expenseDeleteBtn.setOnClickListener {
            if (categoryExpense!!.expenseId > 0) {
                showAlert(getResourceString(R.string.attention),
                    getResourceString(R.string.are_you_sure_you_want_to_delete_this_expense_entry),
                    getResourceString(R.string.yes),
                    getResourceString(R.string.not_now),
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
        mView.enableRepeatBtn.setOnClickListener {
            isRepeatEnabled = !isRepeatEnabled
            if (isRepeatEnabled) {
                mView.enableRepeatBtn.setImageResource(R.drawable.ic_repeat)
                mView.singleEntryView.visibility = View.GONE
                mView.repeatEntryView.visibility = View.VISIBLE
                (mContext as MainActivity?)?.supportActionBar?.title =
                    getResourceString(R.string.schedule_expense)
            } else {
                mView.enableRepeatBtn.setImageResource(R.drawable.ic_single)
                mView.singleEntryView.visibility = View.VISIBLE
                mView.repeatEntryView.visibility = View.GONE
                (mContext as MainActivity?)?.supportActionBar?.title =
                    getResourceString(R.string.add_expense)
            }
        }
        mView.numberTv.setOnClickListener {
            DialogHelper.getInputFor(mView.numberTv, mContext, layoutInflater)
        }
        mView.timesTv.setOnClickListener {
            DialogHelper.getInputFor(mView.timesTv, mContext, layoutInflater)
        }
    }

    override fun onExpenseAdded(amount: Double) {
        mView.inputdigit.setText("")
        Toast.makeText(activity, getResourceString(R.string.successfully_added), Toast.LENGTH_SHORT)
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
        if (MainActivity.addedExpenseCount % 2 == 0) {
            val delay = Random().nextInt(3000 - 1000) + 1000
            Handler().postDelayed({
                AdmobUtils.getInstance((mContext as Activity)).showInterstitialAds()
            }, delay.toLong())
        } else {
            Helpers.requestToReviewApp(mainActivity, viewModel!!)
        }
        MainActivity.addedExpenseCount++
        with(AnalyticsManager) { logEvent(EXPENSE_TYPE_DEFAULT) }
    }

    override fun onExpenseDeleted(categoryExpense: CategoryExpense?) {
        Toast.makeText(
            activity,
            getResourceString(R.string.successfully_deleted_expense_entry),
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

    override fun onScheduleExpense(scheduledExpenseModel: ScheduledExpenseModel) {
        mView.inputdigit.setText("")
        showToast(getResourceString(R.string.expense_scheduled))

        val delay = scheduledExpenseModel.nextoccurrencedate - Calendar.getInstance().timeInMillis
        val data = Data.Builder()
        data.putLong(ExpenseScheduler.SCHEDULED_EXPENSE_ID, scheduledExpenseModel.id)
        val workRequestId = WorkManagerEnqueuer()
            .enqueue<AddScheduledExpenseWorker>(
                requireContext(),
                WorkRequestType.ONETIME,
                delay,
                data.build()
            )
        viewModel?.saveWorkerId(WorkerIdModel(scheduledExpenseModel.id, workRequestId))
    }
}
