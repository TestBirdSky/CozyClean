package com.demo.network

import com.demo.Cache
import com.demo.DemoHelper
import com.demo.Tools

/**
 * Date：2025/11/4
 * Describe:
 */
object TbaUtils {
    //
    val url = ""
    private val mTbaImpl by lazy { TbaImpl() }

    fun postEvent(name: String, value: String? = null) {
        if (DemoHelper.isPostLog.not() || DemoHelper.mustPostLog.contains(name).not()) {

            return
        }
        Tools.log("postEvent -$name --$value")
        mTbaImpl.postEvent(name, value)
    }

    fun postAdEvent(ad: String) {
        mTbaImpl.postAdEvent(ad)
    }

    fun postInstall(ref: String) {
        if (Cache.isPostSuccess) return
        mTbaImpl.postInstall(ref) {
            Cache.isPostSuccess = true
        }
    }
}