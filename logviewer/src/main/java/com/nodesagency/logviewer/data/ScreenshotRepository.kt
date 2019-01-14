package com.nodesagency.logviewer.data

import android.graphics.Bitmap
import android.net.Uri

interface ScreenshotRepository {


    /**
     * Saves screenshot associated with the logs
     * @returns screenshot uri
     */
    fun saveScreenshot(bitmap: Bitmap) : Uri

    /**
     * Deletes all the screenshots created
     */
    fun deleteAllScreenshots()
}