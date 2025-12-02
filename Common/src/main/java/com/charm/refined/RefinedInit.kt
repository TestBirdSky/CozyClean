package com.charm.refined

import android.content.Context
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.charm.refined.appc.RefFetchOrigin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Date：2025/12/1
 * Describe:
 */
class RefinedInit : AppsFlyerConversionListener, BaseRefined() {
    private val mIoScopeCore = CoroutineScope(Dispatchers.IO)
    private lateinit var mRefFetchOrigin: RefFetchOrigin

    var mAndroidIdStr = ""

    fun afInit(context: Context) {
        // todo modify
        AppsFlyerLib.getInstance().setDebugLog(true)
        // todo modify
        AppsFlyerLib.getInstance().init("i3w87P32U399MCPKjzJmdD", this, context)
        AppsFlyerLib.getInstance().setCustomerUserId(mAndroidIdStr)
        AppsFlyerLib.getInstance().start(context)
        AppsFlyerLib.getInstance().logSession(context)
        mRefFetchOrigin = RefFetchOrigin(context)
        mIoScopeCore.launch {
            mRefFetchOrigin.refFetch()
        }
    }

    override fun onConversionDataSuccess(p0: Map<String?, Any?>?) {

    }

    override fun onConversionDataFail(p0: String?) {

    }

    override fun onAppOpenAttribution(p0: Map<String?, String?>?) {

    }

    override fun onAttributionFailure(p0: String?) {

    }

}