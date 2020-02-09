package com.remotearthsolutions.expensetracker.activities.helpers

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.remotearthsolutions.expensetracker.R


object FragmentLoader {
    fun load(activity: AppCompatActivity, fragment: Fragment, title: String, tag: String) {
        activity.supportActionBar!!.title = title
        val fragmentTransaction = activity.supportFragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(
            R.anim.slide_in_up,
            0,
            0,
            R.anim.slide_out_down
        )
        fragmentTransaction.addToBackStack(tag)
        fragmentTransaction.add(
            R.id.framelayout,
            fragment,
            tag
        )
        fragmentTransaction.commit()
    }
}