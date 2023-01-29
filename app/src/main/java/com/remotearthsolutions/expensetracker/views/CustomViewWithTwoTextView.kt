package com.remotearthsolutions.expensetracker.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.databinding.ViewCustomviewwithtwotextviewBinding


class CustomViewWithTwoTextView(context: Context?, attrs: AttributeSet?) :
    LinearLayout(context, attrs) {

    private var binding: ViewCustomviewwithtwotextviewBinding

    init {
        binding = ViewCustomviewwithtwotextviewBinding.inflate(LayoutInflater.from(context),this,true)
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
            titleColor?.let { binding.titleTv.setTextColor(it) }
            infoColor?.let { binding.infoTv.setTextColor(it) }

            title?.let { binding.titleTv.text = it }
            info?.let { binding.infoTv.text = it }


        } finally {
            a?.recycle()
        }
    }

    fun setTitle(text: String) {
        binding.titleTv.text = text
    }

    fun setInfo(text: String) {
        binding.infoTv.text = text
    }
}