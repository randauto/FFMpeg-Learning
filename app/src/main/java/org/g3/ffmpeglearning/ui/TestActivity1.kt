package org.g3.ffmpeglearning.ui/*
package org.o7planning.covert_text_to_audio

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.arthenica.mobileffmpeg.Config
import com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL
import com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS
import com.arthenica.mobileffmpeg.FFmpeg
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.layout_test.*
import java.io.File


class TestActivity1 : AppCompatActivity() {

    var mListAudio = ArrayList<String>()
    var mListVideo = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_test)
        if (!checkPermission()) {
            requestPermission()
        }
        mListAudio = scanDeviceForMp3Files()
        onClick()
    }

    private fun onClick() {
        btRevers.setOnClickListener {
//            runReverseAudio()
            pickAudio()
        }
    }

    private fun pickAudio() {
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

                    val filePath = FileUtils.getPath(this, uri)
                    tuanTest(filePath)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun tuanTest(path: String) {
        val pathInput = path
        val fileIn = File(pathInput)
        val rootFolder = fileIn.parentFile.absolutePath
        val outputPath = rootFolder + File.separator + "camonanh22.mp3"
        val fileOut = File(outputPath)
        if (fileOut.exists()) {
            fileOut.delete()
        }
        val rc: Int = FFmpeg.execute("-i $pathInput -vf reverse -af areverse $outputPath")
        Log.e("TAG", "rc:${"-i $pathInput -vf reverse -af areverse $outputPath"}")
        when (rc) {
            RETURN_CODE_SUCCESS -> {
                Log.i("TAG", "Command execution completed successfully.")
            }
            RETURN_CODE_CANCEL -> {
                Log.i("TAG", "Command execution cancelled by user.")
            }
            else -> {
                Log.i("TAG", String.format("Command execution failed with rc=%d and the output below.", rc))
                Config.printLastCommandOutput(Log.INFO)
            }
        }
    }

    private fun scanDeviceForMp3Files(): ArrayList<String> {
        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"
        val projection = arrayOf(
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DURATION
        )
        val sortOrder = MediaStore.Audio.AudioColumns.TITLE + " COLLATE LOCALIZED ASC"
        val mp3Files = ArrayList<String>()
        var cursor: Cursor? = null
        try {
            val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            cursor = contentResolver.query(uri, projection, selection, null, sortOrder)
            if (cursor != null) {
                cursor.moveToFirst()
                while (!cursor.isAfterLast()) {
                    val title: String = cursor.getString(0)
                    val artist: String = cursor.getString(1)
                    val path: String = cursor.getString(2)
                    val displayName: String = cursor.getString(3)
                    val songDuration: String = cursor.getString(4)
                    cursor.moveToNext()
                    if (path != null && path.endsWith(".mp3")) {
                        mp3Files.add(path)
                    }
                }
            }

            // print to see list of mp3 files
            for (file in mp3Files) {
                Log.i("TAG", file)
            }
        } catch (e: Exception) {
            Log.e("TAG", e.toString())
        } finally {
            if (cursor != null) {
                cursor.close()
            }
        }
        return mp3Files
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
                fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                }

                override
                fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest?>?, token: PermissionToken?) {
                }
            }).check()
    }

}*/