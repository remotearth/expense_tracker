package com.remotearthsolutions.expensetracker.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.remotearthsolutions.expensetracker.R
import kotlinx.android.synthetic.main.view_customviewwithtwotextview.view.*


class CustomViewWithTwoTextView(context: Context?, attrs: AttributeSet?) :
    LinearLayout(context, attrs) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_customviewwithtwotextview, this, true)
        val a = context?.theme?.obtainStyledAttributes(
            attrs,
            R.styleable.CustomViewWithTwoTextView,
            0,
            0
        )

        try {
            val defaultColor = ContextCompat.getColor(context!!, R.color.colorPrimaryDark)
            val title = a?.getString(R.styleable.CustomViewWithTwoTextView_title)
            val titleColor =
                a?.getColor(R.styleable.CustomViewWithTwoTextView_titleColor, defaultColor)
            val info = a?.getString(R.styleable.CustomViewWithTwoTextView_info)
            val infoColor =
                a?.getColor(R.styleable.CustomViewWithTwoTextView_infoColor, defaultColor)
            titleColor?.let { titleTv.setTextColor(it) }
            infoColor?.let { infoTv.setTextColor(it) }

            title?.let { titleTv.text = it }
            info?.let { infoTv.text = it }


        } finally {
            a?.recycle()
        }
    }

    fun setTitle(text: String) {
        titleTv.text = text
    }

    fun setInfo(text: String) {
        infoTv.text = text
    }
}