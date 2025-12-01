package com.righteous.and.stern.jqlait.dmothi

import android.content.Context
import android.os.Environment
import com.righteous.and.stern.jqlait.dmothi.CategoryType
import com.righteous.and.stern.jqlait.dmothi.JunkCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class JunkFileScanner(private val context: Context) {

    private val scannedFiles = mutableSetOf<String>()

    interface ScanCallback {
        fun onScanProgress(path: String, totalSize: Long)
        fun onScanComplete(categories: List<JunkCategory>)
    }

    suspend fun startScan(callback: ScanCallback) = withContext(Dispatchers.IO) {
        scannedFiles.clear()
        
        val categories = mutableMapOf<CategoryType, JunkCategory>().apply {
            CategoryType.values().forEach { type ->
                put(type, JunkCategory(type))
            }
        }

        val directories = mutableListOf<File>()

        // 扫描内部存储
        context.cacheDir?.let { directories.add(it) }
        context.filesDir?.let { directories.add(it) }
        
        // 扫描外部存储
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            Environment.getExternalStorageDirectory()?.let { directories.add(it) }
            context.externalCacheDir?.let { directories.add(it) }
            context.getExternalFilesDir(null)?.let { directories.add(it) }
        }

        // 扫描所有目录
        directories.forEach { dir ->
            if (dir.exists() && dir.canRead()) {
                scanDirectory(dir, categories, callback)
            }
        }

        withContext(Dispatchers.Main) {
            // 返回所有分类，包括空的分类
            callback.onScanComplete(categories.values.toList())
        }
    }

    private suspend fun scanDirectory(
        directory: File,
        categories: MutableMap<CategoryType, JunkCategory>,
        callback: ScanCallback
    ) {
        try {
            val files = directory.listFiles() ?: return

            for (file in files) {
                try {
                    // 避免重复扫描
                    val canonicalPath = file.canonicalPath
                    if (scannedFiles.contains(canonicalPath)) {
                        continue
                    }
                    scannedFiles.add(canonicalPath)

                    withContext(Dispatchers.Main) {
                        callback.onScanProgress(
                            canonicalPath,
                            categories.values.sumOf { it.getTotalSize() }
                        )
                    }

                    if (file.isDirectory) {
                        // 递归扫描子目录
                        scanDirectory(file, categories, callback)
                    } else if (file.isFile) {
                        // 分类文件
                        classifyFile(file, categories)
                    }
                } catch (e: Exception) {
                    // 忽略无法访问的文件
                    continue
                }
            }
        } catch (e: Exception) {
            // 忽略无法访问的目录
        }
    }

    private fun classifyFile(file: File, categories: MutableMap<CategoryType, JunkCategory>) {
        val fileName = file.name.lowercase()
        val filePath = file.absolutePath.lowercase()

        val category = when {
            // App Cache
            filePath.contains("/cache") || fileName.endsWith(".cache") -> {
                categories[CategoryType.APP_CACHE]
            }
            // APK Files
            fileName.endsWith(".apk") -> {
                categories[CategoryType.APK_FILES]
            }
            // Log Files
            fileName.endsWith(".log") || fileName.endsWith(".txt") && filePath.contains("/log") -> {
                categories[CategoryType.LOG_FILES]
            }
            // AD Junk
            filePath.contains("/ad") || filePath.contains("/ads") || 
            filePath.contains("/advertisement") -> {
                categories[CategoryType.AD_JUNK]
            }
            // Temp Files
            fileName.endsWith(".tmp") || fileName.endsWith(".temp") || 
            filePath.contains("/temp") || filePath.contains("/tmp") -> {
                categories[CategoryType.TEMP_FILES]
            }
            else -> null
        }

        category?.let {
            it.files.add(
                JunkFile(
                    file = file,
                    name = file.name,
                    size = file.length(),
                    isSelected = true
                )
            )
        }
    }
}
