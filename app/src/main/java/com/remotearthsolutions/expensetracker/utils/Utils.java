package com.remotearthsolutions.expensetracker.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

import java.util.Random;

public class Utils {

    public static String getRandomColorHexValue() {
        Random ra = new Random();
        int r = ra.nextInt(255);
        int g = ra.nextInt(255);
        int b = ra.nextInt(255);
        return String.format("#%02x%02x%02x", r, g, b);
    }

    public static ScreenSize getDeviceScreenSize(Context context){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return new ScreenSize(displayMetrics.widthPixels,displayMetrics.heightPixels);
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
