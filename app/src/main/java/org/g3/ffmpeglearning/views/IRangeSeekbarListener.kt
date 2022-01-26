package org.g3.ffmpeglearning.views

interface IRangeSeekbarListener {
    fun onStartChange(value: Long)
    fun onEndChange(value: Long)
    fun onStartStop(value: Long)
    fun onEndStop(value: Long)
}