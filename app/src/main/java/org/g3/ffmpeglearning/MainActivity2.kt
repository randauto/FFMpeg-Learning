package org.g3.ffmpeglearning

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main3.*
import org.g3.ffmpeglearning.ui.TestActivity
import org.g3.ffmpeglearning.views.IRangeSeekbarListener
import java.io.File


class MainActivity2 : TestActivity() {
    var widthStart = 0
    var widthEnd = 0
    var widthStartTvFake = 0
    var widthEndTvFake = 0
    var touchDownviewStartX = 0f
    var viewEndX = 0f
    var widthSeekbarView = 0
    var maxWidth: Int = 0
    var widthMinEnd = 0
    var callBack: IRangeSeekbarListener? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
        viewBgStart?.viewTreeObserver!!.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {

                maxWidth = viewBgStart.measuredWidth / 2

                Log.d("TTTT", "widthMinStart = $maxWidth")

                viewBgStart.viewTreeObserver.removeGlobalOnLayoutListener { this }
            }
        })

        viewGradientStart?.viewTreeObserver!!.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                widthStart = viewGradientStart.measuredWidth
                Log.d("TTTT", "widthStart = $widthStart")
//                viewGradientStart.viewTreeObserver.removeGlobalOnLayoutListener { this }
            }
        })

        viewGradientEnd?.viewTreeObserver!!.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                widthEnd = viewGradientEnd.measuredWidth

                Log.d("TTTT", "widthEnd = $widthEnd")
//                viewGradientEnd.viewTreeObserver.removeGlobalOnLayoutListener { this }
            }
        })


        setupViewStart()

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
        tvStart?.viewTreeObserver!!.addOnGlobalLayoutListener {
            widthStartTvFake = tvStart.measuredWidth
        }


        tvEnd?.viewTreeObserver!!.addOnGlobalLayoutListener {
            widthEndTvFake = tvEnd.measuredWidth
        }



        viewStartDrag?.setOnTouchListener { v, event ->
            when (event!!.action) {
                MotionEvent.ACTION_DOWN -> {
                    touchDownviewStartX = event.x
                }

                MotionEvent.ACTION_MOVE -> {
                    Log.d("ACTION_MOVE", "GestureDetector  = ACTION_MOVE")
                    Log.d("ACTION_MOVE", "ACTION_MOVE")
                    var newX = event.x
                    var dX: Int = (newX - touchDownviewStartX).toInt()

                    Log.d("ACTION_MOVE", "dX = $dX")
                    Log.d("ACTION_MOVE", "widthStart = $widthStart")
                    Log.d("ACTION_MOVE", "widthEnd = $widthEnd")

                    if (dX > 0) {
                        if (widthStart > 0) {
                            changeWidthView(viewGradientStart, dX)
//                            translateX(viewGradientStart, -(dX / 2).toFloat())
//                            translateX(viewStartDrag, -(dX).toFloat())
                        } else {
                            if (widthEnd > 0) {
                                changeWidthView(viewGradientEnd, widthEnd/2 + dX)
//                                translateX(viewGradientEnd, -(dX / 2).toFloat())
//                                translateX(viewStartDrag, -(dX).toFloat())
                            } else {
                                if (widthEnd == 0) {
                                    changeWidthView(viewGradientEnd, dX)
                                } else {
                                    changeWidthView(viewGradientEnd, widthEnd + dX)
                                }
//                                translateX(viewGradientEnd, (dX / 2).toFloat())
//                                translateX(viewStartDrag, (dX).toFloat())
                            }

                        }


                    } else {
                        if (widthStart > 0) {
                            changeWidthView(viewGradientStart, dX)
                            translateX(viewGradientStart, -(dX / 2).toFloat())
                            translateX(viewStartDrag, -(dX).toFloat())

                        } else {
                            changeWidthView(viewGradientStart, dX)
                            translateX(viewGradientStart, (dX / 2).toFloat())
                            translateX(viewStartDrag, (dX).toFloat())
                        }

                        if (widthEnd > 0) {
                            changeWidthView(viewGradientEnd, dX)
                            translateX(viewGradientEnd, -(dX / 2).toFloat())
                            translateX(viewStartDrag, -(dX).toFloat())

                        } else {
                            changeWidthView(viewGradientEnd, dX)
                            translateX(viewGradientEnd, (dX / 2).toFloat())
                            translateX(viewStartDrag, (dX).toFloat())
                        }
                    }

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

    private fun translateX(view: View, width: Float) {
        view?.x = width
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
            if (newWidth == maxWidth) {
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