package org.g3.ffmpeglearning

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import kotlinx.android.synthetic.main.activity_main2.*
import kotlin.math.abs
import android.util.DisplayMetrics




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
        val height = displayMetrics.heightPixels
        val widthScreen = displayMetrics.widthPixels

        viewStartDrag?.setOnTouchListener { v, event ->
            when (event!!.action) {
                MotionEvent.ACTION_DOWN -> {
                    viewStartX = event.x
                    Log.d("TTTT", "ACTION_DOWN")
                    Log.d("TTTT", "widthScreen = $widthScreen")
                    Log.d("TTTT", "widthStart = $widthStart")
                    Log.d("TTTT", "widthEnd = $widthEnd")
                }

                MotionEvent.ACTION_MOVE -> {


                    Log.d("TTTT", "ACTION_MOVE")
                    var newX = event.x
                    var dX = newX - viewStartX
                    val newWidth = widthStart + dX

                    widthEnd = viewEnd.layoutParams.width
                    Log.d("TTTT", "newWidth = $newWidth")
                    if (newWidth + widthEnd < widthScreen && newWidth > 150) {
                        viewStart.layoutParams.width = (widthStart + dX).toInt()
                        viewStart.requestLayout()
                    }
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
                }

                MotionEvent.ACTION_MOVE -> {


                    Log.d("TTTT", "ACTION_MOVE")
                    var newX = event.x
                    var dX = viewEndX - newX
                    val newWidth = widthEnd + dX
                    widthStart = viewStart.layoutParams.width
                    Log.d("TTTT", "newWidth = $newWidth")
                    if (newWidth + widthStart < widthScreen && newWidth > 150) {
                        viewEnd.layoutParams.width = (widthEnd + dX).toInt()
                        viewEnd.requestLayout()
                    }
                }

            }

            true
        }
    }
}