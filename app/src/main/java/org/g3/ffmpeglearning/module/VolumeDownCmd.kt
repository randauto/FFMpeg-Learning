package org.g3.ffmpeglearning.module

open class VolumeDownCmd : BaseAudioCmd() {
    var volumne: Float = 0.0f

    override fun getStringCommand(pathInput: String, outputPath: String): String {
        return "-i $pathInput -af 'volume=$volumne' $outputPath"
    }
}