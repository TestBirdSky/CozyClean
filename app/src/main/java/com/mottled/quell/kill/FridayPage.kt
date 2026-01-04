package com.mottled.quell.kill

import android.Manifest
import android.animation.ObjectAnimator
import android.app.ActivityManager
import android.app.usage.StorageStatsManager
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorManager
import android.net.Uri
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.os.StatFs
import android.os.storage.StorageManager
import android.provider.Settings
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mottled.quell.kill.WednesPage
import com.mottled.quell.kill.databinding.FridayPageBinding
import java.util.UUID
import kotlin.math.roundToInt

class FridayPage : AppCompatActivity() {
    private val binding by lazy { FridayPageBinding.inflate(layoutInflater) }
    
    companion object {
        private const val PERMISSION_REQUEST_CODE = 1001
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.friday)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // 只设置左右和底部的padding，让toolbar延伸到状态栏
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            
            // 给toolbar设置顶部padding以适应状态栏
            binding.toolbar.setPadding(
                binding.toolbar.paddingLeft,
                systemBars.top,
                binding.toolbar.paddingRight,
                binding.toolbar.paddingBottom
            )
            
            insets
        }
        
        setupLoadingDialog()
        setupViews()
        
        // 延迟1.5秒后隐藏加载对话框并显示内容
        Handler(Looper.getMainLooper()).postDelayed({
            hideLoadingDialog()
            loadDeviceInfo()
        }, 1500)
    }
    
    private fun setupLoadingDialog() {
        // 设置设备图标
        val imgLoad = binding.loadingDialog.root.findViewById<ImageView>(R.id.imgLoad)
        imgLoad.setImageResource(R.drawable.ic_tues_device)
        
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
    
    private fun setupViews() {
        // 返回按钮
        binding.imgBack.setOnClickListener {
            finish()
        }
        
        // Clean 按钮 - 检查权限后跳转
        binding.btnClean.setOnClickListener {
            if (checkStoragePermissions()) {
                // 权限已授予，直接跳转
                navigateToWednesPage()
            } else {
                // 权限未授予，显示对话框
                showPermissionDialog()
            }
        }
    }
    
    private fun loadDeviceInfo() {
        // 加载设备基本信息
        loadBasicInfo()
        
        // 加载状态信息
        loadStateInfo()
        
        // 加载屏幕信息
        loadScreenInfo()
        
        // 加载电池信息
        loadBatteryInfo()
        
        // 加载传感器信息
        loadSensorInfo()
    }
    
    private fun loadBasicInfo() {
        // 手机型号
        val model = "${Build.MANUFACTURER} ${Build.MODEL}"
        binding.tvPhoneModel.text = model
        
        // 系统版本
        val osVersion = "Android ${Build.VERSION.RELEASE}"
        binding.tvOsVersion.text = osVersion
    }
    
    private fun loadStateInfo() {
        // RAM 信息
        val activityManager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)
        
        val totalRam = memoryInfo.totalMem / (1024.0 * 1024 * 1024)
        val availableRam = memoryInfo.availMem / (1024.0 * 1024 * 1024)
        val usedRam = totalRam - availableRam
        val ramPercentage = ((usedRam / totalRam) * 100).roundToInt()
        
        binding.tvRamInfo.text = String.format("%.1f GB / %.1f GB RAM used", usedRam, totalRam)
        binding.progressRam.progress = ramPercentage
        
        // Storage 信息 - 使用与TuesPage相同的计算方式
        val statFs = StatFs(Environment.getDataDirectory().path)
        
        val blockSizeLong: Long
        val totalBlocksLong: Long
        val availableBlocksLong: Long
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSizeLong = statFs.blockSizeLong
            totalBlocksLong = statFs.blockCountLong
            availableBlocksLong = statFs.availableBlocksLong
        } else {
            blockSizeLong = statFs.blockSize.toLong()
            totalBlocksLong = statFs.blockCount.toLong()
            availableBlocksLong = statFs.availableBlocks.toLong()
        }
        
        val totalSpace = totalBlocksLong * blockSizeLong
        val availableSpace = availableBlocksLong * blockSizeLong
        val usedSpace = totalSpace - availableSpace
        
        // Convert to GB using the same divisor as TuesPage
        val totalGB = totalSpace / (1000.0 * 1000.0 * 1000.0)
        val usedGB = usedSpace / (1000.0 * 1000.0 * 1000.0)
        
        setStorageInfoText(usedGB, totalGB)
        
        val storagePercentage = ((usedSpace.toFloat() / totalSpace.toFloat()) * 100f).roundToInt()
        binding.progressStorage.progress = storagePercentage
        
        // Get app-specific storage info if available
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getAppStorageInfo()
        }
    }
    
    private fun loadScreenInfo() {
        val displayMetrics = DisplayMetrics()
        val windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        
        // 屏幕分辨率
        val width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels
        binding.tvScreenResolution.text = "$width ×$height"
        
        // 屏幕密度
        val density = displayMetrics.densityDpi
        binding.tvScreenDensity.text = "$density DPI"
        
        // 多点触控支持
        binding.tvMultitouch.text = "Supported"
    }
    
    private fun loadBatteryInfo() {
        val batteryStatus: Intent? = registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        
        if (batteryStatus != null) {
            // 电池电量
            val level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            val batteryPct = (level / scale.toFloat() * 100).roundToInt()
            binding.tvBatteryLevel.text = "$batteryPct%"
            
            // 电池健康状态
            val health = batteryStatus.getIntExtra(BatteryManager.EXTRA_HEALTH, -1)
            val healthStatus = when (health) {
                BatteryManager.BATTERY_HEALTH_GOOD -> "Good"
                BatteryManager.BATTERY_HEALTH_OVERHEAT -> "Overheat"
                BatteryManager.BATTERY_HEALTH_DEAD -> "Dead"
                BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> "Over Voltage"
                BatteryManager.BATTERY_HEALTH_COLD -> "Cold"
                else -> "Unknown"
            }
            binding.tvBatteryHealth.text = healthStatus
            
            // 电池温度
            val temperature = batteryStatus.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1)
            val tempCelsius = temperature / 10.0
            binding.tvBatteryTemperature.text = String.format("%.0f°C", tempCelsius)
            
            // 充电状态
            val status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
            val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL
            binding.tvChargingStatus.text = if (isCharging) "Charging" else "Not Charging"
        }
        
        // 电池容量（这个通常是固定值，可以从设备规格中获取）
        binding.tvBatteryCapacity.text = "5000 mAh"
    }
    
    private fun loadSensorInfo() {
        val sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        
        // 加速度传感器
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        binding.tvAccelerometer.text = if (accelerometer != null) "Supported" else "Not Supported"
        
        // 陀螺仪传感器
        val gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        binding.tvGyroscope.text = if (gyroscope != null) "Supported" else "Not Supported"
        
        // 距离传感器
        val proximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
        binding.tvProximity.text = if (proximity != null) "Supported" else "Not Supported"
        
        // 光线传感器
        val light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        binding.tvLightSensor.text = if (light != null) "Supported" else "Not Supported"
    }
    
    private fun showPermissionDialog() {
        // 显示权限对话框
        val permissionDialog = findViewById<View>(R.id.permission_dialog)
        permissionDialog.visibility = View.VISIBLE
        
        // 设置按钮点击事件
        val cancelButton = permissionDialog.findViewById<Button>(R.id.cancel_button)
        val yesButton = permissionDialog.findViewById<Button>(R.id.yes_button)
        
        cancelButton.setOnClickListener {
            hidePermissionDialog()
        }
        
        yesButton.setOnClickListener {
            hidePermissionDialog()
            requestStoragePermissions()
        }
    }
    
    private fun hidePermissionDialog() {
        // 隐藏权限对话框
        val permissionDialog = findViewById<View>(R.id.permission_dialog)
        permissionDialog.visibility = View.GONE
    }
    
    private fun checkStoragePermissions(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Android 11及以上，检查MANAGE_EXTERNAL_STORAGE权限
            Environment.isExternalStorageManager()
        } else {
            // Android 10及以下，检查READ和WRITE权限
            val readPermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
            
            val writePermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
            
            readPermission && writePermission
        }
    }
    
    private fun requestStoragePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Android 11及以上，申请MANAGE_EXTERNAL_STORAGE权限
            if (!Environment.isExternalStorageManager()) {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.data = Uri.parse("package:$packageName")
                startActivityForResult(intent, PERMISSION_REQUEST_CODE)
            } else {
                // 权限已授予
                navigateToWednesPage()
            }
        } else {
            // Android 10及以下，申请READ和WRITE权限
            val permissions = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            
            if (ContextCompat.checkSelfPermission(this, permissions[0]) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, permissions[1]) != PackageManager.PERMISSION_GRANTED) {
                
                ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE)
            } else {
                // 权限已授予
                navigateToWednesPage()
            }
        }
    }
    
    private fun getAppStorageInfo() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                val storageStatsManager = getSystemService(STORAGE_STATS_SERVICE) as StorageStatsManager
                val storageManager = getSystemService(STORAGE_SERVICE) as StorageManager
                
                val storageVolumes = storageManager.storageVolumes
                for (volume in storageVolumes) {
                    if (volume.isPrimary) {
                        val uuidStr = volume.uuid
                        val uuid = if (uuidStr == null) {
                            StorageManager.UUID_DEFAULT
                        } else {
                            UUID.fromString(uuidStr)
                        }
                        
                        val totalBytes = storageStatsManager.getTotalBytes(uuid)
                        val freeBytes = storageStatsManager.getFreeBytes(uuid)
                        val usedBytes = totalBytes - freeBytes
                        
                        val totalGB = totalBytes / (1000.0 * 1000.0 * 1000.0)
                        val usedGB = usedBytes / (1000.0 * 1000.0 * 1000.0)
                        
                        // Update UI with more accurate data
                        setStorageInfoText(usedGB, totalGB)
                        
                        val progressPercentage = ((usedBytes.toFloat() / totalBytes.toFloat()) * 100f).roundToInt()
                        binding.progressStorage.progress = progressPercentage
                        
                        break
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    private fun setStorageInfoText(usedGB: Double, totalGB: Double) {
        val text = String.format("Storage: %.1f GB / %.1f GB", usedGB, totalGB)
        val spannableString = SpannableString(text)
        
        // 只查找已用存储的数字部分并设置颜色
        val usedGBStr = String.format("%.1f GB", usedGB)
        
        val usedStart = text.indexOf(usedGBStr)
        val usedEnd = usedStart + usedGBStr.length
        
        // 只设置已用存储为红色，总存储保持原来的颜色
        val redColor = Color.parseColor("#F2463B")
        
        if (usedStart >= 0) {
            spannableString.setSpan(
                ForegroundColorSpan(redColor),
                usedStart,
                usedEnd,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        
        binding.tvStorageInfo.text = spannableString
    }
    
    private fun navigateToWednesPage() {
        val intent = Intent(this, WednesPage::class.java)
        startActivity(intent)
    }
    
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                // Android 11+，检查是否有所有文件访问权限
                if (Environment.isExternalStorageManager()) {
                    navigateToWednesPage()
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Android 10及以下，检查是否所有权限都被授予
                if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                    navigateToWednesPage()
                } else {
                    Toast.makeText(this, "Permissions denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        
        if (requestCode == PERMISSION_REQUEST_CODE) {
            // 检查权限是否被授予
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    navigateToWednesPage()
                } else {
                    Toast.makeText(this, "Storage permission is required to continue", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}