package com.nodesagency.logtestapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nodesagency.logviewer.LogViewerActivity
import com.nodesagency.logviewer.Logger

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // Insert a couple of sample logs...

        Logger.log("This is a general message")
        Logger.log(categoryName = "Database write", message = "Writing to the database")
        Logger.log(categoryName = "Network call", message = "Calling the network")

        // ...and run the LogViewerActivity

        LogViewerActivity
            .createIntent(this)
            .let(::startActivity)

        finish()
    }
}
