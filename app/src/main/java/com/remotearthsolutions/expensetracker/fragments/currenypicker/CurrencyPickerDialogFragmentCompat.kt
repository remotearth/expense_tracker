package com.remotearthsolutions.expensetracker.fragments.currenypicker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.preference.PreferenceDialogFragmentCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.utils.Constants
import com.remotearthsolutions.expensetracker.utils.SharedPreferenceUtils
import com.remotearthsolutions.expensetracker.utils.Utils


class CurrencyPickerDialogFragmentCompat : PreferenceDialogFragmentCompat() {

    override fun onBindDialogView(view: View) {
        super.onBindDialogView(view)

        val recyclerView = view?.findViewById<RecyclerView>(R.id.currencyList)!!
        val deviceHeight = Utils.getDeviceScreenSize(context)!!.height
        val height = deviceHeight - deviceHeight / 3
        val sharedPreferenceUtils = SharedPreferenceUtils.getInstance(requireContext())!!
        val currencyName = sharedPreferenceUtils.getString(
            Constants.PREF_CURRENCY, requireContext().getString(R.string.default_currency)
        )
        val currencies = context?.resources?.getStringArray(R.array.currency)
        val selectedPosition = currencies?.indexOf(currencyName)

        recyclerView.layoutParams.height = height
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val itemDecoration = DividerItemDecoration(context, RecyclerView.VERTICAL)
        recyclerView.addItemDecoration(itemDecoration)
        val currencyListAdapter = CurrencyListAdapter(requireContext(), currencyName) {
            sharedPreferenceUtils.putString(Constants.PREF_CURRENCY, it)
            dismiss()
        }
        recyclerView.adapter = currencyListAdapter
        recyclerView.scrollToPosition(selectedPosition!!)

        val searchEditText = view?.findViewById<EditText>(R.id.searchEdtxt)!!
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                currencyListAdapter.filter.filter(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

    }

    override fun onDialogClosed(positiveResult: Boolean) {
    }

    companion object {
        fun newInstance(key: String): CurrencyPickerDialogFragmentCompat {
            val fragment = CurrencyPickerDialogFragmentCompat()
            val bundle = Bundle()
            bundle.putString(ARG_KEY, key)
            fragment.arguments = bundle
            return fragment
        }
    }
}