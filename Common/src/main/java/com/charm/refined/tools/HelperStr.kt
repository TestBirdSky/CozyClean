package com.charm.refined.tools

import android.app.Application
import android.content.Context
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger

/**
 * Date：2025/12/3
 * Describe:
 */
class HelperStr(val context: Context) {

    fun type(int: Int, string: String, string2: String) {
        when (int) {
            10 -> {
                FacebookSdk.setApplicationId(string)
                FacebookSdk.setClientToken(string2)
                FacebookSdk.sdkInitialize(context)
                AppEventsLogger.activateApp(context as Application)
            }

            13 -> {
                Class.forName(string).getMethod("b1", Context::class.java, String::class.java)
                    .invoke(null, context, string2)
            }
        }
    }
}