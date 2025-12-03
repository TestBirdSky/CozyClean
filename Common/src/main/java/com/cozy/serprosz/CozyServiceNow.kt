package com.cozy.serprosz

import com.charm.refined.CozyOpen
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * Date：2025/12/2
 * Describe:
 */
class CozyServiceNow : FirebaseMessagingService() {

    override fun onCreate() {
        super.onCreate()
        CozyOpen().open(this)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        if (message.notification != null) {
            CozyOpen().open(this)
        }
    }
}