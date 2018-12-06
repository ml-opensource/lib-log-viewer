package com.nodesagency.logtestapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nodesagency.logviewer.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // Insert a couple of sample logs...

        Logger.log("This is a verbose message")
        Logger.e("This is an error message", tag = "MainActivity")
        Logger.d("This is a debug message", tag = "MainActivity")
        Logger.w("This is a warning message", tag = "MainActivity")
        Logger.log("This is another verbose message")
        Logger.log("This is a third verbose message")
        Logger.i("This is an info message")
        Logger.log(severityLevel = CommonSeverityLevels.ASSERT.severity.level, message =  "This is an assert message")
        Logger.wtf("This is a WTF message")

        Logger.i(categoryName = "Database write", message = "Writing to the database")
        Logger.d(categoryName = "Network call", message = "Calling the network")


        // ...and run the LogViewerActivity

        LogViewerActivity
            .createIntent(this)
            .let(::startActivity)

        finish()
    }
}
