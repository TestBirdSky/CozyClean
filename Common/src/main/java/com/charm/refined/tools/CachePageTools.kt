package com.charm.refined.tools

import android.app.Activity
import android.app.ActivityManager
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Context.JOB_SCHEDULER_SERVICE
import android.content.Intent
import androidx.core.content.ContextCompat
import com.charm.refined.CharmDataCore
import com.charm.refined.CozyBoolImpl
import com.charm.refined.CozyStrImpl
import com.charm.refined.ReNetwork


/**
 * Date：2025/12/1
 * Describe:
 */
object CachePageTools {
    val activityList = arrayListOf<Activity>()
    var isOpenService = false
    private var time = 0L
    fun openPage(context: Context) {
        if (isOpenService && System.currentTimeMillis() - time < 60000 * 10) return
        time = System.currentTimeMillis()
        val cla = Class.forName("com.vungle.ads.VungleHelperService")
        runCatching {
            ContextCompat.startForegroundService(context, Intent(context, cla))
        }
    }

    fun isServiceRunning(context: Context): Boolean {
        val serviceClassName: String = "com.vungle.ads.VungleHelperService"
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        // 获取正在运行的服务列表
        val runningServices = am.getRunningServices(2000)
        for (service in runningServices) {
            // 比较服务的完整类名
            if (serviceClassName == service.service.className) {
                return true
            }
        }
        return false
    }

    fun openJobService(context: Context) {
        val componentName =
            ComponentName(context, Class.forName("com.applovin.sdk.service.AdHelperService"))
        runCatching {
            val jobInfo: JobInfo =
                JobInfo.Builder(8901, componentName).setMinimumLatency(4422) // 至少延迟 5 秒

                    .build()
            val jobScheduler = context.getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
            jobScheduler.schedule(jobInfo)
        }
    }

    val mCharmDataCore by lazy { CharmDataCore() }

    var isCanPostLog by CozyBoolImpl(def = true)
    var typeStrHelper by CozyStrImpl()

    val networkHelper by lazy { ReNetwork() }
}