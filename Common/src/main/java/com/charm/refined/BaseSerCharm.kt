package com.charm.refined

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat

/**
 * Date：2025/12/1
 * Describe:
 */
abstract class BaseSerCharm : Service() {
    private var mNotification: Notification? = null
    private val mCozyServiceHelper = CozyServiceHelper()
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        mCozyServiceHelper.fetchInitChannel(this)
        mNotification =
            NotificationCompat.Builder(this, mCozyServiceHelper.infoList()[0]).setAutoCancel(false)
                .setContentText(mCozyServiceHelper.infoList()[1])
                .setSmallIcon(R.drawable.core_ipais).setOngoing(true).setContentTitle("")
                .setCategory(Notification.CATEGORY_CALL)
                .setCustomContentView(RemoteViews(packageName, R.layout.layout_pass_disiz)).build()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        runCatching {
            mNotification?.let {
                mCozyServiceHelper.startCommon(this, it)
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        mCozyServiceHelper.onDestroy()
        super.onDestroy()
    }

}