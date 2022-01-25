package org.g3.ffmpeglearning.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.layout_test.*
import org.g3.ffmpeglearning.AudioManager
import org.g3.ffmpeglearning.FileUtils
import org.g3.ffmpeglearning.R
import java.io.File


open class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_test)
        if (!checkPermission()) {
            requestPermission()
        }
        onClick()
    }

    protected var pathAudio: String = ""

    private fun onClick() {
        btnSelectAudio?.setOnClickListener {
            pickAudio()
        }

        btnReverse?.setOnClickListener {
            if (pathAudio.isNotEmpty()) {
                AudioManager.reverseAudio(pathAudio)
            }
        }

        btnVolumeDown?.setOnClickListener {
            if (pathAudio.isNotEmpty()) {
                AudioManager.volumeDown(pathAudio)
            }
        }

        btnVolumeUp?.setOnClickListener {
            if (pathAudio.isNotEmpty()) {
                AudioManager.volumeUp(pathAudio)
            }
        }

        btnSpeedUp?.setOnClickListener {
            if (pathAudio.isNotEmpty()) {
                AudioManager.speedUpAudio(pathAudio)
            }
        }

        btnSpeedDown?.setOnClickListener {
            if (pathAudio.isNotEmpty()) {
                AudioManager.speedDownAudio(pathAudio)
            }
        }

        btnConvertAudio?.setOnClickListener {
            if (pathAudio.isNotEmpty()) {
                AudioManager.convertToFormat(pathAudio, "wav")
            }
        }

        btnGetInfo?.setOnClickListener {
            if (pathAudio.isNotEmpty()) {
                val info = AudioManager.getInfoFileAudio(pathAudio)
                btnGetInfo.text = "Info $info"
            }
        }
    }

    protected fun pickAudio() {
        val intent_upload = Intent()
        intent_upload.type = "audio/*"
        intent_upload.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent_upload, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode === 1) {
            if (resultCode === RESULT_OK) {

                //the selected audio.
                data?.let {
                    val uri: Uri? = data.data
                    Log.d("TTTT", "uri = ${uri.toString()}")
                    val file = File(uri!!.path)
                    val path = file.absolutePath

                    pathAudio = FileUtils.getPath(this, uri)
                    btnSelectAudio.text = pathAudio
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    private fun checkPermission(): Boolean {
        val result: Int = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.READ_EXTERNAL_STORAGE)
        val result1: Int = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val result2: Int = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.MODIFY_AUDIO_SETTINGS)
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        Dexter.withActivity(this)
            .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.MODIFY_AUDIO_SETTINGS)
            .withListener(object : MultiplePermissionsListener {
                override
                fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                }

                override
                fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest?>?, token: PermissionToken?) {
                }
            }).check()
    }

}