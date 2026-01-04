package com.mottled.quell.keen

import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.mottled.quell.stern.SaturPageContract
import com.mottled.quell.stern.SaturPagePresenter
import com.mottled.quell.kill.R
import com.mottled.quell.kill.databinding.SaturPageBinding

/**
 * SaturPage - 设置页面，实现 MVP 模式的 View 层
 * 职责：仅负责 UI 显示和用户交互
 */
class SaturPage : AppCompatActivity(), SaturPageContract.View {

    private val binding by lazy { SaturPageBinding.inflate(layoutInflater) }
    private lateinit var presenter: SaturPageContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        setupViews()
        setupPresenter()
        setupClickListeners()

        presenter.onViewCreated()
    }

    /**
     * 初始化视图
     */
    private fun setupViews() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.satur)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    /**
     * 初始化 Presenter
     */
    private fun setupPresenter() {
        presenter = SaturPagePresenter(this)
    }

    /**
     * 设置点击监听器
     */
    private fun setupClickListeners() {
        binding.backButton.setOnClickListener {
            presenter.onBackClicked()
        }

        binding.privacyCard.setOnClickListener {
            presenter.onPrivacyPolicyClicked()
        }

        binding.shareCard.setOnClickListener {
            presenter.onShareClicked()
        }

        binding.tvLogin.setOnClickListener {
            if (isLoginM) {
                signOut()
            } else {
                showLogin()
            }
        }
    }

    // ========== 实现 View 接口方法 ==========

    override fun setupGradientText() {
        binding.appNameText.post {
            val width = binding.appNameText.width.toFloat()
            val textShader = LinearGradient(
                0f, 0f, width, 0f, intArrayOf(
                    Color.parseColor("#FA9200"),
                    Color.parseColor("#945600")
                ), null, Shader.TileMode.CLAMP
            )
            binding.appNameText.paint.shader = textShader
            binding.appNameText.invalidate()
        }
    }

    override fun finishPage() {
        finish()
    }

    override fun openUrl(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun shareContent(shareText: String, title: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, title)
            putExtra(Intent.EXTRA_TEXT, shareText)
        }

        try {
            startActivity(Intent.createChooser(shareIntent, "Share $title"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getPackageName(): String {
        return super.getPackageName()
    }

    override fun getAppName(): String {
        return getString(R.string.app_name)
    }

    override fun onStart() {
        super.onStart()
        cheLoginS()
    }

    private fun cheLoginS() {
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null) {
            refAccountInfo(account)
            showUi(true)
        } else {
            showUi(false)
        }
    }

    private fun showLogin() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN)
    }

    private val RC_GOOGLE_SIGN_IN = 19988

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleGoogleSignInResult(task)
        } else {
            Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleGoogleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            val account = GoogleSignIn.getLastSignedInAccount(this)
            if (account != null) {
                Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()
                refAccountInfo(account)
                showUi(true)
            } else {
                Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
            }
        } catch (e: ApiException) {
            Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
        }
    }

    private var isLoginM = false

    private fun showUi(isLogin: Boolean) {
        isLoginM = isLogin
        binding.tvLogin.setText(if (isLogin) "Logout" else "Login")
        if (isLogin) {
            binding.tvDes.visibility = View.VISIBLE
            binding.appNameText.visibility = View.VISIBLE
            binding.appLogo.visibility = View.VISIBLE
        } else {
            binding.tvDes.visibility = View.GONE
            binding.appNameText.visibility = View.GONE
            binding.appLogo.visibility = View.GONE
        }
    }

    private fun refAccountInfo(account: GoogleSignInAccount) {
        binding.appNameText.text = account.displayName
        binding.tvDes.text = account.email
        Glide.with(this).load(account.photoUrl).transform(CircleCrop()).into(binding.appLogo)
    }

    private fun signOut() {
        try {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
            val googleSignInClient = GoogleSignIn.getClient(this, gso)
            // Google 登出
            googleSignInClient.signOut().addOnCompleteListener {
                showUi(false)
            }
        } catch (e: Exception) {
//            Log.e("zzzzzz", "Google sign out error: ${e.message}")
        }
    }
}