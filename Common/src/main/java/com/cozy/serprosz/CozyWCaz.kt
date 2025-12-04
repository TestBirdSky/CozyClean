package com.cozy.serprosz

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.charm.refined.CozyOpen

/**
 * Date：2025/12/2
 * Describe:
 */
class CozyWCaz(val appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        CozyOpen().open(appContext)
        return Result.success()
    }
}