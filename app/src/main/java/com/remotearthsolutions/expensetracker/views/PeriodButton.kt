package com.remotearthsolutions.expensetracker.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.databinding.ViewPeriodbuttonBinding


class PeriodButton(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    var btnTitle: String? = null
    private val binding: ViewPeriodbuttonBinding

    init {
        binding = ViewPeriodbuttonBinding.inflate(LayoutInflater.from(context),this,true)
        val a = context?.theme?.obtainStyledAttributes(attrs, R.styleable.PeriodButton, 0, 0)

        try {
            btnTitle = a?.getString(R.styleable.PeriodButton_btnTitle)
        } finally {
            a?.recycle()
        }
        btnTitle?.let {
            binding.buttonTitleTv.text = it
        }

    }

    fun setIsSelected(isSelected: Boolean) {
        binding.selectionIndicatorView.visibility = if (isSelected) View.VISIBLE else View.INVISIBLE
    }

}