package com.mottled.quell.kill

import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.mottled.quell.stern.MonPageContract
import com.mottled.quell.stern.MonPagePresenter
import com.mottled.quell.stern.StatusTools
import com.mottled.quell.keen.TuesPage

/**
 * MonPage - 启动页面，实现 MVP 模式的 View 层
 * 职责：仅负责 UI 显示和用户交互
 */
class MonPage : AppCompatActivity(), MonPageContract.View {

    private lateinit var progressBar: ProgressBar
    private lateinit var appNameTextView: TextView
    private lateinit var tvSkip: TextView
    private lateinit var tvLogin: TextView
    private lateinit var loginLayout: View
    private lateinit var presenter: MonPageContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.mon_page)

        setupViews()
        setupPresenter()
        disableBackButton()

        presenter.onViewCreated()

    }

    /**
     * 初始化视图
     */
    private fun setupViews() {
        // 设置窗口边距
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mon)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 初始化视图组件
        progressBar = findViewById(R.id.progress_bar)
        appNameTextView = findViewById(R.id.app_name)
        tvSkip = findViewById(R.id.tv_skip)
        tvLogin = findViewById(R.id.tv_login)
        loginLayout = findViewById(R.id.login_layout)
        tvSkip.setOnClickListener {
            StatusTools.isJumpSkip = true
            navigateToNextPage()
        }
        tvLogin.setOnClickListener {
            s1()
        }
    }

    /**
     * 初始化 Presenter
     */
    private fun setupPresenter() {
        presenter = MonPagePresenter(this)
    }

    /**
     * 禁用返回按钮
     */
    private fun disableBackButton() {
        onBackPressedDispatcher.addCallback(this) {
            // 启动页禁止返回
        }
    }

    // ========== 实现 View 接口方法 ==========

    override fun updateProgress(progress: Int) {
        progressBar.progress = progress
    }

    override fun navigateToNextPage() {
        if (isLogin.not() && StatusTools.isJumpSkip.not()) {
            progressBar.visibility = View.GONE
            loginLayout.visibility = View.VISIBLE
        } else {
            StatusTools.isJumpSkip = true
            startActivity(Intent(this, TuesPage::class.java))
            finish()
        }
    }

    override fun setupGradientText(text: String) {
        appNameTextView.setTextColor(Color.BLACK)
        val paint = appNameTextView.paint
        paint.isAntiAlias = true
        val width = paint.measureText(text)
        val textShader = LinearGradient(
            0f,
            0f,
            width,
            0f,
            intArrayOf(0xFFFA9200.toInt(), 0xFF945600.toInt()),
            null,
            Shader.TileMode.CLAMP
        )
        appNameTextView.paint.shader = textShader
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onViewDestroyed()
    }

    override fun onStart() {
        super.onStart()
        ca1()
    }

    private var isLogin = false

    private fun ca1() {
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null) {
            isLogin = true
        }
    }

    private fun s1() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN)
    }

    private val RC_GOOGLE_SIGN_IN = 19988

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("Log-->", "onActivityResult: $requestCode")
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            h1(task)
        } else {
            Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun h1(task: Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult(ApiException::class.java)
            if (account != null) {
                isLogin = true
                Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()
                navigateToNextPage()
            } else {
                Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
            }
        } catch (e: ApiException) {
            e.printStackTrace()
            Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
        }
    }
}