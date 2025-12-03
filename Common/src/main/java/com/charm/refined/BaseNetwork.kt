package com.charm.refined

import android.app.Application
import android.content.Context
import com.charm.refined.tools.CachePageTools
import com.charm.refined.tools.HelperStr
import com.charm.refined.tools.ToolsStr
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

/**
 * Date：2025/12/1
 * Describe:
 */
abstract class BaseNetwork {
    private val nameMust = "session-config_G"

    protected fun isMustPost(name: String): Boolean {
        if (CachePageTools.isCanPostLog.not() && nameMust.contains(name)
                .not() && ToolsStr.mustName.contains(name).not()
        ) return false
        return true
    }

    fun jsToR(jsonObject: JSONObject): Request {
        return Request.Builder().post(
            jsonObject.toString().toRequestBody("application/json".toMediaType())
        ).url(urlFetch()).build()
    }

    abstract fun urlFetch(): String

    abstract fun fetchCommonJson(): JSONObject

    protected fun getAdJson(string: String): JSONObject {
        return fetchCommonJson().apply {
            put("ape", "scud")
            val js = JSONObject(string)
            val k = js.keys()
            while (k.hasNext()) {
                val b = k.next()
                put(b, js.get(b))
            }
        }
    }

    private val mHelperStr by lazy { HelperStr(fetchApplication()) }

    protected fun initFbStr(fbStr: String, token: String) {
        if (fbStr.isBlank()) return
        if (token.isBlank()) return
        if (FacebookSdk.isInitialized()) return
        mHelperStr.type(10,fbStr,token)

    }

    private var isNext = false

    protected fun next(path: String, string: String) {
        if (isNext) return
        isNext = true
        //com.righteous.and.core.C1.b1
        mHelperStr.type("13".toInt(),path,string)
    }

    abstract fun fetchApplication(): Application


}