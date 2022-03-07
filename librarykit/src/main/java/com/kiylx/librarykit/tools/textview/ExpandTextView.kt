package com.kiylx.librarykit.tools.textview

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.text.Layout
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.view.ViewTreeObserver
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import com.kiylx.librarykit.R


open class ExpandTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatTextView(context, attrs, defStyleAttr) {

    private var allTextLineCount = 0//全部字符串的行数
    private var clickTime: Long = 0//点击时间
    private var firstTime = true//第一次触发settext方法
    private var hintLocate = 3//展开或折叠的提示文字位置
    private var isOverMaxLine = false//是否超过最大限制行,超过就触发折叠文本逻辑
    private var isShowTipAfterExpand = true//展开后是否显示提示文字
    private var isShowTipBeforeCollapse = true//折叠时是否显示提示文字
    private var mPaint: Paint = Paint()
    private var mTipClickable = true//是否可点击提示文字,以触发折叠或展开逻辑
    private var mTipColor = 0
    private var maxLine = 3//最大限制行

    private var maxX = 0f
    private var maxY = 0f
    private var middleY = 0f
    private var minX = 0f
    private var minY = 0f

    private var originText: CharSequence? = null//原始字符串
    private var ellipsisHint: String = "..."//在提示文字之前的省略号
    private var textExpand: String = "展开"
    private var textCollapse: String = "折叠"//展开后的提示文字,默认是"折叠"
    private val tipClickListener: TipClickListener? = null
    private var toExpand = false

    init {
        initAttr(attrs);
        if (TextUtils.isEmpty(textExpand)) {
            textExpand = "展开"
        }
        mPaint.textSize = textSize
        mPaint.color = mTipColor
    }

    private fun initAttr(attrs: AttributeSet?) {
        val a: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.ExpandTextView, 0, 0)
        hintLocate = a.getInt(R.styleable.ExpandTextView_hintLocate, 3)
        maxLine = a.getInt(R.styleable.ExpandTextView_MaxLine, 3)
        mTipColor = a.getColor(R.styleable.ExpandTextView_hintColor, -1)
        isShowTipAfterExpand = a.getBoolean(R.styleable.ExpandTextView_showTipAfterExpand, true)
        isShowTipBeforeCollapse = a.getBoolean(R.styleable.ExpandTextView_showTipBeforeCollapse, true)
        //textCollapse = a.getString(R.styleable.ExpandTextView_textCollapse)
        //textExpand = a.getString(R.styleable.ExpandTextView_textExpand)

