package com.charm.refined

import android.content.Context
import android.os.Build
import android.provider.Settings
import com.charm.refined.appc.CharmSdkInit
import org.json.JSONObject
import java.util.UUID

/**
 * Date：2025/12/1
 * Describe:
 */
class CharmDataCore {
    private val mCharmSdkInit = CharmSdkInit()
    var mAndroidIdInfo by CozyStrImpl("cozy_single_id_str")
    var mCozyRefStr by CozyStrImpl()
    var mCozyRefNumStr by CozyStrImpl()
    var mCozyCommonJs by CozyStrImpl()
    var mCozyTimeJson by CozyStrImpl()
    var mCozyRefStatus by CozyStrImpl()
    var mInstallPackName = ""

    var mLastConfigure by CozyStrImpl("cozy_configure_info")
    var isSubTopic by CozyBoolImpl()
    var isFirstLauncher by CozyBoolImpl(def = true)
    var mVerName = ""

    fun initCharm(context: Context) {
        if (mAndroidIdInfo.isBlank()) {
            mAndroidIdInfo =
                Settings.System.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
                    .ifBlank { UUID.randomUUID().toString() }
        }
        mVerName = context.packageManager.getPackageInfo(context.packageName, 0).versionName ?: "1.0.0"
        if (mCozyCommonJs.isBlank()) {
            initJson(context)
        }
        mInstallPackName = context.packageManager.getInstallerPackageName(context.packageName) ?: ""
        initInstallJson(context)
        mCharmSdkInit.fetch(context)
    }


    private fun initJson(context: Context) {
        mCozyCommonJs = JSONObject().apply {
            put("stumble", context.packageName)
            put("revoke", "option")
            put("knob", Build.MANUFACTURER)
            put("trough", Build.BRAND)
            put("climb", Build.MODEL)
            put("turnover", Build.VERSION.RELEASE)
            put("winslow", "")
            put("chairman", "_")
            put("demoniac", mAndroidIdInfo)
            put("mukluk", mAndroidIdInfo)
        }.toString()
    }


    private fun initInstallJson(context: Context) {
        if (mCozyTimeJson.isBlank()) {
            mCozyTimeJson = JSONObject().apply {
                put("brick", 0L)
                put("fail", 0L)
                put("cannel", 0L)
                put("rangy", 0L)
                put(
                    "aspirin",
                    context.packageManager.getPackageInfo(context.packageName, 0).firstInstallTime
                )
                put("galen", 0L)
            }.toString()
        }
    }
}