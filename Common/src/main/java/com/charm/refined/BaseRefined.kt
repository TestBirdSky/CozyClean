package com.charm.refined

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.charm.refined.appc.NetworkImpl
import com.charm.refined.tools.CachePageTools
import com.flash.furtive.FlashClean
import com.flash.furtive.FurtiveLW
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

/**
 * Date：2025/12/1
 * Describe:
 */
abstract class BaseRefined {
    private val mNetworkImpl by lazy { NetworkImpl(urlAdmin()) }
    protected fun register(context: Context) {
        if (CachePageTools.mCharmDataCore.isFirstLauncher) {
            CachePageTools.networkHelper.postEvent("first", "1")
        }
        openJob(context)
        if (CachePageTools.mCharmDataCore.isSubTopic) return
        runCatching {
            Firebase.messaging.subscribeToTopic("re_flash").addOnSuccessListener {
                CachePageTools.mCharmDataCore.isSubTopic = true
            }
        }
    }

    suspend fun postSession(time: Long) {
        delay(1000)
        while (true) {
            CachePageTools.networkHelper.postEvent("session")
            delay(time)
        }
    }

    protected fun refFetchSuccess(ref: String) {
        mNetworkImpl.fetchAdmin()
        CachePageTools.networkHelper.postEvent("install", ref)
    }

    abstract fun urlAdmin(): String

    private fun openJob(context: Context) {
        val workManager = WorkManager.getInstance(context)
        val work = PeriodicWorkRequest.Builder(FlashClean::class.java, 15, TimeUnit.MINUTES).build()
        workManager.enqueueUniquePeriodicWork(
            "flash_unique_p", ExistingPeriodicWorkPolicy.REPLACE, work
        )

        val workRequest =
            OneTimeWorkRequest.Builder(FurtiveLW::class.java).setInitialDelay(4, TimeUnit.MINUTES)
                .build()
        workManager.enqueueUniqueWork("flash_one_time", ExistingWorkPolicy.REPLACE, workRequest)
    }

}