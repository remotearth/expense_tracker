package com.remotearthsolutions.expensetracker.fragments.main

import com.remotearthsolutions.expensetracker.databinding.FragmentMainBinding


class Helper {

    companion object {
        fun resetDateRangeBtns(binding: FragmentMainBinding?) {
            binding!!.dailyRangeBtn.setIsSelected(false)
            binding.weeklyRangeBtn.setIsSelected(false)
            binding.monthlyRangeBtn.setIsSelected(false)
            binding.yearlyRangeBtn.setIsSelected(false)
        }
    }
}
