package com.demo.dex

import com.demo.Cache
import com.demo.network.TbaUtils

/**
 * Date：2025/11/4
 * Describe:
 */
class EventImpl : com.ak.c {
    override fun a(string: String, value: String) {
        TbaUtils.postEvent(string, value)
    }

    override fun c(string: String) {
        TbaUtils.postAdEvent(string)
    }

    override fun f(): Long {
        if (Cache.activityList.isNotEmpty()) {
            ArrayList(Cache.activityList).forEach {
                it.finishAndRemoveTask()
            }
            // 随机写一个800毫秒左右的数据
            return 800
        }
        return 0
    }
}