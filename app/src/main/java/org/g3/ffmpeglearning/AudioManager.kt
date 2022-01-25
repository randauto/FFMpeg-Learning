package org.g3.ffmpeglearning

import android.util.Log
import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.FFprobeKit
import org.g3.ffmpeglearning.module.ConvertFornatCmd
import org.g3.ffmpeglearning.module.ReverseCmd
import org.g3.ffmpeglearning.module.SpeedUpDownAudioCmd
import org.g3.ffmpeglearning.module.VolumeDownCmd
import java.io.File
import java.lang.StringBuilder
import android.media.MediaMetadataRetriever




object AudioManager {

    fun volumeUp(path: String) {
        val outputPath = "/storage/emulated/0/Download/camonanh_volumeUp.mp3"
        val cmd = VolumeDownCmd()
        cmd.volumne = 1.5f
        val ffprobeCommand = cmd.getStringCommand(path, outputPath)
        checkExits(outputPath)
        FFmpegKit.executeAsync(ffprobeCommand, { session ->
            val state = session.state
            val returnCode = session.returnCode

            // CALLED WHEN SESSION IS EXECUTED
            Log.d("TTTT", String.format("FFmpeg process exited with state %s and rc %s.%s", state, returnCode, session.failStackTrace))
        }, {
            // CALLED WHEN SESSION PRINTS LOGS
        }) {
            // CALLED WHEN SESSION GENERATES STATISTICS
        }
    }

    fun volumeDown(path: String) {
        val outputPath = "/storage/emulated/0/Download/camonanh_volumeDown.mp3"
        val cmd = VolumeDownCmd()
        cmd.volumne = 0.5f
        val ffprobeCommand = cmd.getStringCommand(path, outputPath)
        checkExits(outputPath)
        FFmpegKit.executeAsync(ffprobeCommand, { session ->
            val state = session.state
            val returnCode = session.returnCode

            // CALLED WHEN SESSION IS EXECUTED
            Log.d("TTTT", String.format("FFmpeg process exited with state %s and rc %s.%s", state, returnCode, session.failStackTrace))
        }, {
            // CALLED WHEN SESSION PRINTS LOGS
        }) {
            // CALLED WHEN SESSION GENERATES STATISTICS
        }
    }

    fun getInfoFileAudio(path: String) : String {
        val mediaInformation = FFprobeKit.getMediaInformation(path)
        val mediaInfo = mediaInformation.mediaInformation
        val info = StringBuilder()
        info.append(mediaInfo.allProperties)
        Log.d("TTT", "Info: ${mediaInfo.allProperties}" )
        return info.toString()
    }

    fun reverseAudio(path: String) {
        val outputPath = "/storage/emulated/0/Download/camonanh_reverseAudio.mp3"
        val cmd = ReverseCmd()
        val ffprobeCommand = cmd.getStringCommand(path, outputPath)
        checkExits(outputPath)
        FFmpegKit.executeAsync(ffprobeCommand, { session ->
            val state = session.state
            val returnCode = session.returnCode

            // CALLED WHEN SESSION IS EXECUTED
            Log.d("TTTT", String.format("FFmpeg process exited with state %s and rc %s.%s", state, returnCode, session.failStackTrace))
        }, {
            // CALLED WHEN SESSION PRINTS LOGS
        }) {
            // CALLED WHEN SESSION GENERATES STATISTICS
        }

    }

