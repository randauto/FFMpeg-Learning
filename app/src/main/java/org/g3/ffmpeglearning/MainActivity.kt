package org.g3.ffmpeglearning

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import android.widget.TextView
import androidx.core.view.marginEnd
import androidx.core.view.marginStart
import kotlinx.android.synthetic.main.activity_main2.*
import org.g3.ffmpeglearning.ui.TestActivity
import org.g3.ffmpeglearning.views.IRangeSeekbarListener
import java.io.File
import kotlin.math.roundToInt


class MainActivity : TestActivity() {
    var widthStart = 0
    var widthStartTvFake = 0
    var widthEndTvFake = 0
    var widthEnd = 0
    var touchDownviewStartX = 0f
    var viewEndX = 0f
    var widthSeekbarView = 0
    var widthMinStart: Int = 0
    var widthMinEnd = 0
    var callBack: IRangeSeekbarListener? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        viewStartDrag?.viewTreeObserver!!.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                Log.d("viewStartDrag", "viewStartDrag.width = ${viewStartDrag.measuredWidth}")
                Log.d("viewBgStart", "viewBgStart.marginStart = ${viewBgStart.marginStart}")
                Log.d("viewStartDrag", "viewStartDrag.paddingStart = ${viewStartDrag.paddingStart}")
                Log.d("viewStartDrag", "viewStartDrag.paddingEnd = ${viewStartDrag.paddingEnd}")

                widthMinStart = viewStartDrag.measuredWidth + viewBgStart.marginStart + viewStartDrag.paddingStart

