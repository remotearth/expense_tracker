package com.remotearthsolutions.expensetracker.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

inline fun <reified T : Fragment> FragmentManager.findViewPagerFragmentByTag(viewpagerId:Int, fragmentPosition: Int): T? {
    val fragmentTag = "android:switcher:${viewpagerId}:${fragmentPosition}"
    return findFragmentByTag(fragmentTag) as T?
}
