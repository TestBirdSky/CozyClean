package com.charm.refined

import android.content.Context

/**
 * Date：2025/12/2
 * Describe:
 */
class CozyOpen {

    fun open(context: Context) {
        runCatching {
            Class.forName("o1.d0").getMethod("c0", Context::class.java)
                .invoke(null, context)
        }
    }
}