package org.g3.ffmpeglearning.module

abstract class BaseAudioCmd {
    abstract fun getStringCommand(inputFile: String, outPutFile: String) : String
}