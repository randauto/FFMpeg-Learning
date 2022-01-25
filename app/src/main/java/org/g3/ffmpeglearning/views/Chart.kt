package org.g3.ffmpeglearning.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class Chart(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private var linePaint: Paint? = null

    private var dataPoints: FloatArray? = null
    private var minValue = 0f
    private var maxValue = 0f
    private var verticalDelta = 0f

    init {
        linePaint = Paint().apply {
            isAntiAlias = true
            color = Color.WHITE
            strokeWidth = 8.0f
            style = Paint.Style.STROKE
        }
    }

    fun setDataPoints(originalData: FloatArray) {
        dataPoints = FloatArray(originalData.size)
        minValue = Float.MAX_VALUE
        maxValue = Float.MIN_VALUE
        for (i in dataPoints!!.indices) {
            dataPoints!![i] = originalData[i]
            if (dataPoints!![i] < minValue) minValue = dataPoints!![i]
            if (dataPoints!![i] > maxValue) maxValue = dataPoints!![i]
        }
        verticalDelta = maxValue - minValue
        postInvalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        drawView()
    }

    private fun drawView() {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        canvas.drawARGB(255,0,0,0)
        val leftPadding = paddingLeft
        val topPadding = paddingTop
        val width = canvas.width - leftPadding - paddingRight
        val height = canvas.height - topPadding - paddingBottom
        val lastX = paddingStart
        val lastY = height * ((dataPoints!![0] - minValue) / verticalDelta) + topPadding


    }
}