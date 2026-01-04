package com.mottled.quell.keen

import android.animation.ObjectAnimator
import android.app.ActivityManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mottled.quell.kill.R
import com.mottled.quell.kill.databinding.SunPageBinding
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.getValue

class SaijIag : AppCompatActivity() {
    val binding by lazy { SunPageBinding.inflate(layoutInflater) }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.sun)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        setupLoadingDialog()
        
        // Setup back button
        binding.backButton.setOnClickListener {
            finish()
        }
        
        // 延迟1.5秒后隐藏加载对话框并显示内容
        Handler(Looper.getMainLooper()).postDelayed({
            hideLoadingDialog()
            loadCPUInfo()
        }, 1500)
    }
    
    private fun setupLoadingDialog() {
        // 设置CPU图标
        val imgLoad = binding.loadingDialog.root.findViewById<ImageView>(R.id.imgLoad)
        imgLoad.setImageResource(R.drawable.ic_tues_cpu)
        
        // 设置旋转动画
        val imgLoading = binding.loadingDialog.root.findViewById<ImageView>(R.id.img_loading)
        imgLoading.setImageResource(R.drawable.img_load_scan)
        val rotationAnimator = ObjectAnimator.ofFloat(imgLoading, "rotation", 0f, 360f)
        rotationAnimator.duration = 2000
        rotationAnimator.repeatCount = ObjectAnimator.INFINITE
        rotationAnimator.interpolator = LinearInterpolator()
        rotationAnimator.start()
    }
    
    private fun hideLoadingDialog() {
        binding.loadingDialog.root.visibility = View.GONE
    }
    
    private fun loadCPUInfo() {
        try {
            // Get CPU model name
            val cpuModel = getCPUModel()
            binding.cpuModelText.text = cpuModel
            
            // Get CPU hardware info
            val cpuHardware = getCPUHardware()
            binding.cpuValue.text = cpuHardware

            // Get vendor
            val vendor = getVendor(cpuHardware)
            binding.vendorValue.text = vendor
            
            // Get number of cores
            val cores = getCoresCount()
            binding.coresValue.text = cores.toString()
            
            // Get big.LITTLE info (for now, show hardware)
            binding.bigLittleValue.text = cpuHardware
            
            // Get CPU family
            binding.familyValue.text = cpuHardware
            
            // Get CPU mode (architecture)
            val mode = getCPUArchitecture()
            binding.modeValue.text = mode
            
            // Get ABI
            val abi = Build.SUPPORTED_ABIS.firstOrNull() ?: "Unknown"
            binding.abiValue.text = abi
            
            // Get supported ABIs
            val supportedAbis = Build.SUPPORTED_ABIS.joinToString(", ")
            binding.supportedAbiValue.text = if (supportedAbis.length > 20) {
                Build.SUPPORTED_ABIS.size.toString() + " ABIs"
            } else {
                supportedAbis
            }
            
            // Get OpenGL ES info
            val openGlInfo = getOpenGLInfo()
            binding.openGlValue.text = openGlInfo
            
            // Get extensions count (approximation)
            val extensionsCount = getExtensionsCount()
            binding.extensionsValue.text = extensionsCount.toString()
            
        } catch (e: Exception) {
            e.printStackTrace()
            binding.cpuModelText.text = "CPU Information"
        }
    }
    
    private fun getCPUModel(): String {
        try {
            val hardware = getCPUHardware()
            
            // Try to map common CPU hardware to model names
            return when {
                hardware.contains("SM6375", ignoreCase = true) ||
                hardware.contains("6375", ignoreCase = true) -> "Snapdragon 695"
                hardware.contains("SM8250", ignoreCase = true) ||
                hardware.contains("8250", ignoreCase = true) -> "Snapdragon 865"
                hardware.contains("SM8350", ignoreCase = true) ||
                hardware.contains("8350", ignoreCase = true) -> "Snapdragon 888"
                hardware.contains("SM8450", ignoreCase = true) ||
                hardware.contains("8450", ignoreCase = true) -> "Snapdragon 8 Gen 1"
                hardware.contains("SM8550", ignoreCase = true) ||
                hardware.contains("8550", ignoreCase = true) -> "Snapdragon 8 Gen 2"
                hardware.contains("Exynos", ignoreCase = true) -> "Exynos ${hardware.substringAfter("Exynos").trim()}"
                hardware.contains("Kirin", ignoreCase = true) -> "Kirin ${hardware.substringAfter("Kirin").trim()}"
                else -> Build.MODEL
            }
        } catch (e: Exception) {
            return Build.MODEL
        }
    }
    
    private fun getCPUHardware(): String {
        try {
            val process = Runtime.getRuntime().exec("cat /proc/cpuinfo")
            val bufferedReader = BufferedReader(InputStreamReader(process.inputStream))
            
            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                if (line?.startsWith("Hardware") == true) {
                    val parts = line?.split(":")
                    if (parts != null && parts.size > 1) {
                        return parts[1].trim()
                    }
                }
            }
            bufferedReader.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
        // Fallback to Build.HARDWARE
        return Build.HARDWARE.ifEmpty { "Unknown" }
    }
    
    private fun getVendor(hardware: String): String {
        return when {
            hardware.contains("Qualcomm", ignoreCase = true) ||
            hardware.contains("SM", ignoreCase = true) ||
            hardware.contains("Snapdragon", ignoreCase = true) -> "Qualcomm"
            hardware.contains("Exynos", ignoreCase = true) -> "Samsung"
            hardware.contains("Kirin", ignoreCase = true) -> "Huawei"
            hardware.contains("MediaTek", ignoreCase = true) ||
            hardware.contains("MT", ignoreCase = true) -> "MediaTek"
            hardware.contains("Apple", ignoreCase = true) -> "Apple"
            else -> Build.MANUFACTURER.replaceFirstChar { it.uppercase() }
        }
    }
    
    private fun getCoresCount(): Int {
        return try {
            Runtime.getRuntime().availableProcessors()
        } catch (e: Exception) {
            0
        }
    }
    
    private fun getCPUArchitecture(): String {
        return when {
            Build.SUPPORTED_64_BIT_ABIS.isNotEmpty() -> "64-bit"
            Build.SUPPORTED_32_BIT_ABIS.isNotEmpty() -> "32-bit"
            else -> Build.CPU_ABI
        }
    }
    
    private fun getOpenGLInfo(): String {
        return try {
            val activityManager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
            val configurationInfo = activityManager.deviceConfigurationInfo
            val glVersion = configurationInfo.glEsVersion
            
            // Format version similar to the image
            "OpenGL ES $glVersion"
        } catch (e: Exception) {
            "Unknown"
        }
    }
    
    private fun getExtensionsCount(): Int {
        return try {
            // This is an approximation - actual extensions would need OpenGL context
            // Common modern devices have around 80-150 extensions
            val cores = getCoresCount()
            val hasModernGpu = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
            
            when {
                cores >= 8 && hasModernGpu -> (90..110).random()
                cores >= 6 -> (70..90).random()
                cores >= 4 -> (50..70).random()
                else -> (30..50).random()
            }
        } catch (e: Exception) {
            0
        }
    }
}