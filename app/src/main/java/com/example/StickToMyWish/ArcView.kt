package com.example.StickToMyWish

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator

class ArcView @JvmOverloads constructor(
    context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    //根据数据显示的圆弧Paint
    private var mArcPaint: Paint? = null

    //文字描述的paint
    var mTextPaint: Paint? = null

    //圆弧开始的角度
    val startAngle = 270f

    //圆弧结束的角度
    private val endAngle = 45f

    //圆弧背景的开始和结束间的夹角大小
    private val mAngle = 360f

    var color : Int = R.color.white

    //当前进度夹角大小
    var mIncludedAngle = 0f

    //圆弧的画笔的宽度
    private val mStrokeWith = 10f

    //中心的文字描述
    private var mDes = ""

    //动画效果的数据及最大/小值
    private var mAnimatorValue = 0
    private var mMinValue = 0
    private var mMaxValue = 0

    //中心点的XY坐标
    private var centerX = 0f
    private var centerY = 0f
    private fun initPaint() {
//圆弧的paint
        mArcPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        //抗锯齿
        mArcPaint!!.isAntiAlias = true
        mArcPaint!!.color = Color.parseColor("#666666")
        //设置透明度（数值为0-255）
        mArcPaint!!.alpha = 100
        //设置画笔的画出的形状
        mArcPaint!!.strokeJoin = Paint.Join.ROUND
        mArcPaint!!.strokeCap = Paint.Cap.ROUND
        //设置画笔类型
        mArcPaint!!.style = Paint.Style.STROKE
        mArcPaint!!.strokeWidth = dp2px(mStrokeWith)
        //中心文字的paint
        mTextPaint = Paint()
        mTextPaint!!.isAntiAlias = true
        mTextPaint!!.color = Color.parseColor("#FF4A40")
        //设置文本的对齐方式
        mTextPaint!!.textAlign = Paint.Align.CENTER
        //mTextPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.dp_12));
        mTextPaint!!.textSize = dp2px(25f)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        centerX = (width / 2).toFloat()
        centerY = (height / 2).toFloat()
        //初始化paint
        initPaint()
        //绘制弧度
        drawArc(canvas)
        //绘制文本
        drawText(canvas)
    }

    /**
     * 绘制文本
     *
     * @param canvas
     */
    private fun drawText(canvas: Canvas) {
        val mRect = Rect()
        val mValue = mAnimatorValue.toString()
        //绘制中心的数值
        mTextPaint!!.getTextBounds(mValue, 0, mValue.length, mRect)
        //canvas.drawText(mAnimatorValue.toString(), centerX, centerY + mRect.height(), mTextPaint!!)
        //绘制中心文字描述
        mTextPaint!!.color = Color.parseColor("#999999")
        mTextPaint!!.textSize = dp2px(12f)
        mTextPaint!!.getTextBounds(mDes, 0, mDes.length, mRect)
        //canvas.drawText(mDes, centerX, centerY + 2 * mRect.height() + dp2px(10f), mTextPaint!!)
        //绘制最小值
        val minValue = mMinValue.toString()
        val maxValue = mMaxValue.toString()
        mTextPaint!!.textSize = dp2px(18f)
        mTextPaint!!.getTextBounds(minValue, 0, minValue.length, mRect)
//        canvas.drawText(
//            minValue,
//            (centerX - 0.6 * centerX - dp2px(5f)).toFloat(),
//            (centerY + 0.75 * centerY + mRect.height() + dp2px(5f)).toFloat(),
//            mTextPaint!!
//        )
        //绘制最大指
        mTextPaint!!.getTextBounds(maxValue, 0, maxValue.length, mRect)/*canvas.drawText(
            maxValue,
            (centerX + 0.6 * centerX + dp2px(5f)).toFloat(),
            (centerY + 0.75 * centerY + mRect.height() + dp2px(5f)).toFloat(),
            mTextPaint!!
        )*/
    }

    /**
     * 绘制当前的圆弧
     *
     * @param canvas
     */
    private fun drawArc(canvas: Canvas) {

        mArcPaint!!.color = color
        mArcPaint!!.style = Paint.Style.FILL
        canvas.drawCircle(
            (width / 2).toFloat(),
            (height / 2).toFloat(),
            (width / 2).toFloat() - dp2px(5f),
            mArcPaint!!
        )

        mArcPaint!!.color = Color.parseColor("#666666")
        //绘制圆弧背景
        val mRectF = RectF(
            mStrokeWith + dp2px(5f),
            mStrokeWith + dp2px(5f),
            width - mStrokeWith - dp2px(5f),
            height - mStrokeWith
        )
        mArcPaint!!.style = Paint.Style.STROKE
        canvas.drawArc(mRectF, startAngle, mAngle, false, mArcPaint!!)
        //绘制当前数值对应的圆弧
        mArcPaint!!.color = Color.parseColor("#FF4A40")
        //根据当前数据绘制对应的圆弧
        canvas.drawArc(mRectF, startAngle, mIncludedAngle, false, mArcPaint!!)
    }

    /**
     * 为绘制弧度及数据设置动画
     *
     * @param startAngle   开始的弧度
     * @param currentAngle 需要绘制的弧度
     * @param currentValue 需要绘制的数据
     * @param time         动画执行的时长
     */
    private fun setAnimation(startAngle: Float, currentAngle: Float, currentValue: Int, time: Int) {
//绘制当前数据对应的圆弧的动画效果
        val progressAnimator = ValueAnimator.ofFloat(startAngle, currentAngle)
        progressAnimator.duration = time.toLong()
        progressAnimator.setTarget(mIncludedAngle)
        progressAnimator.addUpdateListener { animation ->
            mIncludedAngle = animation.animatedValue as Float
            //重新绘制，不然不会出现效果
            postInvalidate()
        }
        //开始执行动画
        progressAnimator.start()
        //中心数据的动画效果
        val valueAnimator = ValueAnimator.ofInt(mAnimatorValue, currentValue)
        valueAnimator.duration = 2500
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.addUpdateListener { valueAnimator ->
            mAnimatorValue = valueAnimator.animatedValue as Int
            postInvalidate()
        }
        valueAnimator.start()
    }

    /**
     * 设置数据
     *
     * @param minValue     最小值
     * @param maxValue     最大值
     * @param currentValue 当前绘制的值
     * @param des          描述信息
     */
    fun setValues(minValue: Int, maxValue: Int, currentValue: Int, des: String) {
        var currentValue = currentValue
        mDes = des
        mMaxValue = maxValue
        mMinValue = minValue
        //完全覆盖
        if (currentValue > maxValue) {
            currentValue = maxValue
        }
        //计算弧度比重
        val scale = currentValue.toFloat() / maxValue
        //计算弧度
        val currentAngle = scale * mAngle
        //开始执行动画
        setAnimation(0f, currentAngle, currentValue, 2500)
    }

    fun dp2px(dp: Float): Float {
        val metrics = Resources.getSystem().displayMetrics
        return dp * metrics.density
    }
}