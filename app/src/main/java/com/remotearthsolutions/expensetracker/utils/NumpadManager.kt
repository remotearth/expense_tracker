package com.remotearthsolutions.expensetracker.utils

import android.view.View
import android.widget.EditText
import com.remotearthsolutions.expensetracker.fragments.NumpadFragment
import com.remotearthsolutions.expensetracker.utils.Utils.formatDecimalValues

class NumpadManager : NumpadFragment.Listener {
    private var displayEdtxt: EditText? = null
    private var isOperationDone = false
    private var result = 0.0
    private var lastOperation: String? = null
    private var lastValue = "0"
    fun attachDisplay(inputDigitEdtxt: EditText?) {
        displayEdtxt = inputDigitEdtxt
    }

    fun attachDeleteButton(deleteButton: View) {
        deleteButton.setOnClickListener {
            val len = displayEdtxt!!.text.toString().length
            if (len > 0 && !isOperationDone) {
                displayEdtxt!!.setText(displayEdtxt!!.text.toString().substring(0, len - 1))
            }
        }
    }

    override fun onNumpadButtonClick(value: String?) {
        if (isOperationDone) {
            displayEdtxt!!.setText("")
            isOperationDone = false
        }
        var str = displayEdtxt!!.text.toString()
        if (value == "." && str.contains(".")) {
            displayEdtxt!!.setText(str)
        } else {
            str += value
            displayEdtxt!!.setText(str)
        }
    }

    @Throws(NumberFormatException::class)
    override fun onMathOperationButtonClick(operation: String?) {
        if (isOperationDone && lastOperation != null) {
            lastOperation = operation
            return
        }
        val `val` = displayEdtxt!!.text.toString()
        if (`val`.isEmpty() || `val` == ".") {
            return
        }
        val currentVal: Double
        currentVal = try {
            `val`.toDouble()
        } catch (e: NumberFormatException) {
            throw e
        }
        val lastVal = lastValue.toDouble()
        if (lastOperation == null) {
            result = currentVal
        } else {
            when (lastOperation) {
                "+" -> {
                    result = lastVal + currentVal
                }
                "-" -> {
                    result = lastVal - currentVal
                }
                "*" -> {
                    result = lastVal * currentVal
                }
                "/" -> {
                    result = lastVal / currentVal
                }
            }
        }
        if (operation == "=") {
            lastOperation = null
            lastValue = "0"
        } else {
            lastOperation = operation
            lastValue = result.toString()
        }
        displayEdtxt!!.setText(
            formatDecimalValues(
                result
            )
        )
        isOperationDone = true
    }
}