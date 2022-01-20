package org.g3.ffmpeglearning.module

open class SpeedUpDownAudioCmd : BaseAudioCmd() {
    var atempo: Float = 2.0f

    override fun getStringCommand(pathInput: String, outputPath: String): String {
        return "-i $pathInput -af atempo=$atempo $outputPath"
    }
}