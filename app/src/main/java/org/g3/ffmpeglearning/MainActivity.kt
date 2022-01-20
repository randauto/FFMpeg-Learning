package org.g3.ffmpeglearning

import android.Manifest
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.speech.tts.TextToSpeech
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import android.widget.Toast

import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

class MainActivity : AppCompatActivity() {

    var textToSpeech: TextToSpeech? = null
    var mp: MediaPlayer? = null

    private var mAudioFilename = ""
    private val mUtteranceID = "totts"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!checkPermission()) {
            requestPermission()
        }
        // create an object textToSpeech and adding features into it
        textToSpeech = TextToSpeech(applicationContext) { i ->
            // if No error is found then only it will run
            if (i != TextToSpeech.ERROR) {
                // To Choose language of speech
                textToSpeech!!.language = Locale.ENGLISH
            }
        }
        btnText.setOnClickListener {

            textToSpeech!!.speak(edit_text_speech.text.toString(), TextToSpeech.QUEUE_FLUSH, null)
            fileCreate()
        }



    }



    fun fileCreate() {

        val myHashRender= HashMap<String?, String?>()

        val destFileName = Environment.getExternalStoragePublicDirectory("/Audio/").toString() + "${System.currentTimeMillis()}.wav"
        Toast.makeText(this, "" + destFileName, Toast.LENGTH_SHORT).show()
        myHashRender[TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID] = edit_text_speech.text.toString()
        textToSpeech!!.synthesizeToFile(edit_text_speech.text.toString(), myHashRender, destFileName)

    }
    private fun checkPermission(): Boolean {
        val result: Int = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.READ_EXTERNAL_STORAGE)
        val result1: Int = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val result2: Int = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.MODIFY_AUDIO_SETTINGS)
        return if (result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED) {
            true
        } else {
            false
        }
    }

    private fun requestPermission() {
        Dexter.withActivity(this)
            .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.MODIFY_AUDIO_SETTINGS)
            .withListener(object : MultiplePermissionsListener {
                override
                fun onPermissionsChecked(report: MultiplePermissionsReport?) {}
                override
                fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest?>?, token: PermissionToken?) {}
            }).check()
    }



}