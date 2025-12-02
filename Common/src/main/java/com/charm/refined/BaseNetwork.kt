package com.charm.refined

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

    fun jsToR(jsonObject: JSONObject): Request {
        return Request.Builder().post(
            jsonObject.toString().toRequestBody("application/json".toMediaType())
        ).url(urlFetch()).build()
    }

    abstract fun urlFetch(): String
}