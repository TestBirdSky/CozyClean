package com.mottled.quell.stern

/**
 * SaturPage Presenter - 处理设置页面的业务逻辑
 */
class SaturPagePresenter(private val view: SaturPageContract.View) : SaturPageContract.Presenter {
    
    companion object {
        private const val PRIVACY_POLICY_URL = "https://www.example.com/privacy-policy" // TODO 添加隐私政策链接
    }
    
    override fun onViewCreated() {
        // 初始化渐变文本
        view.setupGradientText()
    }
    
    override fun onBackClicked() {
        // 处理返回逻辑
        view.finishPage()
    }
    
    override fun onPrivacyPolicyClicked() {
        // 打开隐私政策页面
        view.openUrl(PRIVACY_POLICY_URL)
    }
    
    override fun onShareClicked() {
        // 构建分享内容
        val packageName = view.getPackageName()
        val appName = view.getAppName()
        val storeUrl = "https://play.google.com/store/apps/details?id=$packageName"
        val shareText = "Check out $appName! Download it now from the Play Store: $storeUrl"
        
        // 发起分享
        view.shareContent(shareText, appName)
    }
}
