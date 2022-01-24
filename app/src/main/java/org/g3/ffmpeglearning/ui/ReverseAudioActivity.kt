package org.g3.ffmpeglearning.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.g3.ffmpeglearning.PlayerThread
import org.g3.ffmpeglearning.R
import org.g3.ffmpeglearning.RecorderThread

open class ReverseAudioActivity  : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_reverse_audio)
        tvStatus = findViewById(R.id.tvStatus)
        pb = findViewById(R.id.pb)
        btnRecord = findViewById(R.id.btnRecord)
        btnPlay = findViewById(R.id.btnPlay)
        myHandler.sendEmptyMessage(STATUS_NONE)
    }

    /**
     * Message for handler about main activities statement
     */
    val STATUS_NONE = 0

    /**
     * Message for handler about activities statement, when recording is in progress
     */
    var  STATUS_RECORDING = 1

    /**
     * Message for handler about activities statement, when playing is in progress
     */
    val STATUS_PLAYING = 2

    /**
     * Message for handler about activities statement, when recording is finished
     */
    val STATUS_RECORDING_END = 3

    /**
     * Message for handler about activities statement, when playing is finished
     */
    val STATUS_PLAYING_END = 4

    /**
     * Message for handler about activities statement,
     * when application has not file to playing
     */
    val STATUS_NONE_PLAY = 5


    /**
     * Show operations name and status
     */
    var tvStatus: TextView? = null

    /**
     * Show operation progress
     */
    var pb: ProgressBar? = null

    /**
     * Button to start record
     */
    var btnRecord: Button? = null

    /**
     * Button to start play
     */
    var btnPlay: Button? = null

    /**
     * RecorderThread object
     */
    var recorderThread: RecorderThread? = null

    /**
     * PlayerThread object
     */
    var playerThread: PlayerThread? = null


    /**
     * Show the progress of operations and statement of activities element
     */


    var myHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                STATUS_NONE -> {
                    btnRecord!!.isEnabled = true
                    btnPlay!!.isEnabled = true
                    tvStatus!!.text = "Select button, please)"
                    pb!!.visibility = View.GONE
                }
                STATUS_RECORDING -> {
                    btnRecord!!.isEnabled = false
                    btnPlay!!.isEnabled = false
                    tvStatus!!.text = "Recording..."
                    pb!!.max = msg.arg1
                    pb!!.progress = msg.arg2
                    pb!!.visibility = View.VISIBLE
                }
                STATUS_PLAYING -> {
                    btnRecord!!.isEnabled = false
                    btnPlay!!.isEnabled = false
                    tvStatus!!.text = "Playing..."
                    pb!!.max = msg.arg1
                    pb!!.progress = msg.arg2
                    pb!!.visibility = View.VISIBLE
                }
                STATUS_RECORDING_END -> tvStatus!!.text = "Recording complete!"
                STATUS_PLAYING_END -> tvStatus!!.text = "Playing complete!"
                STATUS_NONE_PLAY -> Toast.makeText(baseContext, "File doesn't exist", Toast.LENGTH_SHORT).show()
            }
        }
    }


    /**
     * Starting thread to record sound
     * @param v - building block for interface components
     * @throws InterruptedException
     */
    @Throws(InterruptedException::class)
    fun onClickRecord(v: View?) {
        if (recorderThread != null && recorderThread!!.isAlive) {
            Log.d("Chieu","if")
            recorderThread!!.interrupt()
        } else {
            Log.d("Chieu","else")
            recorderThread = RecorderThread(myHandler)
            recorderThread!!.start()
        }
    }

    /**
     * Starting thread to play recorded sound
     * @param v - building block for interface components
     * @throws InterruptedException
     */
    @Throws(InterruptedException::class)
    fun onClickPlay(v: View?) {
        if (playerThread != null && playerThread!!.isAlive) {
            playerThread!!.interrupt()
        } else {
            playerThread = PlayerThread(myHandler)
            playerThread!!.start()
        }
    }
}