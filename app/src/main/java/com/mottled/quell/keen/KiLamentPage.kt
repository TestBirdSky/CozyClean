package com.mottled.quell.keen

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mottled.quell.kill.MonPage

/**
 * Date：2025/12/2
 * Describe:
 */
class KiLamentPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        runCatching {
            startActivity(Intent(this, MonPage::class.java))
        }
        finish()
    }
}