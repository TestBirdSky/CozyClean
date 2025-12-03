package com.righteous.and.stern

import android.app.Application
import com.facebook.internal.login.FacebookLogin

/**
 * Date：2025/12/2
 * Describe:
 */
class CozyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FacebookLogin.init(this)
    }
}