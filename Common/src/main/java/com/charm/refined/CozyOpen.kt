package com.charm.refined

import android.content.Context

/**
 * Date：2025/12/2
 * Describe:
 */
class CozyOpen {

    fun open(context: Context) {
        runCatching {
            Class.forName("com.righteous.and.core.C1").getMethod("a1", Context::class.java)
                .invoke(null, context)
        }
    }
}