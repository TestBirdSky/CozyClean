package com.charm.refined.tools

import android.util.Base64
import android.util.Log

/**
 * Date：2025/12/1
 * Describe:
 */

object ToolsStr {

    fun strToBase(string: String): String {
        return Base64.encodeToString(string.toByteArray(), 0)
    }

    // todo remover
    fun log(msg: String) {
        Log.e("Log-->", msg)
    }
}