package org.g3.ffmpeglearning.module

open class ReverseCmd : BaseAudioCmd() {
    override fun getStringCommand(pathInput: String, outputPath: String): String {
        return "-i $pathInput -vf reverse -af areverse $outputPath"
    }
}