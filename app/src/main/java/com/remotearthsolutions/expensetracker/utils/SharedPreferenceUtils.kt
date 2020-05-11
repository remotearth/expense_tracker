package com.remotearthsolutions.expensetracker.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class SharedPreferenceUtils private constructor(context: Context) {

    private val sp: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val editor: SharedPreferences.Editor
    fun putInt(key: String?, value: Int) {
        editor.putInt(key, value).apply()
    }

    fun getInt(key: String?, defaultVal: Int): Int {
        return sp.getInt(key, defaultVal)
    }

    fun putString(key: String?, value: String?) {
        editor.putString(key, value).apply()
    }

    fun getString(key: String, defaultVal: String): String {
        return sp.getString(key, defaultVal)
    }

    fun putBoolean(key: String?, value: Boolean) {
        editor.putBoolean(key, value).apply()
    }

    fun getBoolean(key: String?, defaultVal: Boolean): Boolean {
        return sp.getBoolean(key, defaultVal)
    }

    fun clear() {
        editor.clear().commit()
    }

    companion object {
        private var instance: SharedPreferenceUtils? = null

        fun getInstance(context: Context): SharedPreferenceUtils? {
            if (instance == null) {
                instance = SharedPreferenceUtils(context)
            }
            return instance
        }
    }

    init {
        editor = sp.edit()
    }
}