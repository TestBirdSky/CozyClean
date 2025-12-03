package com.righteous.and.stern

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Date：2025/12/2
 * Describe:
 */
class FriActivityFastGo : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        runCatching {
            startActivity(Intent(this, MonPage::class.java))
        }
        finish()
    }
}