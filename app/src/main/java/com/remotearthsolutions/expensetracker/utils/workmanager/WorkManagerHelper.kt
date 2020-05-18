package com.remotearthsolutions.expensetracker.utils.workmanager

import android.content.Context
import androidx.work.WorkManager
import java.util.*


object WorkManagerHelper {
    fun cancelWorkRequest(context: Context, requestId: String) {
        WorkManager.getInstance(context)
            .cancelWorkById(UUID.fromString(requestId))
    }
}