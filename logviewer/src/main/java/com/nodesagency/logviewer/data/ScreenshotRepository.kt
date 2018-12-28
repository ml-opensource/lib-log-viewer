package com.nodesagency.logviewer.data

import android.graphics.Bitmap
import android.net.Uri

interface ScreenshotRepository {


    fun saveScreenshot(bitmap: Bitmap) : Uri

    fun deleteAllScreenshots()
}