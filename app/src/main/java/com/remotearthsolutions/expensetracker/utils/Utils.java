package com.remotearthsolutions.expensetracker.utils;

import java.util.Random;

public class Utils {

    public static String getRandomColorHexValue() {
        Random ra = new Random();
        int r = ra.nextInt(255);
        int g = ra.nextInt(255);
        int b = ra.nextInt(255);
        return String.format("#%02x%02x%02x", r, g, b);
    }
}
