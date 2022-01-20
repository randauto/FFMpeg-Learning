package org.g3.ffmpeglearning

import android.media.AudioFormat
import android.media.MediaRecorder
import android.os.Handler
import android.os.Message
import org.g3.ffmpeglearning.thead.ExtAudioRecorder
import java.lang.Exception
import java.util.concurrent.TimeUnit

class RecorderThread
/**
 * Constructor of the class RecorderThread
 *
 * @param handler - handler to send a message about the progress of the operation
 */(
    /**
     * Handler to send a message about the progress of the operation
     */
    var myHandler: Handler
) : Thread() {
    var recorder: ExtAudioRecorder = ExtAudioRecorder(
        true,
        MediaRecorder.AudioSource.MIC, 44100,
        AudioFormat.CHANNEL_CONFIGURATION_MONO,
        AudioFormat.ENCODING_PCM_8BIT
    )

    /**
     * operations performed when starting the thread
     */
    override fun run() {
        var msg: Message
        try {
            recorder.release()
            recorder.instanse
            recorder.prepare()
            recorder.start()

            // information about recording - progressBar and TextView
            for (i in 0..10) {
                TimeUnit.SECONDS.sleep(1)
                msg = myHandler.obtainMessage(ReverseAudioActivity().STATUS_RECORDING, 10, i)
                myHandler.sendMessage(msg)
            }
            myHandler.sendEmptyMessage(ReverseAudioActivity().STATUS_RECORDING_END)
            TimeUnit.MILLISECONDS.sleep(1000)
            myHandler.sendEmptyMessage(ReverseAudioActivity().STATUS_NONE)
            recorder.release()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}