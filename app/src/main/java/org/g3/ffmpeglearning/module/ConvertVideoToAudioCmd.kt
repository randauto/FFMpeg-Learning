package org.g3.ffmpeglearning.module

open class ConvertVideoToAudioCmd : BaseAudioCmd() {
    var formatAudio: String = "mp3"
    override fun getStringCommand(pathInput: String, outputPath: String): String {
        return "-i $pathInput  -vn -ar 44100 -ac 2 -ab 320 -f $formatAudio $outputPath"
    }
}