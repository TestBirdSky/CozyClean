package com.charm.refined.appc

import android.content.Context
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.android.installreferrer.api.ReferrerDetails
import com.charm.refined.tools.CachePageTools
import kotlinx.coroutines.delay

/**
 * Date：2025/12/1
 * Describe:
 */
class RefFetchOrigin(val context: Context) {

    suspend fun refFetch(): String {
        while (CachePageTools.mCharmDataCore.mCozyRefStr.isBlank()) {
            fetchReferrer(context)
            delay(20000)
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

}