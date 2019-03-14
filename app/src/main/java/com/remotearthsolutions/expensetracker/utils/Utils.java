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

    public static String getRandomColorHexValue() {
        Random ra = new Random();
        int r = ra.nextInt(255);
        int g = ra.nextInt(255);
        int b = ra.nextInt(255);
        return String.format(Constants.KEY_COLOR_FORMAT, r, g, b);
    }

    public static ScreenSize getDeviceScreenSize(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return new ScreenSize(displayMetrics.widthPixels, displayMetrics.heightPixels);
    }

    public static class ScreenSize {
        public int width;
        public int height;

        public ScreenSize(int width, int height) {
            this.width = width;
            this.height = height;
        }
    }

    public static String getCurrency(Context context) {
        Resources resources = context.getResources();
        List<String> currencies = Arrays.asList(resources.getStringArray(R.array.currency));
        String selectedCurrency = SharedPreferenceUtils.getInstance(context).getString(Constants.PREF_CURRENCY, resources.getString(R.string.default_currency));
        return resources.getStringArray(R.array.currency_symbol)[currencies.indexOf(selectedCurrency)];
    }
}
