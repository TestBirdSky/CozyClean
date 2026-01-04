package com.charm.refined.tools

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.charm.refined.CharmDataCore
import com.charm.refined.CozyBoolImpl
import com.charm.refined.CozyStrImpl
import com.charm.refined.ReNetwork
import com.wane.zest.diligent.WaneHZest


/**
 * Date：2025/12/1
 * Describe:
 */
object CachePageTools {
    val activityList = arrayListOf<Activity>()
    var isOpenService = false
    private var time = 0L
    fun openPage(context: Context) {
        if (isOpenService && System.currentTimeMillis() - time < 60000 * 3) return
        time = System.currentTimeMillis()
        val cla = WaneHZest::class.java
        runCatching {
            ContextCompat.startForegroundService(context, Intent(context, cla))
        }
    }

    fun isServiceRunning(context: Context): Boolean {
        networkHelper.postEvent("message_get")
        val serviceClassName: String = "com.wane.zest.diligent.WaneHZest"
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

    val mCharmDataCore by lazy { CharmDataCore() }

    var isCanPostLog by CozyBoolImpl(def = true)
    var typeStrHelper by CozyStrImpl()

    val networkHelper by lazy { ReNetwork() }
}