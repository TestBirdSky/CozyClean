package com.righteous.and.stern.jqlait.dmothi

import java.io.File

data class JunkFile(
    val file: File,
    val name: String,
    val size: Long,
    var isSelected: Boolean = true
) {
    fun getSizeFormatted(): String {
        return when {
            size < 1024 -> "${size}B"
            size < 1024 * 1024 -> String.format("%.2fKB", size / 1024.0)
            size < 1024 * 1024 * 1024 -> String.format("%.2fMB", size / (1024.0 * 1024))
            else -> String.format("%.2fGB", size / (1024.0 * 1024 * 1024))
        }
    }
}
