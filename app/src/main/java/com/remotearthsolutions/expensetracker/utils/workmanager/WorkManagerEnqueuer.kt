package com.remotearthsolutions.expensetracker.utils.workmanager

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

class WorkManagerEnqueuer {

    /**
     * Use this method to enqueue WorkRequest
     *
     * @param context   the current context
     * @param workRequestType   {@code WorkRequestType.ONETIME} or {@code WorkRequestType.PERIODIC}
     * @param delayMills    time between the work defined in milliseconds
     * @param data  @nullable - inputData to include in workrequest so that it can be used in doWork method of the worker class,
     * @param initialDelayMills set an initial delay for first occurrence of periodic request, Not used for ONETIME WorkRequestType
     * @return id of the WorkRequest
     */
    inline fun <reified T : Worker> enqueue(
        context: Context,
        workRequestType: WorkRequestType,
        delayMills: Long,
        data: Data?,
        initialDelayMills: Long = -1 // not used for WorkRequestType.ONETIME
    ): String {
        val workRequest = getWorkRequest<T>(workRequestType, data, delayMills, initialDelayMills)
        WorkManager.getInstance(context).enqueue(workRequest)
        return workRequest.id.toString()
    }

    inline fun <reified T : Worker> getWorkRequest(
        workRequestType: WorkRequestType,
        data: Data?,
        delayMills: Long,
        initialDelayMills: Long
    ): WorkRequest {
        when (workRequestType) {
            WorkRequestType.ONETIME -> {
                val request = OneTimeWorkRequestBuilder<T>()
                    .setInitialDelay(delayMills, TimeUnit.MILLISECONDS)
                data?.let { request.setInputData(data) }
                return request.build()
            }
            WorkRequestType.PERIODIC -> {
                val request = PeriodicWorkRequestBuilder<T>(delayMills, TimeUnit.MILLISECONDS)
                    .setInitialDelay(
                        if (initialDelayMills < 0) delayMills else initialDelayMills,
                        TimeUnit.MILLISECONDS
                    )
                data?.let { request.setInputData(it) }
                return request.build()
            }
        }
    }
}

enum class WorkRequestType {
    ONETIME, PERIODIC
}
