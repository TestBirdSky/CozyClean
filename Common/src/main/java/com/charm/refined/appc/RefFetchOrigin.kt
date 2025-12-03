package com.charm.refined.appc

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.android.installreferrer.api.ReferrerDetails
import com.charm.refined.tools.CachePageTools
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Date：2025/12/1
 * Describe:
 */
class RefFetchOrigin(val context: Context) {

    var liveDataStatus: ((res: String) -> Unit)? = null

    fun refFetch(scope: CoroutineScope): String {
        val ref = CachePageTools.mCharmDataCore.mCozyRefStr
        if (ref.isBlank()) {
            scope.launch {
                while (CachePageTools.mCharmDataCore.mCozyRefStr.isBlank()) {
                    fetchReferrer(context)
                    delay(20000)
                }
            }
        } else {
            return ref
        }
        return "finish"
    }

    private fun fetchReferrer(context: Context) {
        val referrerClient = InstallReferrerClient.newBuilder(context).build()
        referrerClient.startConnection(object : InstallReferrerStateListener {
            override fun onInstallReferrerSetupFinished(p0: Int) {
                runCatching {
                    if (p0 == InstallReferrerClient.InstallReferrerResponse.OK) {
                        val response: ReferrerDetails = referrerClient.installReferrer
                        CachePageTools.mCharmDataCore.mCozyRefStr = response.installReferrer
                        post(response)
                        liveDataStatus?.invoke(response.installReferrer)
                        liveDataStatus = null
                        referrerClient.endConnection()
                    } else {
                        referrerClient.endConnection()
                    }
                }.onFailure {
                    referrerClient.endConnection()
                }
            }

            override fun onInstallReferrerServiceDisconnected() = Unit
        })
    }

    private fun post(res: ReferrerDetails) {
        val strBuild = StringBuilder()
        strBuild.append(res.referrerClickTimestampSeconds)
        strBuild.append("-")
        strBuild.append(res.referrerClickTimestampServerSeconds)
        CachePageTools.mCharmDataCore.mCozyRefNumStr = strBuild.toString()
    }

}