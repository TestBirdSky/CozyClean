package com.righteous.and.stern.jqlait.dmothi

import com.righteous.and.stern.R

data class JunkCategory(
    val type: CategoryType,
    val files: MutableList<JunkFile> = mutableListOf(),
    var isExpanded: Boolean = false
) {
    fun getTotalSize(): Long {
        return files.sumOf { it.size }
    }

    fun getSizeFormatted(): String {
        val totalSize = getTotalSize()
        return when {
            totalSize < 1024 -> "${totalSize}B"
            totalSize < 1024 * 1024 -> String.format("%.2fKB", totalSize / 1024.0)
            totalSize < 1024 * 1024 * 1024 -> String.format("%.2fMB", totalSize / (1024.0 * 1024))
            else -> String.format("%.2fGB", totalSize / (1024.0 * 1024 * 1024))
        }
    }

    fun getSelectedFiles(): List<JunkFile> {
        return files.filter { it.isSelected }
    }

    fun isAllSelected(): Boolean {
        return files.isNotEmpty() && files.all { it.isSelected }
    }

    fun selectAll(selected: Boolean) {
        files.forEach { it.isSelected = selected }
    }
}

enum class CategoryType(val title: String, val iconRes: Int) {
    APP_CACHE("App Cache", R.drawable.ic_wednes_app_cache),
    APK_FILES("Apk Files", R.drawable.ic_wednes_apk_files),
    LOG_FILES("Log Files", R.drawable.ic_wednes_log_files),
    AD_JUNK("AD Junk", R.drawable.ic_wednes_ad_junk),
    TEMP_FILES("Temp Files", R.drawable.ic_wednes_temp_files)
}
