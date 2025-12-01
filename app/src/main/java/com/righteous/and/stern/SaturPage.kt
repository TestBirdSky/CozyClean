package com.righteous.and.stern

import android.content.Intent
import android.graphics.LinearGradient
import android.graphics.Shader
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.righteous.and.stern.databinding.SaturPageBinding

/**
 * SaturPage - 设置页面，实现 MVP 模式的 View 层
 * 职责：仅负责 UI 显示和用户交互
 */
class SaturPage : AppCompatActivity(), SaturPageContract.View {
    
    private val binding by lazy { SaturPageBinding.inflate(layoutInflater) }
    private lateinit var presenter: SaturPageContract.Presenter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        
        setupViews()
        setupPresenter()
        setupClickListeners()
        
        presenter.onViewCreated()
    }
    
    /**
     * 初始化视图
     */
    private fun setupViews() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.satur)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    
    /**
     * 初始化 Presenter
     */
    private fun setupPresenter() {
        presenter = SaturPagePresenter(this)
    }
    
    /**
     * 设置点击监听器
     */
    private fun setupClickListeners() {
        binding.backButton.setOnClickListener {
            presenter.onBackClicked()
        }
        
        binding.privacyCard.setOnClickListener {
            presenter.onPrivacyPolicyClicked()
        }
        
        binding.shareCard.setOnClickListener {
            presenter.onShareClicked()
        }
    }
    
    // ========== 实现 View 接口方法 ==========
    
    override fun setupGradientText() {
        binding.appNameText.post {
            val width = binding.appNameText.width.toFloat()
            val textShader = LinearGradient(
                0f, 0f, width, 0f,
                intArrayOf(
                    android.graphics.Color.parseColor("#FA9200"),
                    android.graphics.Color.parseColor("#945600")
                ),
                null,
                Shader.TileMode.CLAMP
            )
            binding.appNameText.paint.shader = textShader
            binding.appNameText.invalidate()
        }
    }
    
    override fun finishPage() {
        finish()
    }
    
    override fun openUrl(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    override fun shareContent(shareText: String, title: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, title)
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        
        try {
            startActivity(Intent.createChooser(shareIntent, "Share $title"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    override fun getPackageName(): String {
        return super.getPackageName()
    }
    
    override fun getAppName(): String {
        return getString(R.string.app_name)
    }
}