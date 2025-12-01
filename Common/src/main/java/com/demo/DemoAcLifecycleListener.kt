package com.demo

import android.app.Activity
import android.app.Application
import android.os.Bundle

/**
 * Date：2025/10/19
 * Describe:
 */
class DemoAcLifecycleListener : Application.ActivityLifecycleCallbacks {


    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        DemoHelper.openNotification(activity)
        Cache.activityList.add(activity)
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {

    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {
        Cache.activityList.remove(activity)
    }
}