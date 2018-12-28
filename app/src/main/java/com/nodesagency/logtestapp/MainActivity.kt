package com.nodesagency.logtestapp

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.nodesagency.logviewer.LogViewerActivity
import com.nodesagency.logviewer.Logger
import com.nodesagency.logviewer.wtf
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var exampleLogGenerator: ExampleLogGenerator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        exampleLogGenerator = ExampleLogGenerator(
            logCount = 1000,
            delayBetweenLogsMilliseconds = 500,
            undelayedInitialLogCount = 6
        )
    }

    override fun onResume() {
        super.onResume()

        exampleLogGenerator.stop() // Stop generating logs when the LogViewerActivity is stopped

        setAllButtonListener(this)
    }

    override fun onPause() {
        super.onPause()

        setAllButtonListener(null)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.startButton -> startLogViewerActivityWithLiveLogging()
            R.id.clearAllLogsButton -> clearAllLogs()
            R.id.logScreenBtn -> Logger.log(
                tag = "ScreenTag",
                categoryName = "Screenshot",
                message = "Screeshot logged",
                includeScreenshot = true
            )
        }
    }

    private fun setAllButtonListener(listener: View.OnClickListener?) {
        startButton.setOnClickListener(listener)
        logScreenBtn.setOnClickListener(listener)
        clearAllLogsButton.setOnClickListener(listener)
    }

    private fun startLogViewerActivityWithLiveLogging() {
        exampleLogGenerator.start()

        LogViewerActivity
            .createIntent(this)
            .let { startActivity(it) }
    }

    private fun clearAllLogs() = runBlocking {
        Logger.clearAllLogs().join()
    }
}
