package com.charm.refined

import com.charm.refined.tools.ToolsStr
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

/**
 * Date：2025/12/1
 * Describe:
 */
class ReqHelper(val failedTime: Long, val succeedTime: Long) {
    private val mOkHttpClient = OkHttpClient()

    private val mIoScope by lazy { CoroutineScope(Dispatchers.IO) }
    fun requestOk(request: Request, numRetry: Int, success: () -> Unit = {}) {
        mOkHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                if (numRetry > 0) {
                    mIoScope.launch {
                        delay(failedTime)
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
                            delay(succeedTime)
                            requestOk(request, numRetry - 1, success)
                        }
                    }
                }
            }
        })
    }
}