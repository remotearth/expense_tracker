package com.remotearthsolutions.expensetracker.fragments.salary

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.adapters.AccountsAdapter
import com.remotearthsolutions.expensetracker.contracts.BaseView
import com.remotearthsolutions.expensetracker.databaseutils.models.AccountModel
import com.remotearthsolutions.expensetracker.utils.*
import com.remotearthsolutions.expensetracker.viewmodels.AccountViewModel
import kotlinx.android.synthetic.main.fragment_salary.*
import kotlinx.android.synthetic.main.fragment_salary.view.*
import java.util.*


class SalaryFragment : DialogFragment() {

    private var viewModel: AccountViewModel? = null
    private lateinit var mView: View
    private lateinit var dateFormat: String
    private lateinit var sharedPreferenceUtils: SharedPreferenceUtils

    fun setViewModel(viewModel: AccountViewModel) {
        this.viewModel = viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_salary, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferenceUtils = SharedPreferenceUtils.getInstance(requireContext())!!
        dateFormat = sharedPreferenceUtils.getString(
            Constants.PREF_TIME_FORMAT,
            requireContext().resources.getString(R.string.default_time_format)
        )

        initialize()
        mView.salaryToggleBtn.setOnCheckedChangeListener { buttonView, isChecked ->
            mView.salaryContainer.visibility = if (isChecked) View.VISIBLE else View.INVISIBLE
        }

        mView.cancelBtn.setOnClickListener {
            val prevState = sharedPreferenceUtils.getBoolean(Constants.KEY_SALARY_AUTOMATIC, false)
            if (mView.salaryToggleBtn.isChecked != prevState) {
                AlertDialogUtils.show(requireContext(),
                    "",
                    requireContext().getString(R.string.sure_to_cancel_change),
                    requireContext().getString(R.string.yes),
                    requireContext().getString(R.string.no), null,
                    object : BaseView.Callback {
                        override fun onOkBtnPressed() {
                            dismiss()
                        }
                    })
            } else {
                dismiss()
            }
        }

        mView.dateTv.setOnClickListener {
            val dateTimeLong =
                DateTimeUtils.getTimeInMillisFromDateStr(mView.dateTv.text.toString(), dateFormat)
            val previousCal = Calendar.getInstance()
            previousCal.timeInMillis = dateTimeLong

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { _: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                    val calendar = Calendar.getInstance()
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.YEAR, year)
                    mView.dateTv.text = DateTimeUtils.getDate(calendar.timeInMillis, dateFormat)
                },
                previousCal.get(Calendar.YEAR),
                previousCal.get(Calendar.MONTH),
                previousCal.get(Calendar.DAY_OF_MONTH)
            )
            val currentTimeCal = Calendar.getInstance()
            currentTimeCal.add(Calendar.DAY_OF_MONTH, 1)
            datePickerDialog.datePicker.minDate = currentTimeCal.timeInMillis
            datePickerDialog.show()
        }

        mView.okBtn.setOnClickListener {
            if (mView.salaryToggleBtn.isChecked) {
                val amount = salaryAmountEdtxt.text.toString()
                if (amount.isEmpty()) {
                    Utils.showToast(requireContext(), "Enter a valid amount")
                    return@setOnClickListener
                }
                if (amount == "0") {
                    Utils.showToast(requireContext(), "Enter a valid amount")
                    return@setOnClickListener
                }
                val nextSalaryDateLong =
                    DateTimeUtils.getTimeInMillisFromDateStr(
                        mView.dateTv.text.toString(),
                        dateFormat
                    )

                sharedPreferenceUtils.putBoolean(Constants.KEY_SALARY_AUTOMATIC, true)
                sharedPreferenceUtils.putString(Constants.KEY_SALARY_AUTOMATIC_AMOUNT, amount)
                sharedPreferenceUtils.putLong(
                    Constants.KEY_SALARY_AUTOMATIC_DATE,
                    nextSalaryDateLong
                )

                val accountId = (mView.salaryAccountSpnr.selectedItem as AccountModel).id
                sharedPreferenceUtils.putInt(Constants.KEY_SALARY_AUTOMATIC_ACCOUNT_ID, accountId)
                SalaryWorkerHelper.setAutomaticSalary(requireContext(), nextSalaryDateLong)
                with(AnalyticsManager) { logEvent(AUTOMATIC_SALARY_ENABLED) }
            } else {
                sharedPreferenceUtils.putBoolean(Constants.KEY_SALARY_AUTOMATIC, false)
                SalaryWorkerHelper.cancelAutomaticSalary(requireContext())
                with(AnalyticsManager) { logEvent(AUTOMATIC_SALARY_DISABLED) }
            }
            dismiss()
        }
    }

    private fun initialize() {
        updateVisibilityOfSalaryContainer()
        updateSalaryAmount()
        updateNextSalaryDate()
        updateSalaryAccount()
    }

    private fun updateVisibilityOfSalaryContainer() {
        val isAutomaticSalaryEnabled =
            sharedPreferenceUtils.getBoolean(Constants.KEY_SALARY_AUTOMATIC, false)
        mView.salaryToggleBtn.isChecked = isAutomaticSalaryEnabled
        mView.salaryContainer.visibility =
            if (isAutomaticSalaryEnabled) View.VISIBLE else View.INVISIBLE
    }

    private fun updateSalaryAmount() {
        val amount =
            sharedPreferenceUtils.getString(Constants.KEY_SALARY_AUTOMATIC_AMOUNT, "")
        mView.salaryAmountEdtxt.setText(amount)
        mView.salaryAmountEdtxt.setSelection(amount.length)
    }

    private fun updateNextSalaryDate() {
        val cal = Calendar.getInstance()
        cal.add(Calendar.MONTH, 1)
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.set(Calendar.HOUR_OF_DAY, 1)
        cal.set(Calendar.MINUTE, 0)
        mView.dateTv.text = DateTimeUtils.getDate(
            sharedPreferenceUtils.getLong(Constants.KEY_SALARY_AUTOMATIC_DATE, cal.timeInMillis),
            sharedPreferenceUtils.getString(
                Constants.PREF_TIME_FORMAT,
                requireContext().getString(R.string.default_time_format)
            )
        )
    }

    private fun updateSalaryAccount() {
        val accountList = viewModel?.listOfAccountLiveData?.value!!
        val currencySymbol = Utils.getCurrency(requireContext())
        val adapter = AccountsAdapter(requireContext(), accountList, currencySymbol)
        mView.salaryAccountSpnr.adapter = adapter
        val accountId = sharedPreferenceUtils.getInt(
            Constants.KEY_SALARY_AUTOMATIC_ACCOUNT_ID,
            2
        )
        val accountModel = accountList.find { accountModel -> accountModel.id == accountId }
        val index = accountList.indexOf(accountModel);
        mView.salaryAccountSpnr.setSelection(if (index < 0) 1 else index)
    }
}
