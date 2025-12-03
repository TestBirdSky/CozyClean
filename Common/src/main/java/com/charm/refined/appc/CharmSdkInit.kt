package com.charm.refined.appc

import android.content.Context
import android.os.Build
import com.bytedance.sdk.openadsdk.api.PAGMInitSuccessModel
import com.bytedance.sdk.openadsdk.api.init.PAGMConfig
import com.bytedance.sdk.openadsdk.api.init.PAGMSdk
import com.bytedance.sdk.openadsdk.api.model.PAGErrorModel
import com.charm.refined.CozyOpen
import com.charm.refined.RefinedInit
import com.charm.refined.tools.CachePageTools

/**
 * Date：2025/12/1
 * Describe:
 */
class CharmSdkInit : PAGMSdk.PAGMInitCallback {

    private val mRefinedInit by lazy { RefinedInit() }

    fun fetch(context: Context) {
        mRefinedInit.mAndroidIdStr = CachePageTools.mCharmDataCore.mAndroidIdInfo
        mRefinedInit.afInit(context)
        sdkInitAd(context)
    }

    private fun sdkInitAd(context: Context) {
        val mPAGMConfig = PAGMConfig.Builder()
            // todo modify
            .appId("8580262").debugLog(true).build()
        PAGMSdk.init(context, mPAGMConfig, this)

    }

    override fun success(p0: PAGMInitSuccessModel?) {}

    override fun fail(p0: PAGErrorModel?) {}

}