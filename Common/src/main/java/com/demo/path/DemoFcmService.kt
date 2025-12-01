package com.demo.path

import com.demo.DemoHelper
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * Date：2025/10/19
 * Describe:
 * 需要自己重新创建且路径和path目录下的不会被混淆的类最好不要在同一个包目录下从demo开始都算同一个目录
 * 命名最好不要出现FcmService
 *  *  todo remove
 */
class DemoFcmService : FirebaseMessagingService() {

    override fun onCreate() {
        super.onCreate()
        // 反射或者加点垃圾代码来实现
        DemoHelper.openNotification(this)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

    }
}