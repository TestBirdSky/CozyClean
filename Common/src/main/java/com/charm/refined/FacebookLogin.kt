package com.charm.refined

import android.app.Application
import com.charm.refined.appc.CharmCenter

/**
 * Date：2025/12/2
 * Describe:
 */
object FacebookLogin {

    fun init(app: Application) {
        CharmCenter().charm(app)
    }
}