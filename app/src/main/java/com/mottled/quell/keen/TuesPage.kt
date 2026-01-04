package com.mottled.quell.keen

import android.Manifest
import android.app.usage.StorageStatsManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StatFs
import android.os.storage.StorageManager
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mottled.quell.kill.WednesPage
import com.mottled.quell.kill.FridayPage
import com.mottled.quell.kill.R
import com.mottled.quell.kill.databinding.TuesPageBinding
import java.util.UUID
import kotlin.getValue

class TuesPage : AppCompatActivity() {
    val binding by lazy { TuesPageBinding.inflate(layoutInflater) }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.tues)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        // Initialize UI
        setupUI()
        
        // Load storage information
        loadStorageInfo()
        
        // Setup click listeners
        setupClickListeners()
    }
    
    private fun setupUI() {
        // Set app name
        binding.appNameText.text = getString(R.string.app_name)
    }
    
    private fun loadStorageInfo() {
        try {
            // Get internal storage stats
            val stat = StatFs(Environment.getDataDirectory().path)
            
            val blockSizeLong: Long
            val totalBlocksLong: Long
            val availableBlocksLong: Long
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                blockSizeLong = stat.blockSizeLong
                totalBlocksLong = stat.blockCountLong
                availableBlocksLong = stat.availableBlocksLong
            } else {
                blockSizeLong = stat.blockSize.toLong()
                totalBlocksLong = stat.blockCount.toLong()
                availableBlocksLong = stat.availableBlocks.toLong()
            }
            
            val totalSpace = totalBlocksLong * blockSizeLong
            val availableSpace = availableBlocksLong * blockSizeLong
            val usedSpace = totalSpace - availableSpace
            
            // Convert to GB
            val totalGB = totalSpace / (1000.0 * 1000.0 * 1000.0)
            val usedGB = usedSpace / (1000.0 * 1000.0 * 1000.0)
            
            // Update UI
            binding.storageInfo.text = String.format("%.1f GB / %.1f GB", usedGB, totalGB)
            
            // Update progress
            val progressPercentage = ((usedSpace.toFloat() / totalSpace.toFloat()) * 100f)
            binding.circularProgress.setMaxProgress(100f)
            binding.circularProgress.setProgress(progressPercentage)
            
            // Get app-specific storage info if available
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                getAppStorageInfo()
            }
            
        } catch (e: Exception) {
            e.printStackTrace()
            binding.storageInfo.text = "Unable to get storage info"
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
                        binding.storageInfo.text = String.format("%.1f GB / %.1f GB", usedGB, totalGB)
                        
                        val progressPercentage = ((usedBytes.toFloat() / totalBytes.toFloat()) * 100f)
                        binding.circularProgress.setProgress(progressPercentage)
                        
                        break
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    companion object {
        private const val PERMISSION_REQUEST_CODE = 1001
    }

    private fun setupClickListeners() {
        // Circular progress click - check permission first
        binding.circularProgress.setOnClickListener {
            if (checkStoragePermissions()) {
                // Permission already granted, navigate directly
                navigateToWednesPage()
            } else {
                // Permission not granted, show dialog
                showPermissionDialog()
            }
        }
        
        // Settings icon click
        binding.settingsIcon.setOnClickListener {
            // Open app settings
            startActivity(Intent(this, SaturPage::class.java))
        }
        
        // Device card click
        binding.deviceCard.setOnClickListener {
            // Show device information
            showDeviceInfo()
        }
        
        // CPU card click
        binding.cpuCard.setOnClickListener {
            // Show CPU information
            showCPUInfo()
        }
    }
    
    private fun showDeviceInfo() {
        startActivity(Intent(this, FridayPage::class.java))
    }
    
    private fun showCPUInfo() {
        startActivity(Intent(this, SaijIag::class.java))
    }
    
    private fun showPermissionDialog() {
        // Show the included dialog by changing visibility
        val permissionDialog = findViewById<View>(R.id.permission_dialog)
        permissionDialog.visibility = View.VISIBLE
        
        // Setup buttons
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
        // Hide the included dialog
        val permissionDialog = findViewById<View>(R.id.permission_dialog)
        permissionDialog.visibility = View.GONE
    }
    
    private fun checkStoragePermissions(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // For Android 11 and above, check MANAGE_EXTERNAL_STORAGE
            Environment.isExternalStorageManager()
        } else {
            // For Android 10 and below, check READ and WRITE permissions
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
            // For Android 11 and above, request MANAGE_EXTERNAL_STORAGE
            if (!Environment.isExternalStorageManager()) {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.data = Uri.parse("package:$packageName")
                startActivityForResult(intent, PERMISSION_REQUEST_CODE)
            } else {
                // Permission already granted
                navigateToWednesPage()
            }
        } else {
            // For Android 10 and below, request READ and WRITE permissions
            val permissions = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            
            if (ContextCompat.checkSelfPermission(this, permissions[0]) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, permissions[1]) != PackageManager.PERMISSION_GRANTED) {
                
                ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE)
            } else {
                // Permissions already granted
                navigateToWednesPage()
            }
        }
    }
    
    private fun navigateToWednesPage() {
        val intent = Intent(this, WednesPage::class.java)
        startActivity(intent)
    }
    
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                // For Android 11+, check if we have all files access
                if (Environment.isExternalStorageManager()) {
                    navigateToWednesPage()
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            } else {
                // For Android 10 and below, check if both permissions are granted
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
            // Check if the permission was granted
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