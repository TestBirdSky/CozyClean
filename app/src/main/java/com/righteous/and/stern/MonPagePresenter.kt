package com.righteous.and.stern

import android.os.Handler
import android.os.Looper

/**
 * MonPage Presenter - 处理进度条逻辑
 */
class MonPagePresenter(private val view: MonPageContract.View) : MonPageContract.Presenter {
    
    private val handler = Handler(Looper.getMainLooper())
    private var currentProgress = 0
    
    companion object {
        private const val MAX_PROGRESS = 100
        private const val TOTAL_DURATION = 1500L // 1.5秒
        private const val UPDATE_INTERVAL = 10L // 10毫秒
    }
    
    private val updateRunnable = object : Runnable {
        override fun run() {
            // 计算进度增量
            val progressIncrement = (MAX_PROGRESS * UPDATE_INTERVAL.toFloat() / TOTAL_DURATION).toInt()
            currentProgress += maxOf(1, progressIncrement)
            
            if (currentProgress >= MAX_PROGRESS) {
                // 进度完成，跳转到下一页
                currentProgress = MAX_PROGRESS
                view.updateProgress(currentProgress)
                view.navigateToNextPage()
            } else {
                // 更新进度并继续
                view.updateProgress(currentProgress)
                handler.postDelayed(this, UPDATE_INTERVAL)
            }
        }
    }
    
    override fun onViewCreated() {
        // 初始化渐变文本
        view.setupGradientText("Clean It")
        // 开始进度条动画
        currentProgress = 0
        view.updateProgress(currentProgress)
        handler.postDelayed(updateRunnable, UPDATE_INTERVAL)
    }
    
    override fun onViewDestroyed() {
        // 清理资源
        handler.removeCallbacks(updateRunnable)
    }
}
