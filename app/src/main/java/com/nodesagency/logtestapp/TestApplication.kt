package com.nodesagency.logtestapp

import android.app.Application
import com.nodesagency.logviewer.Logger
import timber.log.Timber

class TestApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize the logger library here
        Logger.initialize(this)
        Timber.plant(Logger.tree)
    }
}
