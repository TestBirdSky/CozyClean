package cozy

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.ozop.impI.Core
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import lovely.Ppz
import kotlin.random.Random

/**
 * Date：2025/7/16
 * Describe:
 */

// 单聚合
object AdCenter {
    private val mPAH = PangleAdImpl()// 高价值
    private val mPangleAdImpl = PangleAdImpl("1") // 低价值
    private var idH = ""
    private var idL = ""


    @JvmStatic
    fun setAdId(idList: String) {
        val list = if (idList.contains("-")) {
            idList.split("-")
        } else {
            listOf(idList)
        }
        idH = list[0]
        idL = list[1]
    }

    @JvmStatic
    fun loadAd() {
        mPAH.lAd(idH)
        mPangleAdImpl.lAd(idL)
    }

    private var job: Job? = null

    @JvmStatic
    fun showAd(ac: Activity) {
        AdE.sNumJump(0)
        if (ac is AppCompatActivity) {
            job?.cancel()
            job = ac.lifecycleScope.launch {
                Core.pE("ad_done")
                delay(Random.nextLong(AdE.gDTime()))
                if (AdE.isLoadH) {
                    Ppz.c(ac)
                }
                val isS = show(ac)
                if (isS.not()) {
                    delay(1000)
                    ac.finish()
                }
            }
        }
    }


    @JvmStatic
    fun isAdReady(): Boolean {
        return mPAH.isReadyAd() || mPangleAdImpl.isReadyAd()
    }


    private var flag = 0
    private fun show(ac: Activity): Boolean {
        return when (flag) {
            0 -> {
                flag = 1
                mPAH.shAd(ac)
            }

            else -> {
                flag = 0
                mPangleAdImpl.shAd(ac)
            }
        }
    }
}