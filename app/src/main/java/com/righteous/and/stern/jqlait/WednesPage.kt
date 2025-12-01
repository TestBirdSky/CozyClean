package com.righteous.and.stern.jqlait

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.righteous.and.stern.R
import com.righteous.and.stern.ThursPage
import com.righteous.and.stern.databinding.WednesPageBinding
import com.righteous.and.stern.jqlait.aq.CategoryAdapter
import com.righteous.and.stern.jqlait.dmothi.JunkCategory
import com.righteous.and.stern.jqlait.dmothi.JunkFileScanner
import kotlinx.coroutines.launch

class WednesPage : AppCompatActivity() {
    
    private val binding by lazy { WednesPageBinding.inflate(layoutInflater) }
    private lateinit var scanner: JunkFileScanner
    private val categories = mutableListOf<JunkCategory>()
    private var categoryAdapter: CategoryAdapter? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.wednes)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        setupViews()
        startScanning()
    }
    
    private fun setupViews() {
        // 设置返回按钮
        binding.imgBack.setOnClickListener {
            finish()
        }
        
        // 设置 RecyclerView
        binding.rvCategories.layoutManager = LinearLayoutManager(this)
        
        // 设置清理按钮
        binding.btnCleanNow.setOnClickListener {
            cleanSelectedFiles()
        }
    }
    
    private fun startScanning() {
        scanner = JunkFileScanner(this)
        
        // 显示进度条
        binding.progressScaning.visibility = View.VISIBLE
        
        lifecycleScope.launch {
            scanner.startScan(object : JunkFileScanner.ScanCallback {
                override fun onScanProgress(path: String, totalSize: Long) {
                    updateScanProgress(path, totalSize)
                }
                
                override fun onScanComplete(categories: List<JunkCategory>) {
                    onScanFinished(categories)
                }
            })
        }
    }
    
    private fun updateScanProgress(path: String, totalSize: Long) {
        // 更新扫描路径
        binding.tvScanningPath.text = path
        
        // 更新扫描大小
        val sizeInfo = formatSize(totalSize)
        binding.tvScannedSize.text = sizeInfo.first
        binding.tvScannedSizeUn.text = sizeInfo.second
    }
    
    private fun onScanFinished(scannedCategories: List<JunkCategory>) {
        // 隐藏进度条
        binding.progressScaning.visibility = View.GONE
        
        // 更新分类列表
        categories.clear()
        categories.addAll(scannedCategories)
        
        // 如果扫描到垃圾文件，更改背景
        if (categories.any { it.files.isNotEmpty() }) {
            binding.imgScanBg.setImageResource(R.drawable.bg_junk)
        }
        
        // 设置适配器
        categoryAdapter = CategoryAdapter(categories)
        binding.rvCategories.adapter = categoryAdapter
        
        // 更新最终扫描大小
        val totalSize = categories.sumOf { it.getTotalSize() }
        val sizeInfo = formatSize(totalSize)
        binding.tvScannedSize.text = sizeInfo.first
        binding.tvScannedSizeUn.text = sizeInfo.second
        binding.tvScanningPath.text = "Scan completed"
        
        // 显示清理按钮
        if (totalSize > 0) {
            binding.btnCleanNow.visibility = View.VISIBLE
            binding.btnCleanNow.isEnabled = true
        }
    }
    
    private fun cleanSelectedFiles() {
        lifecycleScope.launch {
            var deletedSize = 0L
            val deletedTypes = mutableSetOf<String>()
            
            // 遍历所有分类，删除选中的文件
            categories.forEach { category ->
                val selectedFiles = category.getSelectedFiles()
                selectedFiles.forEach { junkFile ->
                    try {
                        if (junkFile.file.exists() && junkFile.file.delete()) {
                            deletedSize += junkFile.size
                            deletedTypes.add(category.type.title)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            
            // 跳转到 ThursPage 并传递结果
            val intent = Intent(this@WednesPage, ThursPage::class.java).apply {
                putExtra("deleted_size", deletedSize)
                putExtra("delete_type", deletedTypes.joinToString(", "))
            }
            startActivity(intent)
            finish()
        }
    }
    
    private fun formatSize(size: Long): Pair<String, String> {
        return when {
            size < 1024 -> Pair(size.toString(), "B")
            size < 1024 * 1024 -> Pair(String.format("%.2f", size / 1024.0), "KB")
            size < 1024 * 1024 * 1024 -> Pair(String.format("%.2f", size / (1024.0 * 1024)), "MB")
            else -> Pair(String.format("%.2f", size / (1024.0 * 1024 * 1024)), "GB")
        }
    }
}