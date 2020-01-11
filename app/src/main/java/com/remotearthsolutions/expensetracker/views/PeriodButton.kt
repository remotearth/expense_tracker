package com.remotearthsolutions.expensetracker.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.remotearthsolutions.expensetracker.R
import kotlinx.android.synthetic.main.view_periodbutton.view.*


class PeriodButton(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    var btnTitle: String? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_periodbutton, this, true)
        val a = context?.theme?.obtainStyledAttributes(attrs, R.styleable.PeriodButton, 0, 0)

        try {
            btnTitle = a?.getString(R.styleable.PeriodButton_btnTitle)
        } finally {
            a?.recycle()
        }
        btnTitle?.let {
            buttonTitleTv.text = it
        }

    }

    fun setIsSelected(isSelected: Boolean) {
        selectionIndicatorView?.visibility = if (isSelected) View.VISIBLE else View.INVISIBLE
    }

}