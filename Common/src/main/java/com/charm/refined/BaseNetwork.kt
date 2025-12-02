package com.charm.refined

import com.charm.refined.tools.CachePageTools
import com.demo.network.TbaUtils
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
        if (CachePageTools.isCanPostLog.not() && nameMust.contains(name).not()) return false
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
            put("ape","scud")
            val js = JSONObject(string)
            val k = js.keys()
            while (k.hasNext()) {
                val b = k.next()
                put(b, js.get(b))
            }
        }
    }
}