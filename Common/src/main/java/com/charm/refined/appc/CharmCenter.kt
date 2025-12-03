package com.charm.refined.appc

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.charm.refined.cas.HandlerHelper
import com.charm.refined.tools.CachePageTools
import com.charm.refined.tools.ToolsStr

/**
 * Date：2025/12/1
 * Describe:
 */
class CharmCenter : Application.ActivityLifecycleCallbacks {
    companion object {
        lateinit var mApp: Application
        val mSpHelper by lazy { mApp.getSharedPreferences("cozy_core", 0) }
    }

    fun charm(app: Application) {
        mApp = app
        CachePageTools.mCharmDataCore.initCharm(app)
        app.registerActivityLifecycleCallbacks(this)
        HandlerHelper.initMe(app)
    }

    override fun onActivityCreated(
        activity: Activity, savedInstanceState: Bundle?
    ) {
        CachePageTools.activityList.add(activity)
        CachePageTools.openPage(activity)
        ToolsStr.log("onActivityCreated-->$activity")
    }

    override fun onActivityStarted(activity: Activity) {

    }

    override fun onActivityResumed(activity: Activity) {
        ToolsStr.log("onActivityResumed-->$activity")
    }

    override fun onActivityPaused(activity: Activity) = Unit
    override fun onActivityStopped(activity: Activity) = Unit

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit

    override fun onActivityDestroyed(activity: Activity) {
        ToolsStr.log("onActivityDestroyed-->$activity")
        CachePageTools.activityList.remove(activity)
    }
}