package com.remotearthsolutions.expensetracker.fragments.currenypicker

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import androidx.core.content.res.TypedArrayUtils
import androidx.preference.DialogPreference
import com.remotearthsolutions.expensetracker.R


class CurrencyPickerPreference(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int,
    defStyleRes: Int
) :
    DialogPreference(context, attrs, defStyleAttr, defStyleRes) {

    init {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : this(
        context,
        attrs,
        defStyleAttr,
        android.R.attr.dialogPreferenceStyle
    ) {
        init(context)
    }

    @SuppressLint("RestrictedApi")
    constructor(context: Context, attrs: AttributeSet?) : this(
        context, attrs!!,
        TypedArrayUtils.getAttr(
            context, R.attr.dialogPreferenceStyle,
            android.R.attr.dialogPreferenceStyle
        )
    ) {
        init(context)
    }

    constructor(context: Context) : this(context, null) {
        init(context)
    }

    private fun init(context: Context) {
        positiveButtonText = null
        negativeButtonText = context.getString(R.string.cancel)

    }

    override fun getDialogLayoutResource(): Int {
        return R.layout.listpref_dialog
    }


}