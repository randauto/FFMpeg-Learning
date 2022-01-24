package org.g3.ffmpeglearning

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main2.*
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity() {
    var widthStart = 0
    var widthEnd = 0
    var widthScreen = 0
    var viewStartX = 0f
    var viewEndX = 0f

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)


        viewStart?.viewTreeObserver!!.addOnGlobalLayoutListener { widthStart = viewStart?.layoutParams!!.width }

        viewEnd?.viewTreeObserver!!.addOnGlobalLayoutListener { widthEnd = viewEnd?.layoutParams!!.width }


        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val widthScreen = displayMetrics.widthPixels

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
                    val newWidth : Int = (widthStart + dX).toInt()


                    Log.d("TTTT", "newWidth = $newWidth")
                    when {
                        newWidth <= 150 -> {
                            viewStart.layoutParams.width = 150
                        }
                        newWidth + widthEnd <= widthScreen -> {
                            viewStart.layoutParams.width = newWidth
                        }
                        else -> {
                            viewStart.layoutParams.width = widthScreen - widthEnd
                        }
                    }
                    viewStart.requestLayout()

                }

            }

            true
        }

        viewEndDrag?.setOnTouchListener { v, event ->
            when (event!!.action) {
                MotionEvent.ACTION_DOWN -> {
                    viewEndX = event.x
                    Log.d("TTTT", "ACTION_DOWN")
                    Log.d("TTTT", "widthScreen = $widthScreen")
                    Log.d("TTTT", "widthStart = $widthStart")
                    Log.d("TTTT", "widthEnd = $widthEnd")

                    widthStart = viewStart.layoutParams.width
                }

                MotionEvent.ACTION_MOVE -> {
                    Log.d("TTTT", "ACTION_MOVE")
                    var newX = event.x
                    var dX = viewEndX - newX
                    val newWidth : Int = (widthEnd + dX).roundToInt()
                    Log.d("TTTT", "newWidth = $newWidth")
                    when {
                        newWidth >= 150 -> {
                            viewEnd.layoutParams.width = 150
                        }
                        newWidth + widthStart <= widthScreen -> {
                            viewEnd.layoutParams.width = newWidth
                        }
                        else -> {
                            viewEnd.layoutParams.width = widthScreen - widthStart
                        }
                    }
                    viewEnd.requestLayout()
                }

            }

            true
        }
    }
}