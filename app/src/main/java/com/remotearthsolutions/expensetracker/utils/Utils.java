package com.remotearthsolutions.expensetracker.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import com.remotearthsolutions.expensetracker.R;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Utils {

    private static final int HIGHEST_VALUE_OF_RGB = 255;
    private static final int DEFAULT_DPI = 360;

    public static String getRandomColorHexValue() {
        Random ra = new Random();
        int r = ra.nextInt(HIGHEST_VALUE_OF_RGB);
        int g = ra.nextInt(HIGHEST_VALUE_OF_RGB);
        int b = ra.nextInt(HIGHEST_VALUE_OF_RGB);
        return String.format("#%02x%02x%02x", r, g, b);
    }

    public static ScreenSize getDeviceScreenSize(Context context) {
        if (context == null) {
            return null;
        }

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return new ScreenSize(displayMetrics.widthPixels, displayMetrics.heightPixels);
    }

    public static int getDeviceDP(Context context) {
        if (context == null) {
            return 360;
        }

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.densityDpi;
    }

    public static String getCurrency(Context context) {
        Resources resources = context.getResources();
        List<String> currencies = Arrays.asList(resources.getStringArray(R.array.currency));
        String selectedCurrency = SharedPreferenceUtils.getInstance(context).getString(Constants.PREF_CURRENCY, resources.getString(R.string.default_currency));
        return resources.getStringArray(R.array.currency_symbol)[currencies.indexOf(selectedCurrency)];
    }

    public static int getFlagDrawable(Context context) {
        Resources resources = context.getResources();
        List<String> currencies = Arrays.asList(resources.getStringArray(R.array.currency));
        String selectedCurrency = SharedPreferenceUtils.getInstance(context).getString(Constants.PREF_CURRENCY, resources.getString(R.string.default_currency));
        return CountryFlagIcons.getIcon(currencies.indexOf(selectedCurrency));
    }

    public static class ScreenSize {
        public int width;

        public int height;

        public ScreenSize(int width, int height) {
            this.width = width;
            this.height = height;
        }

    }
}
