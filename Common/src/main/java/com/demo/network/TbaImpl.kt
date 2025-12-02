package com.demo.network

import android.os.Build
import com.charm.refined.tools.ToolsStr
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
import java.util.UUID

/**
 * Date：2025/11/4
 * Describe:
 * TBA 文档中的要求进行数据组装
 */
class TbaImpl {
    private val mOkHttpClient = OkHttpClient()
    private val mIoScope = CoroutineScope(Dispatchers.IO + SupervisorJob())


    fun postEvent(name: String, value: String? = null) {

    }

    fun postAdEvent(ad: String) {

    }

    fun postInstall(ref: String, success: () -> Unit) {
        // TBA 文档中的要求进行数据组装
        val js = createJson()

        // install 事件要多尝试几次至少15次
        requestOk(jsToR(js),30,success)
    }

    //按照TBA文档中的进行拼接
    public fun createJson(): JSONObject {
        return JSONObject().apply {
            put("", UUID.randomUUID().toString())
            put("", "")
            put("", "")
            put("", "_")
            put("", System.currentTimeMillis())
            put("", Build.MODEL)
            put("", Build.BRAND)
            put("", Build.VERSION.SDK_INT)
        }
    }


    private fun requestOk(request: Request, numRetry: Int, success: () -> Unit = {}) {
        mOkHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                if (numRetry > 0) {
                    mIoScope.launch {
                        delay(20000)
                        requestOk(request, numRetry - 1, success)
                    }
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val res = response.body?.string() ?: ""
                val isSuccess = response.isSuccessful && response.code == 200
                ToolsStr.log("onResponse--->$res --isSuccess$isSuccess")
                if (isSuccess) {
                    success.invoke()
                } else {
                    if (numRetry > 0) {
                        mIoScope.launch {
                            delay(50000)
                            requestOk(request, numRetry - 1, success)
                        }
                    }
                }
            }
        })
    }

    private fun jsToR(jsonObject: JSONObject): Request {
        return Request.Builder().post(
            jsonObject.toString().toRequestBody("application/json".toMediaType())
        ).url(TbaUtils.url).build()
    }

}