package net.ferraSolution.food.ui.progress

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.annotation.RequiresApi
import java.util.*

class RippleView : View, AnimatorUpdateListener {
    @SuppressLint("Recycle")
    private inner class Ripple internal constructor(startRadiusFraction: Float, stopRadiusFraction: Float, startAlpha: Float, stopAlpha: Float, color: Int, delay: Long, duration: Long, strokeWidth: Float, updateListener: AnimatorUpdateListener?) {
        var mAnimatorSet: AnimatorSet
        var mRadiusAnimator: ValueAnimator = ValueAnimator.ofFloat(startRadiusFraction, stopRadiusFraction)
        var mAlphaAnimator: ValueAnimator
        var mPaint: Paint
        fun draw(canvas: Canvas, centerX: Int, centerY: Int, radiusMultiplicator: Float) {
            mPaint.alpha = (255 * mAlphaAnimator.animatedValue as Float).toInt()
            canvas.drawCircle(centerX.toFloat(), centerY.toFloat(), mRadiusAnimator.animatedValue as Float * radiusMultiplicator, mPaint)
        }

        fun startAnimation() {
            mAnimatorSet.start()
        }

        fun stopAnimation() {
            mAnimatorSet.cancel()
        }

        init {
            mRadiusAnimator.duration = duration
            mRadiusAnimator.repeatCount = ValueAnimator.INFINITE
            mRadiusAnimator.addUpdateListener(updateListener)
            mRadiusAnimator.interpolator = DecelerateInterpolator()
            mAlphaAnimator = ValueAnimator.ofFloat(startAlpha, stopAlpha)
            mAlphaAnimator.duration = duration
            mAlphaAnimator.repeatCount = ValueAnimator.INFINITE
            mAlphaAnimator.addUpdateListener(updateListener)
            mAlphaAnimator.interpolator = DecelerateInterpolator()
            mAnimatorSet = AnimatorSet()
            mAnimatorSet.playTogether(mRadiusAnimator, mAlphaAnimator)
            mAnimatorSet.startDelay = delay
            mPaint = Paint()
            mPaint.style = Paint.Style.STROKE
            mPaint.color = color
            mPaint.alpha = (255 * startAlpha).toInt()
            mPaint.isAntiAlias = true
            mPaint.strokeWidth = strokeWidth
        }
    }

    private var mRipples: MutableList<Ripple> = ArrayList()

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startAnimation()
    }

    override fun onDetachedFromWindow() {
        stopAnimation()
        super.onDetachedFromWindow()
    }

    private fun init() {
        if (isInEditMode) return

        /*
        Tweak your ripples here!
        */mRipples = ArrayList()
        mRipples.add(Ripple(0.0f, 1.0f, 1.0f, 0.0f, Color.parseColor("#aaffffff"), 0, 800, 16f, this))
        mRipples.add(Ripple(0.0f, 1.0f, 1.0f, 0.0f, Color.parseColor("#ffffffff"), 1024, 1200, 16f, this))
        mRipples.add(Ripple(0.0f, 1.0f, 1.0f, 0.0f, Color.parseColor("#ccffffff"), 2048, 1600, 16f, this))
    }

    fun startAnimation() {
        visibility = VISIBLE
        for (ripple in mRipples) {
            ripple.startAnimation()
        }
    }

    fun stopAnimation() {
        for (ripple in mRipples) {
            ripple.stopAnimation()
        }
        visibility = GONE
    }

    override fun onAnimationUpdate(animation: ValueAnimator) {
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        val centerX = width / 2
        val centerY = height / 2
        val radiusMultiplication = width / 2
        for (ripple in mRipples) {
            ripple.draw(canvas, centerX, centerY, radiusMultiplication.toFloat())
        }
    }

    companion object {
        const val TAG = "RippleView"
    }
}