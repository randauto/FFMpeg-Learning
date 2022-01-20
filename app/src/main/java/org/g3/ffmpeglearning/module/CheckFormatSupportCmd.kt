package org.g3.ffmpeglearning.module

open class CheckFormatSupportCmd : BaseAudioCmd() {
    override fun getStringCommand(pathInput: String, outputPath: String): String {
        return "-formats"
    }
}