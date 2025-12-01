package com.righteous.and.stern

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt

class CircularProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    
    private var progress = 0f
    private var maxProgress = 100f
    private val strokeWidth = 50f
    private val rect = RectF()
    private var centerText = "CLEAN"
    
    init {
        // Setup background arc paint
        backgroundPaint.apply {
            style = Paint.Style.STROKE
            strokeWidth = this@CircularProgressView.strokeWidth
            strokeCap = Paint.Cap.ROUND
            color = Color.parseColor("#00000000")
        }
        
        // Setup progress arc paint with gradient
        progressPaint.apply {
            style = Paint.Style.STROKE
            strokeWidth = this@CircularProgressView.strokeWidth
            strokeCap = Paint.Cap.ROUND
        }
        
        // Setup text paint
        textPaint.apply {
            color = Color.WHITE
            textSize = 60f
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        
        // Setup inner circle paint
        circlePaint.apply {
            style = Paint.Style.FILL
            color = "#FF9500".toColorInt()
        }
    }
    
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        
        val centerX = w / 2f
        val centerY = h / 2f
        val radius = Math.min(w, h) / 2f - strokeWidth
        
        rect.set(
            centerX - radius,
            centerY - radius,
            centerX + radius,
            centerY + radius
        )
        
        // Create gradient for progress arc
        val gradient = SweepGradient(
            centerX,
            centerY,
            intArrayOf(
                Color.parseColor("#FFFA9200"),
                Color.parseColor("#FFFDF7EE"),
                Color.parseColor("#99FA9200")
            ),
            floatArrayOf(0f, 0.5f, 1f)
        )
        
        val matrix = Matrix()
        matrix.setRotate(-90f, centerX, centerY)
        gradient.setLocalMatrix(matrix)
        progressPaint.shader = gradient
    }
    
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        val centerX = width / 2f
        val centerY = height / 2f
        val innerRadius = Math.min(width, height) / 2f - strokeWidth * 2
        
        // Draw inner orange circle
        canvas.drawCircle(centerX, centerY, innerRadius, circlePaint)
        
        // Draw background arc
        canvas.drawArc(rect, -90f, 360f, false, backgroundPaint)
        
        // Draw progress arc
        val sweepAngle = (progress / maxProgress) * 360f
        canvas.drawArc(rect, -90f, sweepAngle, false, progressPaint)
        
        // Draw center text
        val textY = centerY - (textPaint.descent() + textPaint.ascent()) / 2
        canvas.drawText(centerText, centerX, textY, textPaint)
    }
    
    fun setProgress(value: Float) {
        progress = value.coerceIn(0f, maxProgress)
        invalidate()
    }
    
    fun setMaxProgress(value: Float) {
        maxProgress = value
        invalidate()
    }
    
    fun setCenterText(text: String) {
        centerText = text
        invalidate()
    }
}
