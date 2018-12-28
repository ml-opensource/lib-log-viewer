package com.nodesagency.logviewer.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import com.nodesagency.logviewer.BuildConfig
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class ScreenshotRepositoryImpl(val context: Context) : ScreenshotRepository{

    private val storage = context.filesDir

    companion object {
        private const val NAME_PREFIX = "SCREEN_"
    }

    override fun saveScreenshot(bitmap: Bitmap): Uri {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val fileName =  "$NAME_PREFIX$timeStamp.jpg"
        val file = File("${storage.path}${File.separator}$fileName")
        file.createNewFile()

        val stream = FileOutputStream(file)
        stream.write(bitmap.toByteArray())
        stream.flush()
        stream.close()
        return  FileProvider.getUriForFile(context, "${BuildConfig.APPLICATION_ID}.fileprovider", file)

    }

    override fun deleteAllScreenshots() {
        storage.listFiles()
            .filter { it.name.startsWith(NAME_PREFIX) }
            .forEach { it.delete() }

    }

    private fun Bitmap.toByteArray() : ByteArray {
        val output = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 100, output)
        return output.toByteArray()
    }



}