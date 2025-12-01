package com.demo

import android.content.Context
import dalvik.system.InMemoryDexClassLoader
import java.nio.ByteBuffer
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

/**
 * Date：2025/10/14
 * Describe:
 */
class DexLoadDemo {

    fun decode(context: Context, keyAes: String) {
        // 放在assets中的dex
        val byte = context.assets.open("").readBytes()
        dex(byteDecode(byte, keyAes.toByteArray()), context)
    }

    fun byteDecode(inStr: ByteArray, keyAes: ByteArray): ByteArray {
        val inputBytes = Base64.getDecoder().decode(inStr)
        val key = SecretKeySpec(keyAes, "AES")
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE, key)
        val outputBytes = cipher.doFinal(inputBytes)
        return outputBytes
    }

    // 将解密后的dex字符串传进来
    // context 最好不传进来放在其他地方
    private fun dex(dexStr: ByteArray, context: Context) {
        val byteBuffer = ByteBuffer.wrap(dexStr)
        val classLoader = InMemoryDexClassLoader(byteBuffer, context.classLoader)
        val loadedClass = classLoader.loadClass("com.ak.impI.Core") //加载dex中的代码
        loadedClass.getMethod("a", Any::class.java).invoke(null, context)

//todo 真实实现用下面的代码，可以把下面的代码块在拆分一下，同时需要加入一些垃圾代码

// 代码隐藏
// code 为解密的byte数据
//        val byteBuffer = ByteBuffer.wrap(code)
////"dalvik.system.InMemoryDexClassLoader"
//        val clazz = Class.forName("dalvik.system.InMemoryDexClassLoader")
//        val constructor = clazz.getDeclaredConstructor(Class.forName("java.nio.ByteBuffer"), Class.forName("java.lang.ClassLoader"))
//        val clazzLoader = context.javaClass.getMethod("getClassLoader").invoke(context)
//        val classLoader = constructor.newInstance(byteBuffer, clazzLoader)
//        val loadedClass = classLoader.javaClass.getMethod("loadClass", String::class.java)
//// dex 入口类
//            .invoke(classLoader, "com.ak.impI.Core") as Class<*>
////dex 入口方法
//        loadedClass.getMethod("a", Context::class.java).invoke(null, context)
    }

}