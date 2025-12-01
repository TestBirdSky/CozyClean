package com.righteous.and.stern

/**
 * SaturPage MVP 契约接口
 */
interface SaturPageContract {
    
    /**
     * View 接口 - 负责 UI 显示和交互
     */
    interface View {
        fun setupGradientText()
        fun finishPage()
        fun openUrl(url: String)
        fun shareContent(shareText: String, title: String)
        fun getPackageName(): String
        fun getAppName(): String
    }
    
    /**
     * Presenter 接口 - 负责业务逻辑
     */
    interface Presenter {
        fun onViewCreated()
        fun onBackClicked()
        fun onPrivacyPolicyClicked()
        fun onShareClicked()
    }
}
