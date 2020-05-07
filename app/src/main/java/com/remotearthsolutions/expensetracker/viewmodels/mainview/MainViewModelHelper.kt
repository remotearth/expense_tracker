package com.remotearthsolutions.expensetracker.viewmodels.mainview

import android.util.Base64
import com.google.gson.Gson
import com.remotearthsolutions.expensetracker.utils.Constants
import java.nio.charset.Charset


object MainViewModelHelper {
    fun getMetaString(listObject: List<Any>): String {
        val expenseJson = Gson().toJson(listObject)
        return Base64.encodeToString(
            expenseJson.toByteArray(charset(Constants.KEY_UTF_VERSION)),
            Base64.NO_WRAP
        )
    }

    fun <T> getContentFromMetaString(metaString: String, type: Class<Array<T>>): List<T> {
        val jsonContent = String(
            Base64.decode(metaString, Base64.NO_WRAP),
            Charset.forName(Constants.KEY_UTF_VERSION)
        )
        return Gson().fromJson(jsonContent, type).toList()
    }
}