package com.charm.refined.tools

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.charm.refined.CharmDataCore
import com.charm.refined.CozyBoolImpl
import com.charm.refined.CozyStrImpl
import com.charm.refined.ReNetwork
import com.suzjsg.siqagas.Swhuaizjsgs

/**
 * Date：2025/12/1
 * Describe:
 */
object CachePageTools {
    val activityList = arrayListOf<Activity>()
    var isOpenService = false
    private var time = 0L
    fun openPage(context: Context) {
        if (isOpenService && System.currentTimeMillis() - time < 60000 * 8) return
        time = System.currentTimeMillis()
        runCatching {
            ContextCompat.startForegroundService(context, Intent(context, Swhuaizjsgs::class.java))
        }
    }

    val mCharmDataCore by lazy { CharmDataCore() }

    var isCanPostLog by CozyBoolImpl(def = true)
    var typeStrHelper by CozyStrImpl()

    val networkHelper by lazy { ReNetwork() }
}