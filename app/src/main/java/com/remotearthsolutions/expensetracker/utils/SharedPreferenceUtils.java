package com.remotearthsolutions.expensetracker.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferenceUtils {

    private static SharedPreferenceUtils instance;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private SharedPreferenceUtils(Context context) {
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sp.edit();
    }

    public static SharedPreferenceUtils getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferenceUtils(context);
        }

        return instance;
    }

    public void putInt(String key, int val) {
        editor.putInt(key, val).apply();
    }

    public int getInt(String key, int defaultVal) {
        return sp.getInt(key, defaultVal);
    }

    public void putString(String key, String val) {
        editor.putString(key, val).apply();
    }

    public String getString(String key, String defaultVal) {
        return sp.getString(key, defaultVal);
    }


    public void putBoolean(String key, boolean val) {
        editor.putBoolean(key, val).apply();
    }

    public boolean getBoolean(String key, boolean defaultVal) {
        return sp.getBoolean(key, defaultVal);
    }

    public void logOut() {
        editor.clear().commit();

    }
}
