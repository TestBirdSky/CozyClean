package com.demo

import android.app.Application
import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import java.util.UUID

/**
 * Date：2025/10/20
 * Describe:
 */
class AppImpl {

    // 从Application 进来
    fun onCreate(context: Application) {
        if (DemoProgressCheck().isMainProgress(context).not()) return
        if (Cache.mAndroidId.isBlank()) {
            Cache.mAndroidId = UUID.randomUUID().toString()
        }
        Cache.mApp = context
        AppsFlyerSdkInit().init(context, Cache.mAndroidId)

        context.registerActivityLifecycleCallbacks(DemoAcLifecycleListener())

        DemoHelper.openOneWorker(context)
        DemoHelper.openPeriodWorker(context)
        fetchRef(context)
        if (Cache.isTopicSuccess.not()) {
            runCatching {
                Firebase.messaging.subscribeToTopic("topic_xx").addOnSuccessListener {
                    Cache.isTopicSuccess = true
                }
            }
        }

    }

    private fun fetchRef(context: Context) {
        DemoInstallReferrer().fe(context)
    }
}