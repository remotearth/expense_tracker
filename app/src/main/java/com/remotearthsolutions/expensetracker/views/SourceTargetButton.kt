package com.remotearthsolutions.expensetracker.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.utils.CategoryIcons
import kotlinx.android.synthetic.main.custom_expense_source_target_btn.view.*


class SourceTargetButton(context: Context?, attrs: AttributeSet?) : RelativeLayout(context, attrs) {
    private var btnHeader: String? = null
    private var btnTitle: String? = null
    private var btnIcon: String? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.custom_expense_source_target_btn, this, true)
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
        accountTitleTv.text = btnHeader
        accountNameTv.text = btnTitle
        accountImageIv.setImageResource(CategoryIcons.getIconId(btnIcon))

    }

    fun update(btnTitle: String, btnIcon: String) {
        accountNameTv.text = btnTitle
        accountImageIv.setImageResource(CategoryIcons.getIconId(btnIcon))
    }
}