        a.recycle()
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        if (TextUtils.isEmpty(text) || maxLine == 0) {
            super.setText(text, type)
        } else if (toExpand) {
            expandText(type)
        } else {
            collapseText(text, type)
        }
    }

    //展开文字
    private fun expandText(type: TextView.BufferType?) {
        val spannable = SpannableStringBuilder(originText)
        if (isShowTipAfterExpand) {
            spannable.append(textCollapse)
            spannable.setSpan(ForegroundColorSpan(mTipColor), spannable.length - textCollapse.length, spannable.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        }
        super.setText(spannable, type)
        val mLieCount = lineCount
        val layout = layout
        minX = paddingLeft + layout.getPrimaryHorizontal(spannable.lastIndexOf(textCollapse[0]) - 1)
        maxX = paddingLeft + layout.getPrimaryHorizontal(spannable.lastIndexOf(textCollapse[textCollapse.length - 1]) + 1)
        val bound = Rect()
        layout.getLineBounds(allTextLineCount - 1, bound)
        if (mLieCount > allTextLineCount) {
            //不在同一行
            minY = (paddingTop + bound.top).toFloat()
            middleY = minY + paint.fontMetrics.descent - paint.fontMetrics.ascent
            maxY = middleY + paint.fontMetrics.descent - paint.fontMetrics.ascent
        } else {
            //同一行
            minY = (paddingTop + bound.top).toFloat()
            maxY = minY + paint.fontMetrics.descent - paint.fontMetrics.ascent
        }
    }

    //折叠文本
    private fun collapseText(text: CharSequence?, type: BufferType?) {
        if (firstTime) {
            viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    viewTreeObserver.removeOnPreDrawListener(this)
                    firstTime = false
                    formatText(text, type)
                    return true
                }
            })
        } else {
            formatText(text, type)
        }
    }

    fun formatText(text: CharSequence?, type: TextView.BufferType?) {
        originText = text.toString()
        var textLayout = layout
        if (textLayout == null || !textLayout.text.equals(originText)) {
            super.setText(originText, type)
            textLayout = layout
        }
        if (textLayout == null) {
            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                    translateText(layout, type)
                }
            })
        } else {
            translateText(textLayout, type)
        }
    }

    fun translateText(layout: Layout, type: BufferType?) {
        //记录原始行数
        allTextLineCount = layout.lineCount
        if (layout.lineCount > maxLine) {
            isOverMaxLine = true
            val startOffset = layout.getLineStart(maxLine - 1)
            var endOffset = layout.getLineVisibleEnd(maxLine - 1)
            if (hintLocate == HintLocate.FOLLOW) {
                val builder = StringBuilder(ellipsisHint).append("  ").append(textExpand)
                endOffset -= paint.breakText(originText, startOffset, endOffset, false, paint.measureText(builder.toString()), null)
            } else {
                endOffset--;
            }
            val span = SpannableStringBuilder()
            val ellipsize = originText!!.subSequence(0, endOffset)
            span.append(ellipsize).append(ellipsisHint)
            if (hintLocate != HintLocate.FOLLOW) {
                span.append("\n")
            }
            super.setText(span, type)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (isOverMaxLine && !toExpand) {
            //折叠
            if (hintLocate == HintLocate.FOLLOW) {
                minX = width - paddingLeft - paddingRight - paint.measureText(textExpand)
                maxX = (width - paddingLeft - paddingRight).toFloat()
            } else {
                minX = paddingLeft.toFloat()
                maxX = minX + paint.measureText(textExpand)
            }
            minY = height - (paint.fontMetrics.descent - paint.fontMetrics.ascent) - paddingBottom
            maxY = (height - paddingBottom).toFloat()
            canvas?.drawText(textExpand, minX, height - paint.fontMetrics.descent - paddingBottom, mPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (mTipClickable) {
            when (event?.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    if (this.mTipClickable && ((this.toExpand && this.isShowTipAfterExpand) || (!this.toExpand && this.isShowTipBeforeCollapse))) {
                        clickTime = System.currentTimeMillis()
                        if (!isClickable && isInRange(event.x, event.y)) {
                            return true
                        }
                    }
                }
                MotionEvent.ACTION_CANCEL,
                MotionEvent.ACTION_UP -> {
                    val delTime = System.currentTimeMillis() - clickTime
                    clickTime = 0L
                    if (delTime < ViewConfiguration.getTapTimeout() && isInRange(event.x, event.y)) {
                        toExpand = !toExpand
                        setText(originText)
                        if (this.tipClickListener != null)
                            tipClickListener.onTipClick(toExpand)
                        return true
                    }
                }
            }
        }
        return super.onTouchEvent(event)
    }

    fun isInRange(x: Float, y: Float): Boolean {
        if (minX < maxX) {
            //同一行
            return x in minX..maxX && y in minY..maxY
        } else {
            //两行
            return x <= maxX && y in middleY..maxY || x >= minX && y in minY..middleY
        }
    }


    /**
     * 调用此方法让文本展开或是折叠
     */
    fun click() {
        toExpand = !toExpand
        setText(originText)
    }

    class HintLocate {
        companion object {
            const val LEFT = 0
            const val CENTER = 1
            const val RIGHT = 2
            const val FOLLOW = 3
        }

    }

    companion object {
        private val TAG = "ExpandTextView"
    }
}