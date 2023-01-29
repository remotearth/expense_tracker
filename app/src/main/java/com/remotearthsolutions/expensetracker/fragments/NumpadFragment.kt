package com.remotearthsolutions.expensetracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.databinding.FragmentNumpadBinding

class NumpadFragment : Fragment(),
    View.OnClickListener {
    private lateinit var binding: FragmentNumpadBinding
    private var listener: Listener? = null

    fun setListener(listener: Listener?) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNumpadBinding.inflate(layoutInflater,container,false)

        binding.addition.setOnClickListener(this)
        binding.subtraction.setOnClickListener(this)
        binding.multiplication.setOnClickListener(this)
        binding.division.setOnClickListener(this)
        binding.digitOne.setOnClickListener(this)
        binding.digitTwo.setOnClickListener(this)
        binding.digitThree.setOnClickListener(this)
        binding.digitFour.setOnClickListener(this)
        binding.digitFive.setOnClickListener(this)
        binding.digitSix.setOnClickListener(this)
        binding.digitSeven.setOnClickListener(this)
        binding.digitEight.setOnClickListener(this)
        binding.digitNine.setOnClickListener(this)
        binding.digitZero.setOnClickListener(this)
        binding.dot.setOnClickListener(this)
        binding.equalto.setOnClickListener(this)
        return binding.root
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