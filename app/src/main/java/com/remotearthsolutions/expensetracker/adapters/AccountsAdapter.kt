package com.remotearthsolutions.expensetracker.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.databaseutils.models.AccountModel
import com.remotearthsolutions.expensetracker.databinding.CustomAccountBinding
import com.remotearthsolutions.expensetracker.utils.CategoryIcons.getIconId
import com.remotearthsolutions.expensetracker.utils.Utils.formatDecimalValues

class AccountsAdapter(
    mContext: Context,
    private val accountList: List<AccountModel>,
    private val currencySymbol: String
) : ArrayAdapter<AccountModel?>(mContext, R.layout.custom_account, accountList) {

    private lateinit var binding: CustomAccountBinding

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView

        if (view == null) {
            binding = CustomAccountBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            view = binding.root
        }

        val model = accountList[position]
        if (model.icon != null) {
            binding.acimage.setImageResource(getIconId(model.icon!!))
        }
        binding.actitle.text = model.name
        binding.acammount.text = "$currencySymbol ${formatDecimalValues(model.amount)}"
        if (model.amount < 0) {
            binding.acammount.setTextColor(
                ContextCompat.getColor(
                    context, android.R.color.holo_red_dark
                )
            )
        } else {
            binding.acammount.setTextColor(
                ContextCompat.getColor(
                    context, android.R.color.holo_green_light
                )
            )
        }
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }
}