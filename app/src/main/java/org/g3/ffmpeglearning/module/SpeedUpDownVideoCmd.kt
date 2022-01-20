package org.g3.ffmpeglearning.module

open class SpeedUpDownVideoCmd : BaseAudioCmd() {
    var setpts: Float = 0.125f

    override fun getStringCommand(pathInput: String, outputPath: String): String {
        return "-i $pathInput -filter:v \"setpts=$setpts*PTS\" $outputPath"
    }
}