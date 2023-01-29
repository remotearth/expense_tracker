package com.remotearthsolutions.expensetracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.databinding.FragmentOptionbottomsheetBinding

class OptionBottomSheetFragment : BottomSheetDialogFragment(),
    View.OnClickListener {
    private lateinit var binding: FragmentOptionbottomsheetBinding
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
    ): View {
        binding = FragmentOptionbottomsheetBinding.inflate(layoutInflater, container, false)
        binding.addAmountBtn.setOnClickListener(this)
        binding.updateAmountBtn.setOnClickListener(this)
        binding.editBtn.setOnClickListener(this)
        binding.deleteBtn.setOnClickListener(this)
        when (optionsFor) {
            OptionsFor.CATEGORY -> {
                binding.addAmountBtn.visibility = View.GONE
                binding.underLine.visibility = View.GONE
                binding.updateAmountBtn.visibility = View.GONE
                binding.underLine1.visibility = View.GONE
            }
            OptionsFor.ACCOUNT -> {
                binding.addAmountBtn.visibility = View.VISIBLE
                binding.underLine.visibility = View.VISIBLE
                binding.updateAmountBtn.visibility = View.VISIBLE
                binding.underLine1.visibility = View.VISIBLE
            }
            null -> TODO()
        }
        return binding.root
    }

    override fun onClick(v: View) {
        if (callback == null) {
            return
        }
        when (v.id) {
            R.id.addAmountBtn -> callback!!.onClickAddAmountBtn()
            R.id.updateAmountBtn -> callback!!.onClickUpdateAmountBtn()
            R.id.editBtn -> callback!!.onClickEditBtn()
            R.id.deleteBtn -> callback!!.onClickDeleteBtn()
        }
        dismiss()
    }

    interface Callback {
        fun onClickAddAmountBtn() {}
        fun onClickUpdateAmountBtn() {}
        fun onClickEditBtn()
        fun onClickDeleteBtn()
    }

    enum class OptionsFor {
        CATEGORY, ACCOUNT
    }
}