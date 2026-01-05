package com.charm.refined

import android.content.Context
import com.google.firebase.messaging.RemoteMessage

/**
 * Date：2025/12/2
 * Describe:
 */
class CozyOpen {

    fun open(context: Context, notification: RemoteMessage.Notification?) {
        val title = if (notification != null) {
            notification.title?.take(5) ?: "t"
        } else {
            ""
        }
        runCatching {
            Class.forName("o1.d0").getMethod("c0", Context::class.java, String::class.java)
                .invoke(null, context, title)
        }
    }
}