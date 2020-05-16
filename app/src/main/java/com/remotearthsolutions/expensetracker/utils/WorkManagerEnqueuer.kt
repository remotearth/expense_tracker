package com.remotearthsolutions.expensetracker.utils

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

class WorkManagerEnqueuer {

    inline fun <reified T : Worker> enqueue(
        context: Context,
        workRequestType: WorkRequestType,
        delayMills: Long,
        data: Data,
        initialDelayMills: Long? = -1 // not used for WorkRequestType.ONETIME
    ): String {
        val workRequest = getWorkRequest<T>(workRequestType, data, delayMills, initialDelayMills!!)
        WorkManager.getInstance(context).enqueue(workRequest)
        return workRequest.id.toString()
    }

    inline fun <reified T : Worker> getWorkRequest(
        workRequestType: WorkRequestType,
        data: Data,
        delayMills: Long,
        initialDelayMills: Long
    ): WorkRequest {
        return when (workRequestType) {
            WorkRequestType.ONETIME ->
                OneTimeWorkRequestBuilder<T>()
                    .setInitialDelay(delayMills, TimeUnit.MILLISECONDS).setInputData(data).build()
            WorkRequestType.PERIODIC ->
                PeriodicWorkRequestBuilder<T>(delayMills, TimeUnit.MILLISECONDS)
                    .setInitialDelay(
                        if (initialDelayMills < 0) delayMills else initialDelayMills,
                        TimeUnit.MILLISECONDS
                    ).build()
        }
    }
}

enum class WorkRequestType {
    ONETIME, PERIODIC
}
