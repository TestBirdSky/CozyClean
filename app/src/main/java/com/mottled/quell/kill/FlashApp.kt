package com.mottled.quell.kill

import android.app.Application
import com.charm.refined.FacebookLogin

/**
 * Date：2025/12/2
 * Describe:
 */
class FlashApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FacebookLogin.init(this)
    }
}