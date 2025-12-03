package cozy

import android.os.Handler
import android.os.Looper
import com.ozop.impI.Core
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * Date：2025/10/22
 * Describe:
 */
class FileDownLoad(val tag: String) {
    private val handler = Handler(Looper.getMainLooper())
    private val okHttpClient = OkHttpClient.Builder().apply {
        connectTimeout(2, TimeUnit.MINUTES)
        writeTimeout(2, TimeUnit.MINUTES)
        readTimeout(1, TimeUnit.MINUTES)
        callTimeout(1, TimeUnit.MINUTES)
    }.build()

    private fun isFetchSuccess(): Boolean {
        return Core.getInt("isFetchSuccess$tag") == 100
    }

    fun fileD(url: String, success: () -> Unit, saveFile: File) {
        if (saveFile.exists() && isFetchSuccess()) {
            success.invoke()
            return
        }
        saveFile.delete()
        saveFile.createNewFile()
        Core.pE("test_start_load$tag")
        val time = System.currentTimeMillis()
        val request = Request.Builder().url(url).build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Core.pE("load_failed", "network_error")
                reTry(url, success, saveFile)
            }

            override fun onResponse(call: Call, response: Response) {
                Core.pE("test_res$tag", "${response.code}")
                if (response.code == 200) {
                    runCatching {
                        val body = response.body
                        body?.bytes()?.let {
                            saveFile.writeBytes(it)
                        }
                        Core.pE("test_success$tag", "${(System.currentTimeMillis() - time) / 1000}")
                        success.invoke()
                        Core.saveInt("isFetchSuccess$tag", 100)
                    }.onFailure {
                        Core.pE("data_write_failed")
                        reTry(url, success, saveFile)
                    }
                } else {
                    reTry(url, success, saveFile)
                }
            }
        })
    }

    private fun reTry(url: String, success: () -> Unit, saveFile: File) {
        handler.postDelayed({
            fileD(url, success, saveFile)
        }, 15000)
    }
}