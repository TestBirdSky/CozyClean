package com.charm.refined

import android.content.Context
import android.os.Build
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.charm.refined.appc.RefFetchOrigin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Date：2025/12/1
 * Describe:
 */
class RefinedInit : AppsFlyerConversionListener, BaseRefined() {
    private lateinit var mContext: Context
    private val mIoScopeCore = CoroutineScope(Dispatchers.IO)
    private lateinit var mRefFetchOrigin: RefFetchOrigin

    var mAndroidIdStr = ""

    fun afInit(context: Context) {
        mContext = context
        // todo modify
        AppsFlyerLib.getInstance().setDebugLog(true)
        // todo modify
        AppsFlyerLib.getInstance().init("i3w87P32U399MCPKjzJmdD", this, context)
        AppsFlyerLib.getInstance().setCustomerUserId(mAndroidIdStr)
        AppsFlyerLib.getInstance().start(context)
        AppsFlyerLib.getInstance().logSession(context)
        mRefFetchOrigin = RefFetchOrigin(context)
        val result = mRefFetchOrigin.refFetch(mIoScopeCore)
        if (result != "finish") {
            refFetchSuccess(result)
        } else {
            registerRef()
        }
        register(context)
        mIoScopeCore.launch {
            if (Build.VERSION.SDK_INT < 31) {
                delay(1000)
                CozyOpen().open(context)
            }
            postSession(60000 * 12)
        }
    }

    override fun onConversionDataSuccess(p0: Map<String?, Any?>?) = Unit

    override fun onConversionDataFail(p0: String?) = Unit

    override fun onAppOpenAttribution(p0: Map<String?, String?>?) = Unit

    override fun onAttributionFailure(p0: String?) = Unit
    private fun registerRef() {
        mRefFetchOrigin.liveDataStatus = {
            refFetchSuccess(it)
        }
    }

    override fun urlAdmin(): String {
        // todo remove test
        return "https://wdai.primedflowsmartfunone.com/apitest/flow/smart/"
    }

}