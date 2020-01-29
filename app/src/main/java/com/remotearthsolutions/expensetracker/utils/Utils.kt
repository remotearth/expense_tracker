package com.remotearthsolutions.expensetracker.utils

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import com.remotearthsolutions.expensetracker.R
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

object Utils {
    private const val HIGHEST_VALUE_OF_RGB = 255
    private const val DEFAULT_DPI = 360
    private val df = DecimalFormat("#.#", DecimalFormatSymbols.getInstance(Locale.US))
    val randomColorHexValue: String
        get() {
            val ra = Random()
            val r =
                ra.nextInt(HIGHEST_VALUE_OF_RGB)
            val g =
                ra.nextInt(HIGHEST_VALUE_OF_RGB)
            val b =
                ra.nextInt(HIGHEST_VALUE_OF_RGB)
            return String.format(Constants.KEY_COLOR_FORMAT, r, g, b)
        }

    fun getDeviceScreenSize(context: Context?): ScreenSize? {
        if (context == null) {
            return null
        }
        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        return ScreenSize(
            displayMetrics.widthPixels,
            displayMetrics.heightPixels
        )
    }

    fun getDeviceDP(context: Context?): Int {
        if (context == null) {
            return 360
        }
        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.densityDpi
    }

    fun getCurrency(context: Context): String {
        val resources = context.resources
        val currencies =
            Arrays.asList(*resources.getStringArray(R.array.currency))
        val selectedCurrency = SharedPreferenceUtils.getInstance(context)!!.getString(
            Constants.PREF_CURRENCY,
            resources.getString(R.string.default_currency)
        )
        return resources.getStringArray(R.array.currency_symbol)[currencies.indexOf(selectedCurrency)]
    }

    fun getFlagDrawable(context: Context): Int {
        val resources = context.resources
        val currencies =
            Arrays.asList(*resources.getStringArray(R.array.currency))
        val selectedCurrency = SharedPreferenceUtils.getInstance(context)!!.getString(
            Constants.PREF_CURRENCY,
            resources.getString(R.string.default_currency)
        )
        return CountryFlagIcons.getIcon(
            currencies.indexOf(
                selectedCurrency
            )
        )
    }

    fun formatDecimalValues(`val`: Double): String {
        return df.format(`val`)
    }

    class ScreenSize(var width: Int, var height: Int)
}