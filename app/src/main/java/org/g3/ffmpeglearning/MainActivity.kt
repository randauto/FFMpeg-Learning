package org.g3.ffmpeglearning

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main2.*
import org.g3.ffmpeglearning.ui.TestActivity
import java.io.File
import kotlin.math.roundToInt


class MainActivity : TestActivity() {
    var widthStart = 0
    var widthEnd = 0
    var viewStartX = 0f
    var viewEndX = 0f
    var widthScreen = 0
    var widthMin = 0

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        viewRoot?.viewTreeObserver!!.addOnGlobalLayoutListener {
            widthScreen = viewRoot.measuredWidth
        }

        viewEndDrag?.viewTreeObserver!!.addOnGlobalLayoutListener {
            widthMin = viewEndDrag.measuredWidth
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
                    var newWidth: Int = (widthStart + dX).toInt()


                    Log.d("TTTT", "newWidth = $newWidth")
                    when {
                        newWidth <= widthMin -> {
                            newWidth = widthMin
                            viewStart.layoutParams.width = newWidth
                        }
                        newWidth + widthEnd <= widthScreen -> {
                            viewStart.layoutParams.width = newWidth
                        }
                        else -> {
                            newWidth = widthScreen - widthEnd
                            viewStart.layoutParams.width = newWidth
                        }
                    }

                    viewStart.requestLayout()
                    calculateDurationStart(newWidth)
                }

            }

            true
        }
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
                        newWidth <= widthMin -> {
                            newWidth = widthMin
                            viewEnd.layoutParams.width = newWidth
                        }
                        newWidth + widthStart <= widthScreen -> {
                            viewEnd.layoutParams.width = newWidth
                        }
                        else -> {
                            newWidth = widthScreen - widthStart
                            viewEnd.layoutParams.width = newWidth
                        }
                    }

                    viewEnd.requestLayout()
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

    var durationAudio : Long = 0
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
            val durationEnd : Long = (newWidth * durationAudio / widthScreen)
            setTextValue(tvEnd, durationEnd)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    private fun calculateDurationStart(newWidth: Int) {
        try {
            val durationStart : Long = newWidth * durationAudio / widthScreen
            setTextValue(tvStart, durationStart)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    private fun setTextValue(textView: TextView, value: Long) {
        val newValue = AudioManager.formatSeconds(value)
        textView?.text = "$newValue"
    }


}