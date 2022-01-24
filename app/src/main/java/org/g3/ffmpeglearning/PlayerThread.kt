package org.g3.ffmpeglearning

import android.media.MediaPlayer
import android.os.Environment
import android.os.Handler
import android.os.Message
import org.g3.ffmpeglearning.ui.ReverseAudioActivity
import java.io.*
import java.lang.Exception
import java.util.concurrent.TimeUnit

class PlayerThread
/**
 * Constructor of class PlayerThread
 *
 * @param myHandler - handler to send a message about the progress of the operation
 */(
    /**
     * Handler to send a message about the progress of the operation
     */
    var myHandler: Handler
) : Thread() {
    /**
     * Create mediaplayer
     */
    var mediaPlayer: MediaPlayer? = MediaPlayer()

    /**
     * filePath of the recorded sound
     */
    private val fileName = Environment.getExternalStorageDirectory()
        .toString() + "/Audio007wakeUp.wav"

    /**
     * filePath of the inverted sound
     */
    private val revFileName = Environment.getExternalStorageDirectory()
        .toString() + "/Audio007wakeUp.wav"

    /**
     * operations performed when starting the thread
     */
    override fun run() {
        var msg: Message
        try {
            releasePlayer()
            reverseSound()
            mediaPlayer = MediaPlayer()
            mediaPlayer!!.setDataSource(revFileName)
            mediaPlayer!!.prepare()
            mediaPlayer!!.start()

            // information about playing - progressBar and TextView
            for (i in 0..10) {
                TimeUnit.SECONDS.sleep(1)
                msg = myHandler.obtainMessage(ReverseAudioActivity().STATUS_PLAYING, 10, i)
                myHandler.sendMessage(msg)
            }
            myHandler.sendEmptyMessage(ReverseAudioActivity().STATUS_PLAYING_END)
            TimeUnit.MILLISECONDS.sleep(1000)
            myHandler.sendEmptyMessage(ReverseAudioActivity().STATUS_NONE)
            releasePlayer()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Reverse recorded sound. Reading file to array bytes, reverse,
     * and writing in new file
     *
     */
    @Throws(InterruptedException::class)
    private fun reverseSound() {
        try {

            //Reading file to main array  bytes 'music'
            val `in`: InputStream = FileInputStream(fileName)
            val music = ByteArray(`in`.available())
            val bis = BufferedInputStream(`in`, 8000)
            val dis = DataInputStream(bis)
            var i = 0
            while (dis.available() > 0) {
                music[i] = dis.readByte()
                i++
            }

            //create buffer array with bytes without files header information
            //inverse bytes in array
            val len = music.size
            val buff = ByteArray(len)
            for (y in 17 until music.size - 1) {
                buff[len - y - 1] = music[y]
            }

            //put inversed bytes in buffers array to main array 'music'
            var y = 17
            while (y > music.size - 1) {
                music[y] = buff[y]
                y++
            }
            dis.close()

            //write reversed sound to the new file
            val os: OutputStream = FileOutputStream(revFileName)
            val bos = BufferedOutputStream(os, 8000)
            val dos = DataOutputStream(bos)
            dos.write(music, 0, music.size - 1)
            dos.flush()
        } catch (ioe: IOException) {
            myHandler.sendEmptyMessage(ReverseAudioActivity().STATUS_NONE_PLAY)
        }
    }

    /**
     * Release player
     */
    private fun releasePlayer() {
        if (mediaPlayer != null) {
            mediaPlayer!!.release()
            mediaPlayer = null
        }
    }

    /**
     * Stop player
     */
    fun playStop() {
        if (mediaPlayer != null) {
            mediaPlayer!!.stop()
        }
    }
}