                Log.d("TTTT", "widthMinStart = $widthMinStart")
            }
        })
        viewEndDrag?.viewTreeObserver!!.addOnGlobalLayoutListener {
            widthMinEnd = viewEndDrag.measuredWidth + viewBgEnd.marginEnd + viewEndDrag.paddingStart

            Log.d("TTTT", "widthMinEnd = $widthMinEnd")
        }

        viewGradient?.viewTreeObserver!!.addOnGlobalLayoutListener {
            widthSeekbarView = (viewGradient.measuredWidth + convertDpToPixel(45f, this)).toInt()

            Log.d("TTTT", "widthScreen = $widthSeekbarView")
        }

        setupViewStart()
        setupViewEnd()

        onCLickView()
    }

    private fun onCLickView() {
        btnChooseAudio?.setOnClickListener {
            pickAudio()
        }
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

    @SuppressLint("ClickableViewAccessibility")
    private fun setupViewStart() {
        val gestureDetectorStart = GestureDetector(this, object : GestureDetector.OnGestureListener {
            override fun onDown(e: MotionEvent?): Boolean {
                Log.d("TTTT", "GestureDetector  = onDown")
                touchDownviewStartX = e!!.x
                Log.d("TTTT", "ACTION_DOWN")
                Log.d("TTTT", "widthScreen = $widthSeekbarView")
                Log.d("TTTT", "widthStart = $widthStart")
                Log.d("TTTT", "widthEnd = $widthEnd")
                Log.d("TTTT", "widthMinStart = $widthMinStart")
                widthEnd = viewEnd.layoutParams.width
                return true
            }

            override fun onShowPress(e: MotionEvent?) {
                Log.d("TTTT", "GestureDetector  = onShowPress")

            }

            override fun onSingleTapUp(e: MotionEvent?): Boolean {
                Log.d("TTTT", "GestureDetector  = onSingleTapUp")
                return true
            }

            override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {

                return true
            }

            override fun onLongPress(e: MotionEvent?) {
                Log.d("TTTT", "GestureDetector  = onLongPress")

            }

            override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
                Log.d("TTTT", "GestureDetector = onFling velocityX = $velocityX")


                if (Math.abs(velocityX) >=4000) {
                    Log.d("TTTT", "GestureDetector  = ACTION_MOVE")
                    Log.d("TTTT", "ACTION_MOVE")
                    var newX1 = e1!!.x
                    var newX2 = e2!!.x
                    var dX = newX2 - newX1

                    Log.d("TTTT", "dX = $dX")
                    var newWidthStart: Int = (widthStart + dX).toInt()


                    Log.d("TTTT", "newWidth = $newWidthStart")
                    when {
                        newWidthStart <= widthMinStart -> {
                            newWidthStart = widthMinStart
                        }
                        newWidthStart + widthEnd >= widthSeekbarView -> {
                            newWidthStart = widthSeekbarView - widthEnd
                        }
                    }

                    changeWidthView(viewStart, newWidthStart)
                    val newWidthStartFake = newWidthStart - (widthStartTvFake / 2)
                    changeWidthView(viewFakeBgStart, newWidthStartFake)
                    calculateDurationStart(newWidthStart)
                }

                return true
            }

        })

        viewStart?.viewTreeObserver!!.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                widthStart = viewStart.measuredWidth

                Log.d("TTTT", "widthStart = $widthStart")
            }
        })

        tvStart?.viewTreeObserver!!.addOnGlobalLayoutListener {
            widthStartTvFake = tvStart.measuredWidth
        }


        tvEnd?.viewTreeObserver!!.addOnGlobalLayoutListener {
            widthEndTvFake = tvEnd.measuredWidth
        }



        viewStartDrag?.setOnTouchListener { v, event ->
            gestureDetectorStart.onTouchEvent(event)
            when (event!!.action) {
                MotionEvent.ACTION_DOWN -> {

                }

                MotionEvent.ACTION_MOVE -> {
                    Log.d("TTTT", "GestureDetector  = ACTION_MOVE")
                    Log.d("TTTT", "ACTION_MOVE")
                    var newX = event.x
                    var dX = newX - touchDownviewStartX

                    Log.d("TTTT", "dX = $dX")
                    var newWidthStart: Int = (widthStart + dX).toInt()


                    Log.d("TTTT", "newWidth = $newWidthStart")
                    when {
                        newWidthStart <= widthMinStart -> {
                            newWidthStart = widthMinStart
                        }
                        newWidthStart + widthEnd >= widthSeekbarView -> {
                            newWidthStart = widthSeekbarView - widthEnd
                        }
                    }

                    changeWidthView(viewStart, newWidthStart)
                    val newWidthStartFake = newWidthStart - (widthStartTvFake / 2)
                    changeWidthView(viewFakeBgStart, newWidthStartFake)
                    calculateDurationStart(newWidthStart)
                }

                MotionEvent.ACTION_CANCEL -> {
                    callBack?.onStartStop(0L)
                }

                MotionEvent.ACTION_UP -> {
                    callBack?.onStartStop(0L)
                }

            }

            true
        }
    }

    private fun changeWidthView(view: View, width: Int) {
        view?.layoutParams.width = width
        view?.requestLayout()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupViewEnd() {
        viewEnd?.viewTreeObserver!!.addOnGlobalLayoutListener {
            widthEnd = viewEnd?.layoutParams!!.width
            Log.d("TTTT", "widthEnd = $widthEnd")
        }

        viewEndDrag?.setOnTouchListener { v, event ->
            when (event!!.action) {
                MotionEvent.ACTION_DOWN -> {
                    viewEndX = event.x
                    Log.d("TTTT", "ACTION_DOWN")
                    Log.d("TTTT", "widthScreen = $widthSeekbarView")
                    Log.d("TTTT", "widthStart = $widthStart")
                    Log.d("TTTT", "widthEnd = $widthEnd")
                }

                MotionEvent.ACTION_MOVE -> {
                    Log.d("TTTT", "ACTION_MOVE")
                    var newX = event.x
                    var dX = viewEndX - newX
                    var newWidth: Int = (widthEnd + dX).roundToInt()
                    Log.d("TTTT", "newWidth = $newWidth")
                    when {
                        newWidth <= widthMinEnd -> {
                            newWidth = widthMinEnd
                        }
                        newWidth + widthStart <= widthSeekbarView -> {
                        }
                        else -> {
                            newWidth = widthSeekbarView - widthStart
                            tvStart.translationX
                        }
                    }

                    changeWidthView(viewEnd, newWidth)
                    val newWidthStartFake = newWidth - (widthEndTvFake / 2)
                    changeWidthView(viewFakeBgEnd, newWidthStartFake)
                    calculateDurationEnd(newWidth)
                }

                MotionEvent.ACTION_CANCEL -> {
                    callBack?.onEndStop(0L)
                }

                MotionEvent.ACTION_UP -> {
                    callBack?.onEndStop(0L)
                }

            }

            true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode === 1) {
            if (resultCode === RESULT_OK) {

                //the selected audio.
                data?.let {
                    val uri: Uri? = data.data
                    Log.d("TTTT", "uri = ${uri.toString()}")
                    val file = File(uri!!.path)

                    pathAudio = FileUtils.getPath(this, uri)
                    getInfoFile(pathAudio)
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    var durationAudio: Long = 0
    private fun getInfoFile(pathAudio: String?) {
        pathAudio?.let {
            durationAudio = AudioManager.getDuration(File(pathAudio))
            setTextValue(tvDuration, durationAudio)
            //
            Log.d("TTTT", "Duration Audio: $durationAudio")
            Log.d("TTTT", "widthStart: $widthStart")
            Log.d("TTTT", "widthEnd: $widthEnd")
            Log.d("TTTT", "widthScreen: $widthSeekbarView")


            calculateDurationStart(widthStart)

            calculateDurationEnd(widthEnd)
        }
    }

    private fun calculateDurationEnd(newWidth: Int) {
        try {
            var durationEnd: Float = 0f

            if (newWidth == widthMinEnd) {
                durationEnd = 0f
            } else {
                durationEnd = (widthSeekbarView - newWidth * durationAudio / widthSeekbarView).toFloat()
            }

            setTextValue(tvEnd, durationEnd.toLong())
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    private fun calculateDurationStart(newWidth: Int) {
        try {
            val widthSum = widthSeekbarView.toFloat()
            var durationStart: Float = 0f
            if (newWidth == widthMinStart) {
                durationStart = 0f
            } else {
                durationStart = ((newWidth * durationAudio) / widthSum)
            }
            Log.d("durationStart", "durationStart = $durationStart")
            Log.d("widthSeekbarView", "widthSeekbarView = $widthSum")
            Log.d("newWidth", "newWidth = $newWidth")
            setTextValue(tvStart, durationStart.toLong())
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        callBack?.onStartChange(0L)
    }

    private fun setTextValue(textView: TextView, value: Long) {
        val newValue = AudioManager.formatSeconds(value)
        textView?.text = "$newValue"
    }


}