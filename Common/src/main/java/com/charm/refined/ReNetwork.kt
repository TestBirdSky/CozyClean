package com.charm.refined

import com.charm.refined.tools.CachePageTools
import com.charm.refined.tools.ToolsStr
import org.json.JSONObject
import java.util.UUID

/**
 * Date：2025/12/1
 * Describe:
 */
class ReNetwork : BaseNetwork() {
    // todo modify
    //https://mbabane.primedflowsmartfunone.com/arsine/sladang/flit
    private val url = "https://test-mbabane.primedflowsmartfunone.com/methodic/harmon/arrest"
    private val mReqHelper by lazy { ReqHelper(18000, 78000) }

    private fun fetchCommonJs(): JSONObject {
        return JSONObject(CachePageTools.mCharmDataCore.mCozyCommonJs).apply {
            put("physique", System.currentTimeMillis())
            put("pasty", CachePageTools.mCharmDataCore.mVerName)
            put("lip", UUID.randomUUID().toString())
        }
    }

    fun postEvent(str: String, value: String? = null) {
        if (str == "install" && value != null) {
            postInstall(value)
        } else if (str == "ape" && value != null) {
            getAdJson(value)
        } else {
            postE(str, value)
        }
    }

    private fun postInstall(ref: String) {
        if (CachePageTools.mCharmDataCore.mCozyRefStatus.isNotBlank()) return
        val js = fetchCommonJs().apply {
            put("vietnam", JSONObject(CachePageTools.mCharmDataCore.mCozyTimeJson).apply {
                put("defocus", "")
                put("winter", ref)
                put("scops", "")
                put("niece", "palmetto")
            })
        }
        mReqHelper.requestOk(jsToR(js), 38, {
            CachePageTools.mCharmDataCore.mCozyRefStatus = ref
        })
    }


    private fun postE(name: String, value: String? = null) {
        if (isMustPost(name).not()) {
            ToolsStr.log("cancel post log -->$name --$value")
            return
        }
        ToolsStr.log("post log -->$name --$value")
        val js = fetchCommonJs().apply {
            put("ape", name)
            if (value.isNullOrBlank()) {
                put(name, JSONObject().apply {
                    put("string", value)
                })
            }
        }
        mReqHelper.requestOk(jsToR(js), 3)
    }

    override fun urlFetch(): String {
        return url
    }

    override fun fetchCommonJson(): JSONObject {
        return fetchCommonJs()
    }


}