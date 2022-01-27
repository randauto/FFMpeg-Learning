package org.g3.ffmpeglearning.views

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import android.widget.SeekBar
import kotlinx.android.synthetic.main.seekbar_single.view.*
import org.g3.ffmpeglearning.R


class SeekbarSingle : LinearLayout {
    private var widthSeekbar: Int = 0
    private var maxProgress: Int = 0
    private var minProgress: Int = 0
    private var mCurrentValue: Int = 0
    private var mCenterValue: Int = 0
    private var widthUnitProgress: Int = 0

    init {
        LayoutInflater.from(context).inflate(R.layout.seekbar_single, this, true)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr) {
        initParams(attrs)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initParams(attrs)
    }

    private fun initParams(attrs: AttributeSet) {

        val styledAttrs = context.obtainStyledAttributes(
            attrs,
            R.styleable.SeekbarSingle, 0, 0
        )
        maxProgress = styledAttrs.getInteger(R.styleable.SeekbarSingle_sb_maxProgress, 100)
//        minProgress = styledAttrs.getInteger(R.styleable.SeekbarSingle_sb_minProgress, 0)
        Log.d("TTTT", "maxProgress = $maxProgress")
        Log.d("TTTT", "minProgress = $minProgress")
        setMaxProgress(maxProgress)

        styledAttrs.recycle()

        initView()
    }

    fun setMaxProgress(max: Int) {
        maxProgress = max
        seekBarAudio.max = maxProgress
    }

    private fun initView() {
        seekBarAudio.max = maxProgress
        mCenterValue = (maxProgress - Math.abs(minProgress)) / 2
        mCurrentValue = mCenterValue

        seekbarGradientEnd.max = (maxProgress - minProgress) / 2
        seekbarGradientStart.max = (maxProgress - minProgress) / 2

        Log.d("TTTT", "mCenterValue = $mCenterValue")
        viewBgSeekbar?.viewTreeObserver!!.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                widthSeekbar = viewBgSeekbar.measuredWidth
                Log.d("TTTT", "widthSeekbar = $widthSeekbar")
                widthUnitProgress = (widthSeekbar / maxProgress)
                Log.d("TTTT", "widthUnitProgress = $widthUnitProgress")

                translateTextView(widthUnitProgress * mCenterValue - convertDpToPixel(1f, context).toInt())
                viewBgSeekbar.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
        seekBarAudio.progress = mCurrentValue
        seekBarAudio?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.d("TTTT", "onProgressChanged progress=$progress")
                var value = 0
                if (progress < mCenterValue) {
                    value = mCenterValue - progress
                    seekbarGradientStart.progress = value
                    seekbarGradientEnd.progress = 0
                    setTextValue(value, true)
                    translateTextView(widthUnitProgress * progress +
                            convertDpToPixel(2f, context).toInt())
                } else {
                    value = progress - mCenterValue
                    seekbarGradientEnd.progress = value
                    seekbarGradientStart.progress = 0
                    setTextValue(value, false)
                    translateTextView(widthUnitProgress * progress + convertDpToPixel(10f, context).toInt())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

    }

    private fun translateTextView(width: Int) {
        seekbarTextView?.layoutParams!!.width = width
        seekbarTextView?.requestLayout()
    }
    private fun setTextValue(value: Int, isNegativeValue: Boolean) {
        if (isNegativeValue) {
            tvValue?.text = "-$value"
        } else {
            tvValue?.text = "$value"
        }
    }


    private fun changeWidthView(view: View, width: Int) {
        view?.layoutParams.width = width
        view?.requestLayout()
    }

    fun convertDpToPixel(dp: Float, context: Context): Float {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return dp * (metrics.densityDpi / 160f)
    }

    fun convertPixelsToDp(px: Int, context: Context): Float {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return px / (metrics.densityDpi / 160f)
    }

}