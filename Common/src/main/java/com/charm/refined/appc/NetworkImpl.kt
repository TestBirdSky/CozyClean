package com.charm.refined.appc

import com.charm.refined.tools.CachePageTools
import com.charm.refined.tools.ToolsStr
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

/**
 * Date：2025/12/2
 * Describe:
 */
class NetworkImpl(val url: String) {
    private var mS = ""
    private val mOkClient = OkHttpClient()
    private val mNetworkHelper = NetworkHelper("dt")

    fun fetchAdmin() {
        val con = CachePageTools.mCharmDataCore.mLastConfigure
        if (con.isBlank()) {// 没配置直接进行获取
            fetch(6)
        } else {
            // 有配置先使用上一次的配置，然后在进行数据更新
            refConfigure(con)
            if (mS == "a") {
                mNetworkHelper.fetchRandomTime {
                    fetch(1)
                }
            } else {
                tryFetch()
            }
        }
    }

    private fun refConfigure(con: String) {
        runCatching {
            JSONObject(con).apply {
                val s = optString("polished_name")
                if (s == "Nefarious") { // 不能直接使用我这边方式，需要自行修改具体如何判断成A用户还是B用户
                    mS = "a"
                } else if (s.contains("Breathtaking")) {
                    if (mS == "a") {
                        return
                    }
                    mS = "b"
                }
                mNetworkHelper.refreshData(this)
                // 将数据保存在本地用sp或者mmkv等
                CachePageTools.mCharmDataCore.mLastConfigure = con
                if (mS == "a") {
                    mNetworkHelper.actionNext()
                }
            }
        }.onFailure {
            it.printStackTrace()
            CachePageTools.networkHelper.postEvent("json_failed", it.localizedMessage)
        }
    }

    fun fetch(num: Int) {
        mNetworkHelper.fetchRequest(url)?.let {
            requestAdmin(it, num)
        }
    }

    private fun requestAdmin(request: Request, num: Int) {
        CachePageTools.networkHelper.postEvent("config_R")
        mOkClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                if (num > 0) {
                    CachePageTools.networkHelper.postEvent("config_G", "error_net")
                    mNetworkHelper.mIoScope.launch {
                        delay(12000)
                        requestAdmin(request, num - 1)
                    }
                } else {
                    reqCozy("timeout")
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string() ?: ""
                val code = response.code
                if (code == 200) {
                    val res = ToolsStr.dateSync(body, response.headers[mNetworkHelper.d] ?: "")
                    if (res.isBlank()) {
                        reqCozy("null")
                    } else {
                        refConfigure(res)
                        tryFetch()
                        CachePageTools.networkHelper.postEvent("config_G", mS)
                    }
                } else {
                    if (num > 0) {
                        CachePageTools.networkHelper.postEvent("config_G", "${response.code}")
                        mNetworkHelper.mIoScope.launch {
                            delay(89111)
                            requestAdmin(request, num - 1)
                        }
                    } else {
                        reqCozy("timeout")
                    }
                }
            }
        })
    }

    private var num = 0
    private fun reqCozy(result: String) {
        CachePageTools.networkHelper.postEvent("config_G", result)
        if (mS.isBlank()) {
            if (num < 3) {
                mNetworkHelper.mIoScope.launch {
                    delay(60000)
                    fetch(1)
                }
            }
            num++
        } else {
            tryFetch()
        }
    }

    private fun tryFetch() {
        mNetworkHelper.mIoScope.launch {
            delay(mNetworkHelper.fetchTime(mS))
            fetch(1)
        }
    }

}