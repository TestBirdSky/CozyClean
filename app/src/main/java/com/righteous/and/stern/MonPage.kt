package com.righteous.and.stern

import android.content.Intent
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * MonPage - 启动页面，实现 MVP 模式的 View 层
 * 职责：仅负责 UI 显示和用户交互
 */
class MonPage : AppCompatActivity(), MonPageContract.View {
    
    private lateinit var progressBar: ProgressBar
    private lateinit var appNameTextView: TextView
    private lateinit var presenter: MonPageContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.mon_page)
        
        setupViews()
        setupPresenter()
        disableBackButton()
        
        presenter.onViewCreated()
    }
    
    /**
     * 初始化视图
     */
    private fun setupViews() {
        // 设置窗口边距
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mon)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        // 初始化视图组件
        progressBar = findViewById(R.id.progress_bar)
        appNameTextView = findViewById(R.id.app_name)
    }
    
    /**
     * 初始化 Presenter
     */
    private fun setupPresenter() {
        presenter = MonPagePresenter(this)
    }
    
    /**
     * 禁用返回按钮
     */
    private fun disableBackButton() {
        onBackPressedDispatcher.addCallback(this) {
            // 启动页禁止返回
        }
    }
    
    // ========== 实现 View 接口方法 ==========
    
    override fun updateProgress(progress: Int) {
        progressBar.progress = progress
    }
    
    override fun navigateToNextPage() {
        startActivity(Intent(this, TuesPage::class.java))
        finish()
    }
    
    override fun setupGradientText(text: String) {
        appNameTextView.setTextColor(android.graphics.Color.BLACK)
        val paint = appNameTextView.paint
        paint.isAntiAlias = true
        val width = paint.measureText(text)
        val textShader = LinearGradient(
            0f, 0f, width, 0f,
            intArrayOf(0xFFFA9200.toInt(), 0xFF945600.toInt()),
            null,
            Shader.TileMode.CLAMP
        )
        appNameTextView.paint.shader = textShader
    }
    
    override fun onDestroy() {
        super.onDestroy()
        presenter.onViewDestroyed()
    }
}