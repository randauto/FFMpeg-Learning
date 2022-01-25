package org.g3.ffmpeglearning

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.core.view.marginStart
import kotlinx.android.synthetic.main.activity_main2.*
import org.g3.ffmpeglearning.ui.TestActivity
import java.io.File
import kotlin.math.roundToInt


class MainActivity : TestActivity() {
    var widthStart = 0
    var widthStartTvFake = 0
    var widthEndTvFake = 0
    var widthEnd = 0
    var viewStartX = 0f
    var viewEndX = 0f
    var widthScreen = 0
    var widthMinStart = 0
    var widthMinEnd = 0

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        viewStartDrag?.viewTreeObserver!!.addOnGlobalLayoutListener {
            widthMinStart = viewStartDrag.measuredWidth
            widthMinStart += widthMinStart
            widthMinStart += viewBgStart.marginStart
            widthScreen = viewGradient.measuredWidth
        }
        viewEndDrag?.viewTreeObserver!!.addOnGlobalLayoutListener {
            widthMinEnd = viewEndDrag.measuredWidth
            widthMinEnd += widthMinEnd
            widthMinEnd += viewEndDrag.marginStart
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

    @SuppressLint("ClickableViewAccessibility")
    private fun setupViewStart() {
        viewStart?.viewTreeObserver!!.addOnGlobalLayoutListener {
            widthStart = viewStart.measuredWidth
        }

        tvStart?.viewTreeObserver!!.addOnGlobalLayoutListener {
            widthStartTvFake = tvStart.measuredWidth
        }


        tvEnd?.viewTreeObserver!!.addOnGlobalLayoutListener {
            widthEndTvFake = tvEnd.measuredWidth
        }



        viewStartDrag?.setOnTouchListener { v, event ->
            when (event!!.action) {
                MotionEvent.ACTION_DOWN -> {
                    viewStartX = event.x
                    Log.d("TTTT", "ACTION_DOWN")
                    Log.d("TTTT", "widthScreen = $widthScreen")
                    Log.d("TTTT", "widthStart = $widthStart")
                    Log.d("TTTT", "widthEnd = $widthEnd")
                    widthEnd = viewEnd.layoutParams.width
                }

                MotionEvent.ACTION_MOVE -> {
                    Log.d("TTTT", "ACTION_MOVE")
                    var newX = event.x
                    var dX = newX - viewStartX
                    var newWidthStart: Int = (widthStart + dX).toInt()


                    Log.d("TTTT", "newWidth = $newWidthStart")
                    when {
                        newWidthStart <= widthMinStart -> {
                            newWidthStart = widthMinStart
                        }
                        newWidthStart + widthEnd <= widthScreen -> {
                        }
                        else -> {
                            newWidthStart = widthScreen - widthEnd
                        }
                    }

                    changeWidthView(viewStart, newWidthStart)
                    val newWidthStartFake = newWidthStart - (widthStartTvFake / 2)
                    changeWidthView(viewFakeBgStart, newWidthStartFake)
                    calculateDurationStart(newWidthStart)
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
        }

        viewEndDrag?.setOnTouchListener { v, event ->
            when (event!!.action) {
                MotionEvent.ACTION_DOWN -> {
                    viewEndX = event.x
                    Log.d("TTTT", "ACTION_DOWN")
                    Log.d("TTTT", "widthScreen = $widthScreen")
                    Log.d("TTTT", "widthStart = $widthStart")
                    Log.d("TTTT", "widthEnd = $widthEnd")

                    widthEnd = viewEnd.measuredWidth
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
                        newWidth + widthStart <= widthScreen -> {
                        }
                        else -> {
                            newWidth = widthScreen - widthStart
                        }
                    }

                    changeWidthView(viewEnd, newWidth)
                    val newWidthStartFake = newWidth - (widthEndTvFake / 2)
                    changeWidthView(viewFakeBgEnd, newWidthStartFake)
                    calculateDurationEnd(newWidth)
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
            Log.d("TTTT", "widthScreen: $widthScreen")


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
                durationEnd = (widthScreen - newWidth * durationAudio / widthScreen).toFloat()
            }

            setTextValue(tvEnd, durationEnd.toLong())
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    private fun calculateDurationStart(newWidth: Int) {
        try {
            var durationStart: Float = 0f
            if (newWidth == widthMinStart) {
                durationStart = 0f
            } else {
                durationStart = ((newWidth * durationAudio) / widthScreen).toFloat()
            }
            setTextValue(tvStart, durationStart.toLong())
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    private fun setTextValue(textView: TextView, value: Long) {
        val newValue = AudioManager.formatSeconds(value)
        textView?.text = "$newValue"
    }


}