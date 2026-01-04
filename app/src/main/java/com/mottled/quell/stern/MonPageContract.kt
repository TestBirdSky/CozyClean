package com.mottled.quell.stern

/**
 * MVP 契约接口，定义 View 和 Presenter 的职责
 */
interface MonPageContract {
    
    /**
     * View 接口 - 负责 UI 显示
     */
    interface View {
        fun updateProgress(progress: Int)
        fun navigateToNextPage()
        fun setupGradientText(text: String)
    }
    
    /**
     * Presenter 接口 - 负责业务逻辑
     */
    interface Presenter {
        fun onViewCreated()
        fun onViewDestroyed()
    }
}
