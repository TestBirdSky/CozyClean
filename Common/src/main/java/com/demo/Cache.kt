package com.demo

import android.app.Activity
import android.app.Application
import com.demo.network.AdminDemo

/**
 * Date：2025/11/4
 * Describe:
 */
object Cache {
    lateinit var mApp: Application
    val activityList = arrayListOf<Activity>()

    var mAppVersion = ""// todo 动态获取app版本

    val mAdminDemo by lazy { AdminDemo(mApp, "adminurl", mAndroidId, mAppVersion) }

    // 下面的数据需要持久化
    var mAndroidId = ""
    var isPostSuccess = false
    var isTopicSuccess = false
}