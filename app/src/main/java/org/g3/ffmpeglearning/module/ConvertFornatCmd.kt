package org.g3.ffmpeglearning.module

open class ConvertFornatCmd : BaseAudioCmd() {
    override fun getStringCommand(pathInput: String, outputPath: String): String {
        return "-i $pathInput $outputPath"
    }
}