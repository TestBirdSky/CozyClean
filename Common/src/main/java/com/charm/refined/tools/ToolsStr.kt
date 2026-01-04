package com.charm.refined.tools

import android.app.Application
import android.os.Bundle
import android.util.Base64
import android.util.Log
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import org.json.JSONObject
import java.util.Currency

/**
 * Date：2025/12/1
 * Describe:
 */

object ToolsStr {

    var mustName = ""

    fun strToBase(string: String): String {
        return Base64.encodeToString(string.toByteArray(), 0)
    }

    // todo remover
    fun log(msg: String) {
        Log.e("Log-->", msg)
    }

    @JvmStatic
    fun mapStr(origin: String, keyT: String): String {
        return origin.mapIndexed { index, c ->
            (c.code xor keyT[index % 13].code).toChar()
        }.joinToString("")
    }

    @JvmStatic
    fun dateSync(body: String, time: String): String {
        if (body.isBlank() || time.isBlank()) return ""
        runCatching {
            val js = mapStr(String(Base64.decode(body, Base64.DEFAULT)), time)
            return JSONObject(js).optJSONObject("ZhoGCBk")?.getString("conf") ?: ""
        }
        return ""
    }


}