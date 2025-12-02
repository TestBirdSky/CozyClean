package com.charm.refined

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import com.charm.refined.tools.CachePageTools

/**
 * Date：2025/12/1
 * Describe:
 */
class CozyServiceHelper {
    private val idCozy = 1899
    private val idStr = "Cozy_notif"


    fun fetchInitChannel(context: Context): CozyServiceHelper {
        val channel = NotificationChannel(
            idStr, "CozyChannel", NotificationManager.IMPORTANCE_DEFAULT
        )
        (context.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(
            channel
        )
        CachePageTools.isOpenService = true
        return this
    }

    fun infoList(): List<String> {
        return arrayListOf(idStr, "", "name", "paper")
    }

    fun startCommon(service: Service, notification: Notification) {
        service.startForeground(idCozy, notification)
    }

    fun onDestroy() {
        CachePageTools.isOpenService = false

    }

}