package com.remotearthsolutions.expensetracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.remotearthsolutions.expensetracker.R
import kotlinx.android.synthetic.main.fragment_optionbottomsheet.view.*

class OptionBottomSheetFragment : BottomSheetDialogFragment(),
    View.OnClickListener {
    private var callback: Callback? = null
    private var optionsFor: OptionsFor? = null
    fun setCallback(
        callback: Callback?,
        optionsFor: OptionsFor?
    ) {
        this.callback = callback
        this.optionsFor = optionsFor
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =
            inflater.inflate(R.layout.fragment_optionbottomsheet, container, false)
        view.addAmountBtn.setOnClickListener(this)
        view.editBtn.setOnClickListener(this)
        view.deleteBtn.setOnClickListener(this)
        when (optionsFor) {
            OptionsFor.CATEGORY -> {
                view.addAmountBtn.visibility = View.GONE
                view.underLine.visibility = View.GONE
            }
            OptionsFor.ACCOUNT -> {
                view.addAmountBtn.visibility = View.VISIBLE
                view.underLine.visibility = View.VISIBLE
            }
        }
        return view
    }

    override fun onClick(v: View) {
        if (callback == null) {
            return
        }
        when (v.id) {
            R.id.addAmountBtn -> callback!!.onClickAddAmountBtn()
            R.id.editBtn -> callback!!.onClickEditBtn()
            R.id.deleteBtn -> callback!!.onClickDeleteBtn()
        }
        dismiss()
    }

    interface Callback {
        fun onClickAddAmountBtn()
        fun onClickEditBtn()
        fun onClickDeleteBtn()
    }

    enum class OptionsFor {
        CATEGORY, ACCOUNT
    }
}