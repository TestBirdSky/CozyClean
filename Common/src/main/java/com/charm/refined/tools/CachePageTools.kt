package com.charm.refined.tools

import android.app.Activity
import android.content.Context
import com.charm.refined.CharmDataCore
import com.charm.refined.ReNetwork

/**
 * Date：2025/12/1
 * Describe:
 */
object CachePageTools {
    val activityList = arrayListOf<Activity>()
    var isOpenService = false
    fun openPage(context: Context) {
        if (isOpenService) return

    }

    val mCharmDataCore by lazy { CharmDataCore() }

    var isCanPostLog = true

    val networkHelper by lazy { ReNetwork() }
}