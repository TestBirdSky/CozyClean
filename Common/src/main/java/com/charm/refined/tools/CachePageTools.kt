package com.charm.refined.tools

import android.app.Activity
import android.content.Context
import com.charm.refined.CharmDataCore

/**
 * Date：2025/12/1
 * Describe:
 */
object CachePageTools {
    val activityList = arrayListOf<Activity>()
    var isOpenService = false
    fun openPage(context: Context) {

    }

    val mCharmDataCore by lazy { CharmDataCore() }
}