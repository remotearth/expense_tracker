package com.remotearthsolutions.expensetracker.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.databinding.CustomExpenseSourceTargetBtnBinding
import com.remotearthsolutions.expensetracker.utils.CategoryIcons


class SourceTargetButton(context: Context?, attrs: AttributeSet?) : RelativeLayout(context, attrs) {
    private var btnHeader: String? = null
    private var btnTitle: String? = null
    private var btnIcon: String? = null
    private var binding: CustomExpenseSourceTargetBtnBinding

    init {
        binding = CustomExpenseSourceTargetBtnBinding.inflate(LayoutInflater.from(context), this)
        val a = context?.theme?.obtainStyledAttributes(attrs, R.styleable.SourceTargetButton, 0, 0)

        try {
            btnHeader = a?.getString(R.styleable.SourceTargetButton_btnHeader)
            btnTitle = a?.getString(R.styleable.SourceTargetButton_btnName)
            btnIcon = a?.getString(R.styleable.SourceTargetButton_btnIcon)
        } finally {
            a?.recycle()
        }

        update(btnHeader!!, btnTitle!!, btnIcon!!)
    }

    fun update(btnHeader: String, btnTitle: String, btnIcon: String) {
        binding.accountTitleTv.text = btnHeader
        binding.accountNameTv.text = btnTitle
        binding.accountImageIv.setImageResource(CategoryIcons.getIconId(btnIcon))

    }

    fun update(btnTitle: String, btnIcon: String) {
        binding.accountNameTv.text = btnTitle
        binding.accountImageIv.setImageResource(CategoryIcons.getIconId(btnIcon))
    }
}