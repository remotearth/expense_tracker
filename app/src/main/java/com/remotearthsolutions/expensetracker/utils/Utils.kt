package com.remotearthsolutions.expensetracker.utils

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.widget.Toast
import com.remotearthsolutions.expensetracker.R
import com.yariksoffice.lingver.Lingver
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.random.Random

object Utils {
    private const val HIGHEST_VALUE_OF_RGB = 255
    private const val DEFAULT_DPI = 360
    private val df = DecimalFormat("#.#", DecimalFormatSymbols.getInstance(Locale.US))
    val randomColorHexValue: String
        get() {
            val r = Random.nextInt(HIGHEST_VALUE_OF_RGB)
            val g = Random.nextInt(HIGHEST_VALUE_OF_RGB)
            val b = Random.nextInt(HIGHEST_VALUE_OF_RGB)
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
            listOf(*resources.getStringArray(R.array.currency))
        val selectedCurrency = SharedPreferenceUtils.getInstance(context)!!.getString(
            Constants.PREF_CURRENCY,
            resources.getString(R.string.default_currency)
        )
        return resources.getStringArray(R.array.currency_symbol)[currencies.indexOf(selectedCurrency)]
    }

    fun getFlagDrawable(context: Context): Int {
        val resources = context.resources
        val currencies =
            listOf(*resources.getStringArray(R.array.currency))
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

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    @Suppress("DEPRECATION")
    @SuppressWarnings("deprecation")
    private fun setLocale(context: Context, localeStr: String) {
        val configuration = context.resources.configuration
        val displayMetrics: DisplayMetrics = context.resources.displayMetrics
        configuration.setLocale(Locale(localeStr))
        Locale.setDefault(Locale(localeStr))
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            context.applicationContext.createConfigurationContext(configuration)
        } else {
            context.resources.updateConfiguration(context.resources.configuration, displayMetrics)
        }
    }

    fun setAppLanguage(context: Context) {
        val lang = SharedPreferenceUtils.getInstance(context)
            ?.getString(Constants.PREF_LANGUAGE, context.getString(R.string.default_language))!!
        val languages = context.resources.getStringArray(R.array.lanugages)
        val langCodes = context.resources.getStringArray(R.array.lanugageCode)
        val code = langCodes[languages.indexOf(lang)]
        Lingver.getInstance().setLocale(context, code)
    }

    class ScreenSize(var width: Int, var height: Int)
}