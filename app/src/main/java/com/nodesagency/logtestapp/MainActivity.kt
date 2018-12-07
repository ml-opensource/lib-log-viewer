package com.nodesagency.logtestapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.nodesagency.logviewer.LogViewerActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var logGenerator: LogGenerator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        logGenerator = LogGenerator(
            logCount = 1000,
            delayBetweenLogsMilliseconds = 1000,
            undelayedInitialLogCount = 6
        )
    }

    override fun onResume() {
        super.onResume()

        logGenerator.stop()

        startButton.setOnClickListener(this)
    }

    override fun onPause() {
        super.onPause()

        startButton.setOnClickListener(null)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.startButton -> startLogViewerActivityWithLiveLogging()
        }
    }

    private fun startLogViewerActivityWithLiveLogging() {
        logGenerator.start()

        LogViewerActivity
            .createIntent(this)
            .let { startActivity(it) }
    }
}
