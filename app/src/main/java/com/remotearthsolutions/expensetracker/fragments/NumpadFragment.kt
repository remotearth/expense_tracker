package com.remotearthsolutions.expensetracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.remotearthsolutions.expensetracker.R
import kotlinx.android.synthetic.main.fragment_numpad.view.*

class NumpadFragment : Fragment(),
    View.OnClickListener {
    private var listener: Listener? = null

    fun setListener(listener: Listener?) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_numpad, container, false)

        view.addition.setOnClickListener(this)
        view.subtraction.setOnClickListener(this)
        view.multiplication.setOnClickListener(this)
        view.division.setOnClickListener(this)
        view.digitOne.setOnClickListener(this)
        view.digitTwo.setOnClickListener(this)
        view.digitThree.setOnClickListener(this)
        view.digitFour.setOnClickListener(this)
        view.digitFive.setOnClickListener(this)
        view.digitSix.setOnClickListener(this)
        view.digitSeven.setOnClickListener(this)
        view.digitEight.setOnClickListener(this)
        view.digitNine.setOnClickListener(this)
        view.digitZero.setOnClickListener(this)
        view.dot.setOnClickListener(this)
        view.equalto.setOnClickListener(this)
        return view
    }

    override fun onClick(v: View) {
        try {
            when (v.id) {
                R.id.digitOne -> {
                    listener!!.onNumpadButtonClick(getString(R.string.one))
                }
                R.id.digitTwo -> {
                    listener!!.onNumpadButtonClick(getString(R.string.two))
                }
                R.id.digitThree -> {
                    listener!!.onNumpadButtonClick(getString(R.string.three))
                }
                R.id.digitFour -> {
                    listener!!.onNumpadButtonClick(getString(R.string.four))
                }
                R.id.digitFive -> {
                    listener!!.onNumpadButtonClick(getString(R.string.five))
                }
                R.id.digitSix -> {
                    listener!!.onNumpadButtonClick(getString(R.string.six))
                }
                R.id.digitSeven -> {
                    listener!!.onNumpadButtonClick(getString(R.string.seven))
                }
                R.id.digitEight -> {
                    listener!!.onNumpadButtonClick(getString(R.string.eight))
                }
                R.id.digitNine -> {
                    listener!!.onNumpadButtonClick(getString(R.string.nine))
                }
                R.id.digitZero -> {
                    listener!!.onNumpadButtonClick(getString(R.string.zero))
                }
                R.id.dot -> {
                    listener!!.onNumpadButtonClick(getString(R.string.point))
                }
                R.id.addition -> {
                    listener!!.onMathOperationButtonClick(getString(R.string.plus))
                }
                R.id.subtraction -> {
                    listener!!.onMathOperationButtonClick(getString(R.string.subtraction))
                }
                R.id.multiplication -> {
                    listener!!.onMathOperationButtonClick(getString(R.string.multiplication))
                }
                R.id.division -> {
                    listener!!.onMathOperationButtonClick(getString(R.string.division))
                }
                R.id.equalto -> {
                    listener!!.onMathOperationButtonClick(getString(R.string.equalto))
                }
            }
        } catch (e: NumberFormatException) {
            Toast.makeText(
                context,
                context?.resources?.getString(R.string.make_sure_enter_valid_number),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    interface Listener {
        fun onNumpadButtonClick(value: String?)
        @Throws(NumberFormatException::class)
        fun onMathOperationButtonClick(operation: String?)
    }
}