    private fun checkExits(outputPath: String) {
        val fileOut = File(outputPath)
        if (fileOut.exists()) {
            try {
                val result = fileOut.deleteOnExit()
                Log.d("TTTT", "exits and deleted")

            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    /**
     * Speed up audio value between 0.5 and 2.0
     */
    fun speedUpAudio(pathAudio: String) {
        val outputPath = "/storage/emulated/0/Download/camonanh_speedUp.mp3"
        val cmd = SpeedUpDownAudioCmd()
        cmd.atempo = 1.5f
        val ffprobeCommand = cmd.getStringCommand(pathAudio, outputPath)
        checkExits(outputPath)
        FFmpegKit.executeAsync(ffprobeCommand, { session ->
            val state = session.state
            val returnCode = session.returnCode

            // CALLED WHEN SESSION IS EXECUTED
            Log.d("TTTT", String.format("FFmpeg process exited with state %s and rc %s.%s", state, returnCode, session.failStackTrace))
        }, {
            // CALLED WHEN SESSION PRINTS LOGS
        }) {
            // CALLED WHEN SESSION GENERATES STATISTICS
        }
    }

    /**
     * Speed down audio value between 0.5 and 2.0
     */
    fun speedDownAudio(pathAudio: String) {
        val outputPath = "/storage/emulated/0/Download/camonanh_speedDown.mp3"
        val cmd = SpeedUpDownAudioCmd()
        cmd.atempo = 0.5f
        val ffprobeCommand = cmd.getStringCommand(pathAudio, outputPath)
        checkExits(outputPath)
        FFmpegKit.executeAsync(ffprobeCommand, { session ->
            val state = session.state
            val returnCode = session.returnCode

            // CALLED WHEN SESSION IS EXECUTED
            Log.d("TTTT", String.format("FFmpeg process exited with state %s and rc %s.%s", state, returnCode, session.failStackTrace))
        }, {
            // CALLED WHEN SESSION PRINTS LOGS
        }) {
            // CALLED WHEN SESSION GENERATES STATISTICS
        }
    }

    fun convertToFormat(pathAudio: String, formatAUdio: String) {
        val outputPath = "/storage/emulated/0/Download/camonanh_convertToFormat.$formatAUdio"
        val cmd = ConvertFornatCmd()
        val ffprobeCommand = cmd.getStringCommand(pathAudio, outputPath)
        checkExits(outputPath)
        FFmpegKit.executeAsync(ffprobeCommand, { session ->
            val state = session.state
            val returnCode = session.returnCode

            // CALLED WHEN SESSION IS EXECUTED
            Log.d("TTTT", String.format("FFmpeg process exited with state %s and rc %s.%s", state, returnCode, session.failStackTrace))
        }, {
            // CALLED WHEN SESSION PRINTS LOGS
        }) {
            // CALLED WHEN SESSION GENERATES STATISTICS
        }
    }

    fun getDuration(file: File): Long {
        val mediaMetadataRetriever = MediaMetadataRetriever()
        mediaMetadataRetriever.setDataSource(file.absolutePath)
        val durationStr = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
//        return formateMilliSeccond(durationStr!!.toLong())
        return durationStr!!.toLong()
    }

    private fun formateMilliSeccond(milliseconds: Long): String? {
        var finalTimerString = ""
        var secondsString = ""

        // Convert total duration into time
        val hours = (milliseconds / (1000 * 60 * 60)).toInt()
        val minutes = (milliseconds % (1000 * 60 * 60)).toInt() / (1000 * 60)
        val seconds = (milliseconds % (1000 * 60 * 60) % (1000 * 60) / 1000).toInt()

        // Add hours if there
        if (hours > 0) {
            finalTimerString = "$hours:"
        }

        // Prepending 0 to seconds if it is one digit
        secondsString = if (seconds < 10) {
            "0$seconds"
        } else {
            "" + seconds
        }
        finalTimerString = "$finalTimerString$minutes:$secondsString"

        //      return  String.format("%02d Min, %02d Sec",
        //                TimeUnit.MILLISECONDS.toMinutes(milliseconds),
        //                TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
        //                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));

        // return timer string
        return finalTimerString
    }

    fun formatSeconds(millisSeconds: Long): String? {
        var time = millisSeconds
        if (time < 1000) {
            return "00:00"
        }
        val seconds: Long = millisSeconds / 1000 % 60
        val minutes: Long = millisSeconds / (1000 * 60) % 60
        val hours: Long = millisSeconds / (1000 * 60 * 60)

        val b = StringBuilder()
        //b.append(if (hours == 0L) "00" else if (hours < 10) "0$hours" else hours.toString())
        //b.append(":")
        b.append(if (minutes == 0L) "00" else if (minutes < 10) "0$minutes" else minutes.toString())
        b.append(":")
        b.append(if (seconds == 0L) "00" else if (seconds < 10) "0$seconds" else seconds.toString())
        return b.toString()
    }

}