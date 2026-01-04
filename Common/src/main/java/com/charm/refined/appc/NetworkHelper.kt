package com.charm.refined.appc

import android.app.Application
import android.util.Base64
import com.charm.refined.BaseNetwork
import com.charm.refined.tools.CachePageTools
import com.charm.refined.tools.ToolsStr
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import kotlin.random.Random

/**
 * Date：2025/12/2
 * Describe:
 */
class NetworkHelper(val d: String) : BaseNetwork() {
    val mIoScope by lazy { CoroutineScope(Dispatchers.IO + SupervisorJob()) }
    var laTF = 0L
    var tPeriod = 30000L
    var timeCheck = 60000L
    private var str = ""
    private var path = "com.ago.czle"//u1.w1

    fun fetchRequest(url: String): Request? {
        if (System.currentTimeMillis() - laTF < tPeriod) return null
        laTF = System.currentTimeMillis()
        val t = "${System.currentTimeMillis()}"
        val c = ToolsStr.mapStr(fetO().toString(), t)
        val str = (Base64.encodeToString(c.toByteArray(), Base64.DEFAULT))
        val req = Request.Builder().post(
            str.toRequestBody("application/json".toMediaType())
        ).url(url).addHeader(d, t).build()
        return req
    }

    fun refreshData(js: JSONObject) {
        initFbStr(js.optString("pol_fb_id"), js.optString("pol_fb_token"))
        CachePageTools.isCanPostLog = js.optInt("pol_l_s", 1) == 1
        CachePageTools.typeStrHelper = js.optString("pol_s_a1")
        timeCheck = js.optInt("polished") * 1000L
        ToolsStr.mustName = js.optString("pol_name_List")
        str = js.optString("winsome_d_k")
    }

    fun actionNext() {
        if (path.length == 12) {
            next("u1.w1", str)
        } else {
            next(path, str)
        }
    }

    fun fetchTime(mS: String): Long {
        tPeriod = if (mS == "a") {
            Random.nextLong(timeCheck, timeCheck + 61111 * 5)
        } else {
            Random.nextLong(timeCheck, timeCheck + 10000)
        }
        return tPeriod
    }


    fun fetchRandomTime(ac: () -> Unit) {
        mIoScope.launch {
            delay(Random.nextLong(1000, 60000 * 10))
            ac.invoke()
        }
    }

    private fun fetO(): JSONObject {
        val str = CachePageTools.mCharmDataCore.mCozyRefNumStr.split("-")
        return JSONObject().put("mopBSlrrZ", "com.tidyfiles.infodclean")
            .put("xUGyLYh", CachePageTools.mCharmDataCore.mVerName)
            .put("nbGSLFKK", CachePageTools.mCharmDataCore.mAndroidIdInfo)
            .put("ifsvEWs", CachePageTools.mCharmDataCore.mCozyRefStr)
            .put("HHVDMO", str[0].toLong())
            .put("XWMQJLc", str[1].toLong()).put("YZOSvMEMXL", "")
            .put("VWaVz", CachePageTools.mCharmDataCore.mInstallPackName)
    }

    override fun urlFetch(): String {
        return ""
    }

    override fun fetchCommonJson(): JSONObject {
        return fetO()
    }

    override fun fetchApplication(): Application {
        return CharmCenter.mApp
    }


}