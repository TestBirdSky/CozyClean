package com.righteous.and.stern

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.righteous.and.stern.databinding.ThursPageBinding
import com.righteous.and.stern.jqlait.WednesPage

class ThursPage : AppCompatActivity() {
    
    private val binding by lazy { ThursPageBinding.inflate(layoutInflater) }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.thurs)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        showLoadingDialog()
        setupViews()
        displayCleaningResult()
    }
    
    private fun showLoadingDialog() {
        // 显示加载对话框
        binding.loadingDialog.root.visibility = View.VISIBLE
        
        // 获取加载图片并启动旋转动画
        val loadingImage = binding.loadingDialog.root.findViewById<android.widget.ImageView>(R.id.img_loading)
        val imgBack = binding.loadingDialog.root.findViewById<android.widget.ImageView>(R.id.img_back)

        val rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_loading)
        loadingImage.startAnimation(rotateAnimation)
        
        // 1.5秒后隐藏加载对话框
        Handler(Looper.getMainLooper()).postDelayed({
            loadingImage.clearAnimation()
            binding.loadingDialog.root.visibility = View.GONE
        }, 1500)
        imgBack.setOnClickListener {
            finish()
        }
    }
    
    private fun setupViews() {
        // 返回按钮
        binding.imgBack.setOnClickListener {
            finish()
        }
        
        // Clean 卡片 - 跳转到 WednesPage
        binding.cardClean.setOnClickListener {
            val intent = Intent(this, WednesPage::class.java)
            startActivity(intent)
        }
        
        // Device 卡片 - 跳转到 SaturPage
        binding.cardDevice.setOnClickListener {
            val intent = Intent(this, SaturPage::class.java)
            startActivity(intent)
        }
        
        // CPU 卡片 - 跳转到 FridayPage
        binding.cardCpu.setOnClickListener {
            val intent = Intent(this, FridayPage::class.java)
            startActivity(intent)
        }
    }
    
    private fun displayCleaningResult() {
        // 接收清理结果
        val deletedSize = intent.getLongExtra("deleted_size", 0L)
        
        // 格式化大小
        val sizeInfo = formatSize(deletedSize)
        val sizeText = sizeInfo.first
        val sizeUnit = sizeInfo.second
        
        // 创建带颜色的文本
        val fullText = "Saved ${sizeText}${sizeUnit} space for you"
        val spannableString = SpannableString(fullText)
        
        // 找到大小部分的位置
        val startIndex = fullText.indexOf(sizeText)
        val endIndex = startIndex + sizeText.length + sizeUnit.length
        
        // 设置绿色
        val greenColor = ContextCompat.getColor(this, android.R.color.holo_green_dark)
        spannableString.setSpan(
            ForegroundColorSpan(greenColor),
            startIndex,
            endIndex,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        
        binding.tvSavedText.text = spannableString
    }
    
    private fun formatSize(size: Long): Pair<String, String> {
        return when {
            size < 1024 -> Pair(size.toString(), "B")
            size < 1024 * 1024 -> Pair(String.format("%.0f", size / 1024.0), "KB")
            size < 1024 * 1024 * 1024 -> Pair(String.format("%.0f", size / (1024.0 * 1024)), "MB")
            else -> Pair(String.format("%.2f", size / (1024.0 * 1024 * 1024)), "GB")
        }
    }
}